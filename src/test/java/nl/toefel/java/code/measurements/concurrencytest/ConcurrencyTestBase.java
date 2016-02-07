/*
 *
 *     Copyright 2016 Christophe Hesters
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */

package nl.toefel.java.code.measurements.concurrencytest;

import nl.toefel.java.code.measurements.api.Snapshot;
import nl.toefel.java.code.measurements.api.StatisticalDistribution;
import nl.toefel.java.code.measurements.api.Statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static java.lang.Math.max;
import static nl.toefel.java.code.measurements.concurrencytest.TaskFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is the base class for testing a {@link Statistics} implementation by creating a number of concurrent tasks
 * that call most of the API methods (with the exception of the reset() and xxxSnapshotAndReset()). The expected number
 * of registered statistics is checked after all tasks have finished.
 * <p>
 * Sample usage for implementations:
 * <p>
 * <pre>
 * {@code
 *     public class SomeStatisticsImplConcurrencyTest extends ConcurrencyTestBase {
 *           @Override
 *           protected Statistics createStatistics() {
 *               return new SomeStatisticsImpl();
 *           }
 *
 *           @Test
 *           public void testConcurrencyOneThreadEach() {
 *               // 100 concurrent threads per method, 50000 iterations per thread
 *               runConcurrencyTest(100, 50000);
 *           }
 *      }
 * }
 * </pre>
 */
public abstract class ConcurrencyTestBase {

	private static final String OCCURRENCES_NAME = "occurrences";
	public static final String OCCURRENCE_NAME = "occurrence";
	public static final String SAMPLE_NAME = "sample";
	public static final String DURATION_NAME = "duration";

	private CountDownLatch starter;
	private CountDownLatch finisher;

	private List<ConcurrentTask> concurrentTasks;

	public static Statistics subject;

	protected abstract Statistics createStatistics();

	/**
	 * Runs a concurrency tests without calling the reset methods. The given number of threads is used for all tasks except for the
	 * {@link GetSnapshotsTask} because they are quite heavy will probably last longer than all the other threads.
	 *
	 * @param threadsPerTask number of tasks to create of each kind
	 * @param loopsPerThread time of executions per task
	 */
	protected void runConcurrencyTest(int threadsPerTask, int loopsPerThread) {
		runConcurrencyTest(threadsPerTask, threadsPerTask, threadsPerTask, max(threadsPerTask / 4, 1), threadsPerTask, loopsPerThread);
	}

	protected void runConcurrencyTest(int occurrenceThreads,
									  int samplesThreads,
									  int durationsThreads,
									  int snapshotThreads,
									  int findStatisticThreads,
									  int loopsPerThread) {
		setupTestForAllSampleMethods(occurrenceThreads, samplesThreads, durationsThreads, snapshotThreads, findStatisticThreads, loopsPerThread);

		startTest();
		waitTillAllTasksFinish();

		Snapshot snapshot = subject.getSnapshot();

		assertNoTasksIndicateFailure();
		assertOnlyExpectedNamesAreRegistered(snapshot);
		assertCountedMeasurementsAreAsExpectedForSnapshot(occurrenceThreads, samplesThreads, durationsThreads, loopsPerThread, snapshot);
		assertCountedMeasurementsAreEqualToPostedEventsByTasksForSnapshot(snapshot);
		assertCountedMeasurementsAreAsExpectedThroughApi(occurrenceThreads, samplesThreads, durationsThreads, loopsPerThread);

	}

