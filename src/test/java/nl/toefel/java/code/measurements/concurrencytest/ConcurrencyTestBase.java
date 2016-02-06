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
import nl.toefel.java.code.measurements.api.Statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static nl.toefel.java.code.measurements.concurrencytest.TaskFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class ConcurrencyTestBase {

    private static final String OCCURRENCE_EVENT = "occurrence";
    private static final String SAMPLE_EVENT = "sample";
    private static final String DURATION_EVENT = "duration";
    private static final String FIND_STATISTIC_EVENT = "findstatistic";

    private CountDownLatch starter;
    private CountDownLatch finisher;

    private List<ConcurrentTask> concurrentTasks;

    public static Statistics subject;

    protected abstract Statistics createStatistics();

    void setupTestForAllSampleMethods(int occurrenceThreads,
                                      int sampleThreads,
                                      int durationThreads,
                                      int snapshotThreads,
                                      int findStatisticThreads,
                                      int loopsPerThread) {
        int totalThreads = occurrenceThreads + sampleThreads + durationThreads + snapshotThreads + findStatisticThreads;
        subject = createStatistics();
        starter = new CountDownLatch(1);
        finisher = new CountDownLatch(totalThreads);
        concurrentTasks = new ArrayList<ConcurrentTask>();
        addConcurrentTasks(OCCURRENCE_TASK_FACTORY, OCCURRENCE_EVENT, occurrenceThreads, loopsPerThread);
        addConcurrentTasks(SAMPLE_TASK_FACTORY, SAMPLE_EVENT, sampleThreads, loopsPerThread);
        addConcurrentTasks(DURATION_TASK_FACTORY, DURATION_EVENT, durationThreads, loopsPerThread);
        addConcurrentTasks(GET_SNAPSHOT_TASK_FACTORY, "", snapshotThreads, loopsPerThread);
        addConcurrentTasks(FIND_STATISTIC_TASK_FACTORY, FIND_STATISTIC_EVENT, findStatisticThreads, loopsPerThread);

        assertThat(concurrentTasks).hasSize(totalThreads);
        prepareThreads();
        System.out.println(String.format("created %d threads ready to fire", concurrentTasks.size()));
    }

    void addConcurrentTasks(TaskFactory factory, String eventName, int instances, int loops) {
        for (int i = 0; i < instances; i++) {
            concurrentTasks.add(factory.create(starter, finisher, eventName, loops));
        }
    }

    void prepareThreads() {
        for (ConcurrentTask task : concurrentTasks) {
            new Thread(task).start();
        }
    }

    private void start() {
        starter.countDown();
    }

    private void waitTillAllTasksFinish() {
        try {
            finisher.await();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    private int countEventsPostedByTasks(String occurrenceEvent) {
        int eventsPostedForKey = 0;
        for (ConcurrentTask task : concurrentTasks) {
            if (occurrenceEvent.equals(task.getEventName())) {
                eventsPostedForKey += task.getEventsPosted();
            }
        }
        return eventsPostedForKey;
    }

    protected void runConcurrencyTest(int threadsPerTask, int loopsPerThread) {
        runConcurrencyTest(threadsPerTask, threadsPerTask, threadsPerTask, threadsPerTask, threadsPerTask, loopsPerThread);
    }

    protected void runConcurrencyTest(int occurrenceThreads,
                                      int samplesThreads,
                                      int durationsThreads,
                                      int snapshotThreads,
                                      int findStatisticThreads,
                                      int loopsPerThread) {
        setupTestForAllSampleMethods(occurrenceThreads, samplesThreads, durationsThreads, snapshotThreads, findStatisticThreads, loopsPerThread);

        start();
        waitTillAllTasksFinish();

        Snapshot snapshot = subject.getSnapshot();
        assertThat(snapshot.getOccurrences()).containsOnlyKeys(OCCURRENCE_EVENT);
        assertThat(snapshot.getDurations()).containsOnlyKeys(DURATION_EVENT);
        assertThat(snapshot.getSamples()).containsOnlyKeys(SAMPLE_EVENT);

        assertSampleCountIsAsExpected(occurrenceThreads, samplesThreads, durationsThreads, loopsPerThread, snapshot);
        assertSampleCountIsSameAsPostedEventsByTasks(snapshot);
        assertNoTasksIndicateFailure();
    }

    private void assertNoTasksIndicateFailure() {
        for (ConcurrentTask task : concurrentTasks) {
            assertThat(task.isFailed()).as("one or more tasks indicate failure").isFalse();
        }
    }

    private void assertSampleCountIsAsExpected(int occurrenceThreads,
                                               int samplesThreads,
                                               int durationsThreads,
                                               int loopsPerThread,Snapshot snapshot) {
        assertThat(snapshot.getOccurrences().get(OCCURRENCE_EVENT)).as("expected occurrences").isEqualTo(occurrenceThreads * loopsPerThread);
        assertThat(snapshot.getSamples().get(SAMPLE_EVENT).getSampleCount()).as("expected samples").isEqualTo(samplesThreads * loopsPerThread);
        assertThat(snapshot.getDurations().get(DURATION_EVENT).getSampleCount()).as("expected durations").isEqualTo(durationsThreads * loopsPerThread);
    }

    private void assertSampleCountIsSameAsPostedEventsByTasks(Snapshot snapshot) {
        int occurrencesPosted = countEventsPostedByTasks(OCCURRENCE_EVENT);
        int samplesPosted = countEventsPostedByTasks(SAMPLE_EVENT);
        int durationsPosted = countEventsPostedByTasks(DURATION_EVENT);

        assertThat(snapshot.getOccurrences().get(OCCURRENCE_EVENT)).as("recorded occurrences").isEqualTo(occurrencesPosted);
        assertThat(snapshot.getSamples().get(SAMPLE_EVENT).getSampleCount()).as("recored samples").isEqualTo(samplesPosted);
        assertThat(snapshot.getDurations().get(DURATION_EVENT).getSampleCount()).as("recored durations").isEqualTo(durationsPosted);
    }
}

