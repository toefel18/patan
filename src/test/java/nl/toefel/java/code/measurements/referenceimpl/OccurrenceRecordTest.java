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

package nl.toefel.java.code.measurements.referenceimpl;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OccurrenceRecordTest extends StatisticTestBase {

	@Test(expected = IllegalArgumentException.class)
	public void testNullName() {
		OccurrenceRecord.createCounterStatistic(null, 0);
	}

	@Test
	public void testEqualsHashcodeSameName() {
		OccurrenceRecord occurrence1 = OccurrenceRecord.createCounterStatistic("test.occurence", 1);
		OccurrenceRecord occurrence2 = OccurrenceRecord.createCounterStatistic("test.occurence", 4);
		testEqualsHashcodeSameName(occurrence1, occurrence2);
	}

	@Test
	public void testEqualsNull() {
		testEqualsNull(OccurrenceRecord.createCounterStatistic("test.occurence", 1));
	}

	@Test
	public void testEqualsHashcodeOtherName() {
		OccurrenceRecord occurrence1 = OccurrenceRecord.createCounterStatistic("test.occurence", 1);
		OccurrenceRecord occurrence2 = OccurrenceRecord.createCounterStatistic("test2.stat", 4);
		testEqualsHashcodeOtherName(occurrence1, occurrence2);
	}

	@Test
	public void testComparable() {
		OccurrenceRecord a = OccurrenceRecord.createCounterStatistic("test.a", 1);
		OccurrenceRecord b = OccurrenceRecord.createCounterStatistic("test.b", 4);
		testComparable(a, b);
	}

	@Test
	public void testComparableAgainstNull() {
		testComparableAgainstNull(OccurrenceRecord.createCounterStatistic("test.a", 4));
	}

	@Test
	public void testToString() {
		OccurrenceRecord r = OccurrenceRecord.createCounterStatistic("test.a", 4);
		assertThat(r.toString()).isNotNull().contains("test.a", s(4));
	}
}