	/**
	 * Tests that if there are multiple concurrent writes and multiple threads that use a variant of getSnapshotAndReset()
	 * the total amount of data spread to those snapshots must be equal to the toal input, otherwise values got lost or
	 * there are illegal extra values detected. The parameters can be used to tweak the load
	 *
	 * @param writerThreads threads that add values to occurrences, durations and samples
	 * @param resetThreads threads that get snapshots (of all kinds) with reset
	 * @param loopsPerWriterThread
	 * @param loopsPerResetThread
	 */
	protected void runResetTest(int writerThreads,
								int resetThreads,
								int loopsPerWriterThread,
								int loopsPerResetThread) {
		setupTestForResetMethods(writerThreads, resetThreads, loopsPerWriterThread, loopsPerResetThread);

		startTest();
		waitTillAllTasksFinish();

		assertNoTasksIndicateFailure();
		assertThatResetTasksRecordedSamples(writerThreads, loopsPerWriterThread, subject.getSnapshotAndReset());
	}

	private void assertThatResetTasksRecordedSamples(final int writerThreads, final int loopsPerWriterThread, final Snapshot remainder) {
		long totalSamples = ConcurrencyTestBase.sampleCountOrZero(remainder.getSamples().get(SAMPLE_NAME));
		long totalDurations = ConcurrencyTestBase.sampleCountOrZero(remainder.getDurations().get(DURATION_NAME));
		long totalOccurrences = ConcurrencyTestBase.occurrenceCountOrZero(remainder.getOccurrences().get(OCCURRENCE_NAME));

		// these debug statements help to see if multiple tasks collected values and that not all was contained within the snapshot itself.
		// System.out.println("debug: remainders: " + totalDurations + " " + totalOccurrences + " " + totalSamples);

		// collect all the values from the reset tasks
		for (ConcurrentTask task : concurrentTasks) {
			if (task instanceof ResetBaseTask) {
				totalSamples += ((ResetBaseTask) task).samples;
				totalDurations += ((ResetBaseTask) task).durations;
				totalOccurrences += ((ResetBaseTask) task).occurrences;
				//System.out.println("debug:  " + totalDurations + " " + totalOccurrences + " " + totalSamples);
			}
		}

		assertThat(totalSamples).as("total samples").isEqualTo(writerThreads * loopsPerWriterThread);
		assertThat(totalDurations).as("total durations").isEqualTo(writerThreads * loopsPerWriterThread);
		assertThat(totalOccurrences).as("total occurrences").isEqualTo(writerThreads * loopsPerWriterThread);
	}

	private void setupTestForAllSampleMethods(int occurrenceThreads,
											  int sampleThreads,
											  int durationThreads,
											  int snapshotThreads,
											  int threadsPerFindMethod,
											  int loopsPerThread) {
		// addOccurrence and addOccurrences are two different tasks, but use the same occurrenceThreads count
		// each find method is also run concurrently findOccurrence, findDuration and findSampleDistribution
		int totalThreads = (2 * occurrenceThreads) + sampleThreads + durationThreads + snapshotThreads + (3 * threadsPerFindMethod);
		subject = createStatistics();
		starter = new CountDownLatch(1);
		finisher = new CountDownLatch(totalThreads);

		concurrentTasks = new ArrayList<ConcurrentTask>();
		addConcurrentTasks(OCCURRENCE_TASK_FACTORY, OCCURRENCE_NAME, occurrenceThreads, loopsPerThread);
		addConcurrentTasks(OCCURRENCES_TASK_FACTORY, OCCURRENCES_NAME, occurrenceThreads, loopsPerThread);
		addConcurrentTasks(SAMPLE_TASK_FACTORY, SAMPLE_NAME, sampleThreads, loopsPerThread);
		addConcurrentTasks(DURATION_TASK_FACTORY, DURATION_NAME, durationThreads, loopsPerThread);
		addConcurrentTasks(GET_SNAPSHOT_TASK_FACTORY, "", snapshotThreads, loopsPerThread);
		addConcurrentTasks(FIND_SAMPLE_DISTRIBUTION_TASK_FACTORY, SAMPLE_NAME, threadsPerFindMethod, loopsPerThread);
		addConcurrentTasks(FIND_OCCURRENCE_TASK_FACTORY, OCCURRENCE_NAME, threadsPerFindMethod, loopsPerThread);
		addConcurrentTasks(FIND_DURATION_TASK_FACTORY, DURATION_NAME, threadsPerFindMethod, loopsPerThread);

		assertThat(concurrentTasks).hasSize(totalThreads);

		prepareThreads();
		System.out.println(String.format("created %d threads ready to fire for concurrency test", concurrentTasks.size()));
	}

