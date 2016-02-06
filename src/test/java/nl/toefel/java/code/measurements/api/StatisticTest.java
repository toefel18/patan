package nl.toefel.java.code.measurements.api;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class StatisticTest {

	@Test
	public void testEqualsHashcodeSameName() {
		Statistic stat1 = Statistic.createWithSingleSample(1);
		Statistic stat2 = Statistic.createWithSingleSample(4);
		assertThat(stat1).isNotEqualTo(stat2);
		assertThat(stat1.hashCode()).isNotEqualTo(stat2);
	}

	@Test
	public void testgetEmptyName() {
		assertThat(Statistic.createEmpty()).isNotNull();
	}

	@Test
	public void testEqualsNull() {
		Statistic stat = Statistic.createWithSingleSample(1);
		assertThat(stat.equals(null)).isFalse();
	}

	@Test
	public void testEqualsHashcodeOtherName() {
		Statistic stat1 = Statistic.createWithSingleSample(1);
		Statistic stat2 = Statistic.createWithSingleSample(4);
		assertThat(stat1).isNotEqualTo(stat2);
		assertThat(stat1.hashCode()).isNotEqualTo(stat2);
	}

	@Test
	public void testToString() {
		Statistic a = Statistic.createWithSingleSample(1);
		assertThat(a.toString())
				.isNotNull()
				.contains(s(a.getSampleCount()),
						s(a.getMinimum()),
						s(a.getMaximum()),
						s(a.getSampleAverage()),
						s(a.getSampleVariance()),
						s(a.getSampleStdDeviation()));
	}

	public String s(Number nr) {
		return String.valueOf(nr);
	}

}