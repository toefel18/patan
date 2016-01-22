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