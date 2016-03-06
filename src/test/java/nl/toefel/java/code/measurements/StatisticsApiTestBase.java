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

package nl.toefel.java.code.measurements;

import nl.toefel.java.code.measurements.api.TimedTask;
import nl.toefel.java.code.measurements.api.Snapshot;
import nl.toefel.java.code.measurements.api.StatisticalDistribution;
import nl.toefel.java.code.measurements.api.Statistics;
import nl.toefel.java.code.measurements.api.Stopwatch;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static nl.toefel.java.code.measurements.singlethreadedimpl.AssertionHelper.*;
import static nl.toefel.java.code.measurements.singlethreadedimpl.TimingHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.Assert.fail;
/**
 * Base class for tests that verify correctness of the API.
 */
public abstract class StatisticsApiTestBase {

	private Statistics stats;

	protected abstract Statistics createStatistics();

	@Before
	public void setUp() {
		stats = createStatistics();
	}

	@Test
	public void testFindStatisticsNotNull() {
		StatisticalDistribution empty = stats.findSampleDistribution("test.empty");
		assertThat(empty).isNotNull();
		assertThat(empty.isEmpty()).isTrue();
	}

	@Test
	public void testFindOccurrenceNotNull() {
		long emptyCounter = stats.findOccurrence("test.empty");
		assertThat(emptyCounter).isZero();
	}

	@Test
	public void testFindDurationNotNull() {
		StatisticalDistribution empty = stats.findDuration("test.empty");
		assertThat(empty).isNotNull();
		assertThat(empty.isEmpty()).isTrue();
	}

	@Test
	public void testStartStopwatch() {
		Stopwatch stopwatch = stats.startStopwatch();
		assertThat(stopwatch).isNotNull();
		assertThat(stopwatch.elapsedMillis()).isLessThan(100);
	}

	@Test
	public void testRecordElapsedTime() {
		Stopwatch stopwatch = stats.startStopwatch();

		expensiveMethodTakingMillis(100);
		long elapsed = stats.recordElapsedTime("test.duration", stopwatch);

		StatisticalDistribution record = stats.findDuration("test.duration");

		assertThat(elapsed)
				.isEqualTo(record.getMinimum())
				.isEqualTo(record.getMaximum());
		assertThat(record.getAverage()).isCloseTo(elapsed, within(0.001));
		assertRecordHasParametersWithin(record, 1, 100, 100, 100, 20);
		assertThat(record.getVariance()).as("variance").isCloseTo(0.0d, within(0.0d));
		assertThat(record.getStdDeviation()).as("standardDeviation").isEqualTo(Double.NaN);
	}

	@Test
	public void testRecordElapsedRunnable() {
		// = stats.recordElapsedTime("test.duration", () -> expensiveMethodTakingMillis(100));
		stats.recordElapsedTime("test.duration", new Runnable() {
			@Override
			public void run() {
				expensiveMethodTakingMillis(100);
			}
		});
		StatisticalDistribution record = stats.findDuration("test.duration.ok");
		assertThat(record.getMinimum()).isEqualTo(record.getMaximum());
		assertThat(record.getAverage()).isCloseTo(100, within(0.001));
		assertRecordHasParametersWithin(record, 1, 100, 100, 100, 20);
		assertThat(record.getVariance()).as("variance").isCloseTo(0.0d, within(0.0d));
		assertThat(record.getStdDeviation()).as("standardDeviation").isEqualTo(Double.NaN);
	}

	@Test
	public void testRecordElapsedRunnableException() {
		long elapsedGuess = System.currentTimeMillis();
		try {
				stats.recordElapsedTime("test.duration", new Runnable() {
				@Override
				public void run() {
					expensiveMethodTakingMillisException(100);
				}
			});
			fail("should raise exception");
		} catch (IllegalArgumentException e) {
			elapsedGuess = System.currentTimeMillis()-elapsedGuess;
			StatisticalDistribution record = stats.findDuration("test.duration.failed");
			assertRecordHasParametersWithin(record, 1, elapsedGuess, elapsedGuess, elapsedGuess, 40);
			assertThat(record.getVariance()).as("variance").isCloseTo(0.0d, within(0.0d));
			assertThat(record.getStdDeviation()).as("standardDeviation").isEqualTo(Double.NaN);
		}
	}


