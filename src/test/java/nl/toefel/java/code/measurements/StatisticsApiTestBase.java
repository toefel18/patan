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

import nl.toefel.java.code.measurements.api.Statistic;
import nl.toefel.java.code.measurements.api.Statistics;
import nl.toefel.java.code.measurements.api.Stopwatch;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static nl.toefel.java.code.measurements.referenceimpl.AssertionHelper.assertRecordHasExactParameters;
import static nl.toefel.java.code.measurements.referenceimpl.AssertionHelper.assertRecordHasParametersWithin;
import static nl.toefel.java.code.measurements.referenceimpl.TimingHelper.expensiveMethodTakingMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;


public abstract class StatisticsApiTestBase {

	private Statistics stats;

	protected abstract Statistics createStatistics();

	@Before
	public void setUp() {
		stats = createStatistics();
	}

	@Test
	public void testFindStatisticsNotNull() {
		Statistic empty = stats.findStatistic("test.empty");
		assertThat(empty).isNotNull();
		assertThat(empty.isEmpty()).isTrue();
	}

	@Test
	public void testFindOccurrenceNotNull() {
		Statistic empty = stats.findOccurrence("test.empty");
		assertThat(empty).isNotNull();
		assertThat(empty.isEmpty()).isTrue();
		assertThat(empty.getSampleCount()).isZero();
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
		stats.recordElapsedTime("test.duration", stopwatch);

		Statistic record = stats.findStatistic("test.duration");

		assertRecordHasParametersWithin(record, "test.duration", 1, 100, 100, 100, 20);
		assertThat(record.getSampleVariance()).as("variance").isCloseTo(0.0d, within(0.0d));
		assertThat(record.getSampleStdDeviation()).as("standardDeviation").isEqualTo(Double.NaN);
	}

	@Test
	public void testRecordElapsedTimeMultiple() {
		Stopwatch stopwatch = stats.startStopwatch();
		expensiveMethodTakingMillis(80);
		stats.recordElapsedTime("test.duration", stopwatch);

		Stopwatch anotherStopwatch = stats.startStopwatch();
		expensiveMethodTakingMillis(150);
		stats.recordElapsedTime("test.duration", anotherStopwatch);

		Statistic record = stats.findStatistic("test.duration");
		assertRecordHasParametersWithin(record, "test.duration", 2, 80, 150, 115.0d, 20);
		assertThat(record.getSampleVariance()).as("variance").isCloseTo(2450.0d, within(200.0d));
		assertThat(record.getSampleStdDeviation()).as("standardDeviation").isCloseTo(50, within(5.0d));
	}

	@Test
	public void testAddOccurrence_sinleInvocation() {
		stats.addOccurrence("test.occurrence");
		Statistic counterStat = stats.findOccurrence("test.occurrence");
		assertRecordHasExactParameters(counterStat, "test.occurrence", 1, 0, 0, 0, 0, 0);
	}

	@Test
	public void testAddOccurrence_findStatistic() {
		stats.addOccurrence("test.occurrence");
		Statistic counterStat = stats.findStatistic("test.occurrence");
		assertThat(counterStat.isEmpty()).as("occurences are queried through findOccurence").isTrue();
	}

	@Test
	public void testAddOccurrence_multipleInvocations() {
		stats.addOccurrence("test.occurrence");
		stats.addOccurrence("test.occurrence");
		stats.addOccurrence("test.occurrence");

		Statistic counterStat = stats.findOccurrence("test.occurrence");
		assertRecordHasExactParameters(counterStat, "test.occurrence", 3, 0, 0, 0, 0, 0);
	}

	@Test
	public void testAddOccurrence_newInstancesEachInvocation() {
		stats.addOccurrence("test.occurrence");
		Statistic counterStatisticFirst = stats.findOccurrence("test.occurrence");
		assertRecordHasExactParameters(counterStatisticFirst, "test.occurrence", 1, 0, 0, 0, 0, 0);

		stats.addOccurrence("test.occurrence");
		Statistic counterStatSecond = stats.findOccurrence("test.occurrence");
		assertThat(counterStatisticFirst).isNotSameAs(counterStatSecond);
		assertRecordHasExactParameters(counterStatisticFirst, "test.occurrence", 1, 0, 0, 0, 0, 0);
		assertRecordHasExactParameters(counterStatSecond, "test.occurrence", 2, 0, 0, 0, 0, 0);
	}

