package nl.toefel.java.code.measurements.referenceimpl;

import nl.toefel.java.code.measurements.api.Statistic;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class StatisticRecordTest extends StatisticTestBase {

	@Test
	public void testEqualsHashcodeSameName() {
		StatisticRecord stat1 = StatisticRecord.createWithSingleSample("test.occurence", 1);
		StatisticRecord stat2 = StatisticRecord.createWithSingleSample("test.occurence", 4);
		testEqualsHashcodeSameName(stat1, stat2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullName() {
		StatisticRecord.createEmpty(null);
	}

	@Test
	public void testEqualsNull() {
		StatisticRecord stat = StatisticRecord.createWithSingleSample("test.occurence", 1);
		testEqualsNull(stat);
	}

	@Test
	public void testEqualsHashcodeOtherName() {
		StatisticRecord stat1 = StatisticRecord.createWithSingleSample("test.occurence", 1);
		StatisticRecord stat2 = StatisticRecord.createWithSingleSample("test2.stat", 4);
		testEqualsHashcodeOtherName(stat1, stat2);
	}

	@Test
	public void testComparable() {
		StatisticRecord a = StatisticRecord.createWithSingleSample("test.a", 1);
		StatisticRecord b = StatisticRecord.createWithSingleSample("test.b", 4);
		testComparable(a, b);
	}

	@Test
	public void testComparableAgainstNull() {
		testComparableAgainstNull(StatisticRecord.createWithSingleSample("test.a", 1));
	}

	@Test
	public void testToString() {
		StatisticRecord a = StatisticRecord.createWithSingleSample("test.a", 1);
		assertThat(a.toString())
				.isNotNull()
				.contains(a.getName(),
						s(a.getSampleCount()),
						s(a.getMinimum()),
						s(a.getMaximum()),
						s(a.getSampleAverage()),
						s(a.getSampleVariance()),
						s(a.getSampleStdDeviation()));
	}

}