	@Test
	public void testRecordElapsedTimeTask() {
		String retValue;// = stats.recordElapsedTime("test.duration", () ->
						// expensiveMethodTakingMillis(100));
		long elapsedGuess = System.currentTimeMillis();
		retValue = stats.recordElapsedTime("test.duration", new TimedTask<String>() {
			@Override
			public String get() {
				return expensiveMethodTakingMillis(100);
			}
		});
		elapsedGuess = System.currentTimeMillis()-elapsedGuess;
		assertThat(retValue).isEqualTo("hi");
		StatisticalDistribution record = stats.findDuration("test.duration.ok");
		assertRecordHasParametersWithin(record, 1, elapsedGuess, elapsedGuess, elapsedGuess, 40);
		assertThat(record.getVariance()).as("variance").isCloseTo(0.0d, within(0.0d));
		assertThat(record.getStdDeviation()).as("standardDeviation").isEqualTo(Double.NaN);
	}

	@Test
	public void testRecordElapsedTimeTaskException() {
		long elapsedGuess = System.currentTimeMillis();
		try {
				stats.recordElapsedTime("test.duration", new TimedTask<String>() {
				@Override
				public String get() {
					return expensiveMethodTakingMillisException(100);
				}
			});
			fail("should raise exception");
		} catch (IllegalArgumentException e) {
			elapsedGuess = System.currentTimeMillis()-elapsedGuess;
			StatisticalDistribution record = stats.findDuration("test.duration.failed");
			assertRecordHasParametersWithin(record, 1, elapsedGuess, elapsedGuess, elapsedGuess, 40);
			assertThat(record.getVariance()).as("variance").isCloseTo(0.0d, within(0.0d));
			assertThat(record.getStdDeviation()).as("standardDeviation").isEqualTo(Double.NaN);
		}
	}

	@Test
	public void testRecordElapsedTimeMultiple() {
		Stopwatch stopwatch = stats.startStopwatch();
		expensiveMethodTakingMillis(80);
		stats.recordElapsedTime("test.duration", stopwatch);

		Stopwatch anotherStopwatch = stats.startStopwatch();
		expensiveMethodTakingMillis(150);
		stats.recordElapsedTime("test.duration", anotherStopwatch);

		StatisticalDistribution record = stats.findDuration("test.duration");
		assertRecordHasParametersWithin(record, 2, 80, 150, 115.0d, 20);
		assertThat(record.getVariance()).as("variance").isCloseTo(2450.0d, within(200.0d));
		assertThat(record.getStdDeviation()).as("standardDeviation").isCloseTo(50, within(5.0d));
	}

	@Test
	public void testAddOccurrence_sinleInvocation() {
		stats.addOccurrence("test.occurrence");
		assertThat(stats.findOccurrence("test.occurrence")).isEqualTo(1);
	}

	@Test
	public void testAddOccurrence_findStatistic() {
		stats.addOccurrence("test.occurrence");
		StatisticalDistribution counterStat = stats.findSampleDistribution("test.occurrence");
		assertThat(counterStat.isEmpty()).as("occurences are queried through findOccurence").isTrue();
	}

	@Test
	public void testAddOccurrence_findDuration() {
		stats.addOccurrence("test.occurrence");
		StatisticalDistribution counterStat = stats.findDuration("test.occurrence");
		assertThat(counterStat.isEmpty()).as("occurences are queried through findOccurence").isTrue();
	}

	@Test
	public void testAddOccurrence_multipleInvocations() {
		stats.addOccurrence("test.occurrence");
		stats.addOccurrence("test.occurrence");
		stats.addOccurrence("test.occurrence");

		assertThat(stats.findOccurrence("test.occurrence")).isEqualTo(3);
	}