	@Test
	public void testAddOccurrences_singleInvocation() {
		stats.addOccurrences("test.occurrences", 3);
		Statistic counterStatistic = stats.findOccurrence("test.occurrences");
		assertRecordHasExactParameters(counterStatistic, "test.occurrences", 3, 0, 0, 0, 0, 0);
	}

	@Test
	public void testAddOccurrences_multipleInvocations() {
		stats.addOccurrences("test.occurrences", 1);
		stats.addOccurrences("test.occurrences", 4);
		Statistic counterStatistic = stats.findOccurrence("test.occurrences");
		assertRecordHasExactParameters(counterStatistic, "test.occurrences", 5, 0, 0, 0, 0, 0);
	}

	@Test
	public void testAddSample() {
		stats.addSample("test.sample", 5);
		Statistic stat = stats.findStatistic("test.sample");
		assertRecordHasExactParameters(stat, "test.sample", 1, 5, 5, 5, 0, Double.NaN);
	}

	@Test
	public void testAddSamples_calculation() {
		stats.addSample("test.sample", 5);
		stats.addSample("test.sample", 10);
		stats.addSample("test.sample", 15);
		Statistic stat = stats.findStatistic("test.sample");

		assertThat(stat.getName()).as("name").isEqualTo("test.sample");
		assertThat(stat.getSampleCount()).as("sampleCount").isEqualTo(3);
		assertThat(stat.getMinimum()).as("minimum").isEqualTo(5);
		assertThat(stat.getMaximum()).as("maximum").isEqualTo(15);
		assertThat(stat.getSampleAverage()).as("average").isCloseTo(10, within(0.01d));
		assertThat(stat.getSampleVariance()).as("variance").isCloseTo(50.0, within(0.01d));
		assertThat(stat.getSampleStdDeviation()).as("standardDeviation").isCloseTo(5, within(0.01d));
	}

	@Test
	public void testAddSampleAndCounterSameName() {
		stats.addSample("test.samename", 5);
		stats.addOccurrence("test.samename");

		Statistic stat = stats.findStatistic("test.samename");
		assertRecordHasExactParameters(stat, "test.samename", 1, 5, 5, 5, 0, Double.NaN);

		Statistic counter = stats.findOccurrence("test.samename");
		assertRecordHasExactParameters(counter, "test.samename", 1, 0, 0, 0, 0, 0);
	}

	@Test
	public void testGetSortedSnapshot() {
		stats.addOccurrences("test.occurrences", 1);
		stats.addSample("test.sample", 5);

		Map<String, Statistic> snapshot = stats.getSortedSnapshot();

		assertThat(snapshot).containsKeys("test.occurrences", "test.sample");
		assertRecordHasExactParameters(snapshot.get("test.occurrences"), "test.occurrences", 1, 0, 0, 0, 0, 0);
		assertRecordHasExactParameters(snapshot.get("test.sample"), "test.sample", 1, 5, 5, 5, 0, Double.NaN);
	}

	@Test
	public void testGetSortedSnapshotWithSameOccurrenceAsStat() {
		stats.addOccurrence("test.test");
		stats.addSample("test.test", 5);

		Map<String, Statistic> snapshot = stats.getSortedSnapshot();

		assertThat(snapshot).containsKeys("test.test", "#test.test");
		assertRecordHasExactParameters(snapshot.get("#test.test"), "#test.test", 1, 0, 0, 0, 0, 0);
		assertRecordHasExactParameters(snapshot.get("test.test"), "test.test", 1, 5, 5, 5, 0, Double.NaN);
	}

	@Test
	public void testGetSortedSnapshotEmpty() {
		assertThat(stats.getSortedSnapshot()).isNotNull().isEmpty();
	}

	@Test
	public void testGetSortedSnapshotAndReset() {
		stats.addOccurrences("test.occurrences", 1);
		stats.addSample("test.sample", 5);
		assertThat(stats.getSortedSnapshotAndReset()).hasSize(2);
		assertThat(stats.getSortedSnapshot()).isEmpty();
	}

	@Test
	public void testReset() {
		stats.addOccurrences("test.occurrences", 1);
		stats.addSample("test.sample", 5);
		assertThat(stats.getSortedSnapshot()).hasSize(2);

		stats.reset();

		assertThat(stats.getSortedSnapshot()).isEmpty();
	}
}