package nl.toefel.patan.singlethreadedimpl;

import nl.toefel.patan.api.StatisticalDistribution;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ImmutableStatisticalDistributionTest {

	@Test
	public void testEqualsHashcodeSameName() {
		StatisticalDistribution stat1 = ImmutableStatisticalDistribution.createWithSingleSample(1);
		StatisticalDistribution stat2 = ImmutableStatisticalDistribution.createWithSingleSample(4);
		assertThat(stat1).isNotEqualTo(stat2);
		assertThat(stat1.hashCode()).isNotEqualTo(stat2);
	}

	@Test
	public void testgetEmptyName() {
		assertThat(ImmutableStatisticalDistribution.createEmpty()).isNotNull();
	}

	@Test
	public void testEqualsNull() {
		StatisticalDistribution stat = ImmutableStatisticalDistribution.createWithSingleSample(1);
		assertThat(stat.equals(null)).isFalse();
	}

	@Test
	public void testEqualsHashcodeOtherName() {
		StatisticalDistribution stat1 = ImmutableStatisticalDistribution.createWithSingleSample(1);
		StatisticalDistribution stat2 = ImmutableStatisticalDistribution.createWithSingleSample(4);
		assertThat(stat1).isNotEqualTo(stat2);
		assertThat(stat1.hashCode()).isNotEqualTo(stat2);
	}

	@Test
	public void testToString() {
		StatisticalDistribution a = ImmutableStatisticalDistribution.createWithSingleSample(1);
		assertThat(a.toString())
				.isNotNull()
				.contains(s(a.getSampleCount()),
						s(a.getMinimum()),
						s(a.getMaximum()),
						s(a.getAverage()),
						s(a.getStdDeviation()));
	}

	public String s(Number nr) {
		return String.valueOf(nr);
	}

	@Test
	public void qqqq() {
		StatisticalDistribution dist = ImmutableStatisticalDistribution.createWithSingleSample(1);
		for (int i = 1; i < 10; i++) {
			dist.newWithExtraSample(i);
		}
		//qqqq assert min, max, avg, count, stdDev
	}
}