	@Test
	public void testAddOccurrence_newInstancesEachInvocation() {
		stats.addOccurrence("test.occurrence");
		assertThat(stats.findOccurrence("test.occurrence")).isEqualTo(1);

		stats.addOccurrence("test.occurrence");
		assertThat(stats.findOccurrence("test.occurrence")).isEqualTo(2);
	}

	@Test
	public void testAddOccurrences_singleInvocation() {
		stats.addOccurrences("test.occurrences", 3);
		assertThat(stats.findOccurrence("test.occurrences")).isEqualTo(3);
	}

	@Test
	public void testAddOccurrences_multipleInvocations() {
		stats.addOccurrences("test.occurrences", 1);
		stats.addOccurrences("test.occurrences", 4);
		assertThat(stats.findOccurrence("test.occurrences")).isEqualTo(5);
	}

	@Test
	public void testAddSample() {
		stats.addSample("test.sample", 5);
		StatisticalDistribution stat = stats.findSampleDistribution("test.sample");
		assertRecordHasExactParameters(stat, 1, 5, 5, 5, 0, Double.NaN);
	}

	@Test
	public void testAddSamples_calculation() {
		stats.addSample("test.sample", 5);
		stats.addSample("test.sample", 10);
		stats.addSample("test.sample", 15);
		StatisticalDistribution stat = stats.findSampleDistribution("test.sample");

		assertThat(stat.getSampleCount()).as("sampleCount").isEqualTo(3);
		assertThat(stat.getMinimum()).as("minimum").isEqualTo(5);
		assertThat(stat.getMaximum()).as("maximum").isEqualTo(15);
		assertThat(stat.getAverage()).as("average").isCloseTo(10, within(0.01d));
		assertThat(stat.getVariance()).as("variance").isCloseTo(50.0, within(0.01d));
		assertThat(stat.getStdDeviation()).as("standardDeviation").isCloseTo(5, within(0.01d));
	}

	@Test
	public void testRecordElapsedTime_calculation() {
		final long ALLOWED_OFFSET_DRIFT = 15;

		Stopwatch stopwatch = stats.startStopwatch();

		stats.recordElapsedTime("test.test", stopwatch); // approx 0ms
		long millisStart = stopwatch.elapsedMillis();
		expensiveMethodTakingMillis(100);
		stats.recordElapsedTime("test.test", stopwatch); // approx 100ms
		expensiveMethodTakingMillis(100);
		long millisEnd = stopwatch.elapsedMillis();
		stats.recordElapsedTime("test.test", stopwatch); // approx 200ms;

		StatisticalDistribution duration = stats.findDuration("test.test");

		assertThat(duration.getSampleCount()).as("sampleCount").isEqualTo(3);
		assertThat(duration.getMinimum()).as("minimum").isCloseTo(millisStart, within(ALLOWED_OFFSET_DRIFT));
		assertThat(duration.getMaximum()).as("maximum").isCloseTo(millisEnd, within(ALLOWED_OFFSET_DRIFT));
		assertThat(duration.getAverage()).as("average").isCloseTo(100, within(50.0d));
		assertThat(duration.getVariance()).as("variance").isCloseTo(20200, within(1000.0));
		assertThat(duration.getStdDeviation()).as("standardDeviation").isCloseTo(100, within(5.0));
	}

	@Test
	public void testAddSampleAndCounterSameName() {
		stats.addSample("test.samename", 5);
		stats.addOccurrence("test.samename");
		stats.recordElapsedTime("test.test", stats.startStopwatch());

		assertRecordHasExactParameters(stats.findSampleDistribution("test.samename"), 1, 5, 5, 5, 0, Double.NaN);
		assertThat(stats.findOccurrence("test.samename")).isEqualTo(1);
		assertRecordHasParametersWithin(stats.findDuration("test.test"), 1, 0, 0, 0, 10);
	}

