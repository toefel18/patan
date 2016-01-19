/*
 *    Copyright 2016 Christophe Hesters
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package nl.toefel.java.code.measurements.referenceimpl;

import nl.toefel.java.code.measurements.api.Stopwatch;
import org.junit.Before;
import org.junit.Test;

import static nl.toefel.java.code.measurements.referenceimpl.TimingHelper.sleep;
import static org.assertj.core.api.Assertions.assertThat;


public class StatisticsTest {

	private Statistics stats;

	@Before
	public void setUp() {
		stats = new Statistics();
	}

	@Test
	public void testStartStopwatch() {
		Stopwatch stopwatch = stats.startStopwatch();
		assertThat(stopwatch).isNotNull();
		assertThat(stopwatch.elapsedMillis()).isLessThan(50);
	}

	@Test
	public void testRecordElapsedTime() {
		Stopwatch stopwatch = stats.startStopwatch();
		sleep(100);
		stats.recordElapsedTime("test.duration", stopwatch);
	}

	@Test
	public void testAddOccurrence() {
		stats.addOccurrence("test.occurrence");
	}

	@Test
	public void testAddOccurrences() {

	}

	@Test
	public void testAddSample() {

	}

	@Test
	public void testFindStatistic() {

	}

	@Test
	public void testGetSnapshot() {

	}

	@Test
	public void testGetSnapshotAndReset() {

	}

	@Test
	public void testReset() {

	}

}