	private void setupTestForResetMethods(int writerThreads,
										  int resetThreads,
										  int loopsPerThread,
										  int loopsPerResetThread) {
		int totalThreads = (3 * writerThreads) + (4 * resetThreads);
		subject = createStatistics();
		starter = new CountDownLatch(1);
		finisher = new CountDownLatch(totalThreads);

		concurrentTasks = new ArrayList<ConcurrentTask>();
		addConcurrentTasks(OCCURRENCE_TASK_FACTORY, OCCURRENCE_NAME, writerThreads, loopsPerThread);
		addConcurrentTasks(SAMPLE_TASK_FACTORY, SAMPLE_NAME, writerThreads, loopsPerThread);
		addConcurrentTasks(DURATION_TASK_FACTORY, DURATION_NAME, writerThreads, loopsPerThread);

		addConcurrentTasks(RESET_SNAPSHOT_TASK_FACTORY, "", resetThreads, loopsPerResetThread);
		addConcurrentTasks(RESET_ALLDURATIONS_SNAPSHOT_TASK_FACTORY, "", resetThreads, loopsPerResetThread);
		addConcurrentTasks(RESET_ALLOCCURRENCE_SNAPSHOT_TASK_FACTORY, "", resetThreads, loopsPerResetThread);
		addConcurrentTasks(RESET_ALLSAMPLES_SNAPSHOT_TASK_FACTORY, "", resetThreads, loopsPerResetThread);

		assertThat(concurrentTasks).hasSize(totalThreads);

		prepareThreads();
		System.out.println(String.format("created %d threads ready to fire for reset test", concurrentTasks.size()));
	}

	private void addConcurrentTasks(TaskFactory factory, String eventName, int instances, int loops) {
		for (int i = 0; i < instances; i++) {
			concurrentTasks.add(factory.create(starter, finisher, eventName, loops));
		}
	}

	private void prepareThreads() {
		for (ConcurrentTask task : concurrentTasks) {
			new Thread(task).start();
		}
	}

	private void startTest() {
		starter.countDown();
	}