	@Test
	public void testGetSnapshotWithSameOccurrenceAsStat() {
		stats.addOccurrence("test.test");
		stats.addSample("test.test", 5);
		stats.recordElapsedTime("test.test", stats.startStopwatch());

		Snapshot snapshot = stats.getSnapshot();

		assertThat(snapshot.getOccurrences()).containsKeys("test.test");
		assertThat(snapshot.getSamples()).containsKeys("test.test");
		assertThat(snapshot.getDurations()).containsKeys("test.test");

		assertThat(snapshot.getOccurrences().get("test.test")).isEqualTo(1L);
		assertRecordHasExactParameters(snapshot.getSamples().get("test.test"), 1, 5, 5, 5, 0, Double.NaN);
		assertRecordHasParametersWithin(snapshot.getDurations().get("test.test"), 1, 0, 0, 0, 10);
	}

	@Test
	public void testGetSnapshotEmpty() {
		Snapshot snapshot = assertEmpty(stats.getSnapshot());
		assertThat(snapshot.getTimestampTaken()).isCloseTo(System.currentTimeMillis(), within(100L));
	}

	@Test
	public void testGetSnapshotAndReset() {
		addCounterDurationAndSample("test.test");
		assertSize(stats.getSnapshotAndReset(), 1, 1, 1);
		assertEmpty(stats.getSnapshot());
	}

	@Test
	public void testReset() {
		addCounterDurationAndSample("test.test");
		assertSize(stats.getSnapshotAndReset(), 1, 1, 1);
		stats.reset();
		assertEmpty(stats.getSnapshot());
	}

	private void addCounterDurationAndSample(final String name) {
		stats.addOccurrences(name, 1);
		stats.addSample(name, 5);
		stats.recordElapsedTime(name, stats.startStopwatch());
	}

	@Test
	public void testGetAllSamplesSnapshot() {
		stats.addSample("test.test", 100);
		Map<String, StatisticalDistribution> samples = stats.getAllSamplesSnapshot();
		assertThat(samples).isNotNull().hasSize(1);
		assertRecordHasExactParameters(samples.get("test.test"), 1, 100, 100, 100, 0, Double.NaN);
	}

	@Test
	public void testGetAllSamplesSnapshotAndReset() {
		stats.addSample("test.test", 100);
		Map<String, StatisticalDistribution> samples = stats.getAllSamplesSnapshotAndReset();
		assertThat(samples).isNotNull().hasSize(1);
		assertRecordHasExactParameters(samples.get("test.test"), 1, 100, 100, 100, 0, Double.NaN);
		assertThat(stats.getAllSamplesSnapshot()).isNotNull().isEmpty();
	}

	@Test
	public void testGetAllDurationsSnapshot() {
		stats.recordElapsedTime("test.test", stats.startStopwatch());
		Map<String, StatisticalDistribution> durations = stats.getAllDurationsSnapshot();
		assertThat(durations).isNotNull().hasSize(1);
		assertRecordHasParametersWithin(durations.get("test.test"), 1, 0, 0, 0, 100);
	}

	@Test
	public void testGetAllDurationsSnapshotAndReset() {
		stats.recordElapsedTime("test.test", stats.startStopwatch());
		Map<String, StatisticalDistribution> durations = stats.getAllDurationsSnapshotAndReset();
		assertThat(durations).isNotNull().hasSize(1);
		assertRecordHasParametersWithin(durations.get("test.test"), 1, 0, 0, 0, 100);
		assertThat(stats.getAllDurationsSnapshot()).isNotNull().isEmpty();
	}

	@Test
	public void testGetAllOccurrencesSnapshot() {
		stats.addOccurrence("test.test");
		Map<String, Long> occurrences = stats.getAllOccurrencesSnapshot();
		assertThat(occurrences).isNotNull().containsOnlyKeys("test.test").containsValues(1L);
	}

	@Test
	public void testGetAllOccurrencesSnapshotAndReset() {
		stats.addOccurrence("test.test");
		Map<String, Long> occurrences = stats.getAllOccurrencesSnapshotAndReset();
		assertThat(occurrences).isNotNull().containsOnlyKeys("test.test").containsValues(1L);
		assertThat(stats.getAllOccurrencesSnapshot()).isNotNull().isEmpty();
	}
}