	private void waitTillAllTasksFinish() {
		try {
			finisher.await();
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	private int countEventsPostedByTasks(String name) {
		int eventsPostedForKey = 0;
		for (ConcurrentTask task : concurrentTasks) {
			if (name.equals(task.getEventName()) && task.isWriter()) {
				eventsPostedForKey += task.getEventsExecuted();
			}
		}
		return eventsPostedForKey;
	}

	private void assertOnlyExpectedNamesAreRegistered(final Snapshot snapshot) {
		assertThat(snapshot.getOccurrences()).containsOnlyKeys(OCCURRENCE_NAME, OCCURRENCES_NAME);
		assertThat(snapshot.getDurations()).containsOnlyKeys(DURATION_NAME);
		assertThat(snapshot.getSamples()).containsOnlyKeys(SAMPLE_NAME);
	}

	private void assertNoTasksIndicateFailure() {
		for (ConcurrentTask task : concurrentTasks) {
			assertThat(task.isFailed()).as("task of type %s indicates failure", task.getClass().getSimpleName()).isFalse();
		}
	}

	private void assertCountedMeasurementsAreAsExpectedForSnapshot(int occurrenceThreads,
																   int samplesThreads,
																   int durationsThreads,
																   int loopsPerThread,
																   Snapshot snapshot) {
		assertThat(snapshot.getOccurrences().get(OCCURRENCE_NAME)).as("expected occurrences").isEqualTo(occurrenceThreads * loopsPerThread);
		assertThat(snapshot.getOccurrences().get(OCCURRENCES_NAME)).as("expected double occurrences").isEqualTo(occurrenceThreads * loopsPerThread * OccurrencesTask.OCCURRENCES_PER_RUN);
		assertThat(snapshot.getSamples().get(SAMPLE_NAME).getSampleCount()).as("expected samples").isEqualTo(samplesThreads * loopsPerThread);
		assertThat(snapshot.getDurations().get(DURATION_NAME).getSampleCount()).as("expected durations").isEqualTo(durationsThreads * loopsPerThread);
	}

	private void assertCountedMeasurementsAreEqualToPostedEventsByTasksForSnapshot(Snapshot snapshot) {
		int occurrencesPosted = countEventsPostedByTasks(OCCURRENCE_NAME);
		int doubleOccurrencesPosted = countEventsPostedByTasks(OCCURRENCES_NAME);
		int samplesPosted = countEventsPostedByTasks(SAMPLE_NAME);
		int durationsPosted = countEventsPostedByTasks(DURATION_NAME);

		assertThat(snapshot.getOccurrences().get(OCCURRENCE_NAME)).as("recorded occurrences").isEqualTo(occurrencesPosted);
		assertThat(snapshot.getOccurrences().get(OCCURRENCES_NAME)).as("recorded double occurrences").isEqualTo(doubleOccurrencesPosted * OccurrencesTask.OCCURRENCES_PER_RUN);
		assertThat(snapshot.getSamples().get(SAMPLE_NAME).getSampleCount()).as("recored samples").isEqualTo(samplesPosted);
		assertThat(snapshot.getDurations().get(DURATION_NAME).getSampleCount()).as("recored durations").isEqualTo(durationsPosted);
	}

	private void assertCountedMeasurementsAreAsExpectedThroughApi(final int occurrenceThreads, final int samplesThreads, final int durationsThreads, final int loopsPerThread) {
		// looking up individual values through the stats API
		assertThat(subject.findOccurrence(OCCURRENCE_NAME)).as("expected occurrences").isEqualTo(occurrenceThreads * loopsPerThread);
		assertThat(subject.findOccurrence(OCCURRENCES_NAME)).as("expected double occurrences").isEqualTo(occurrenceThreads * loopsPerThread * OccurrencesTask.OCCURRENCES_PER_RUN);
		assertThat(subject.findSampleDistribution(SAMPLE_NAME).getSampleCount()).as("expected samples").isEqualTo(samplesThreads * loopsPerThread);
		assertThat(subject.findDuration(DURATION_NAME).getSampleCount()).as("expected durations").isEqualTo(durationsThreads * loopsPerThread);

		// check the more specific snapshots as well
		assertThat(subject.getAllOccurrencesSnapshot().get(OCCURRENCE_NAME)).as("expected occurrences").isEqualTo(occurrenceThreads * loopsPerThread);
		assertThat(subject.getAllOccurrencesSnapshot().get(OCCURRENCES_NAME)).as("expected double occurrences").isEqualTo(occurrenceThreads * loopsPerThread * OccurrencesTask.OCCURRENCES_PER_RUN);
		assertThat(subject.getAllSamplesSnapshot().get(SAMPLE_NAME).getSampleCount()).as("expected samples").isEqualTo(samplesThreads * loopsPerThread);
		assertThat(subject.getAllDurationsSnapshot().get(DURATION_NAME).getSampleCount()).as("expected durations").isEqualTo(durationsThreads * loopsPerThread);
	}


	static long sampleCountOrZero(StatisticalDistribution statisticDistribution) {
		if (statisticDistribution == null) {
			return 0;
		} else {
			return statisticDistribution.getSampleCount();
		}
	}

	static long occurrenceCountOrZero(Long occurrenceCounter) {
		if (occurrenceCounter == null) {
			return 0;
		} else {
			return occurrenceCounter;
		}
	}
}

