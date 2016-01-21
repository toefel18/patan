package nl.toefel.java.code.measurements.referenceimpl;

import nl.toefel.java.code.measurements.api.Statistic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * @author hestersco
 */
public class AssertionHelper {

	public static void assertRecordHasExactParameters(final Statistic record,
												final String name,
												final long samples,
												final long min,
												final long max,
												final double avg,
												final double variance,
												final double stddev) {
		assertThat(record.getName()).as("name").isEqualTo(name);
		assertThat(record.getSampleCount()).as("sampleCount").isEqualTo(samples);
		assertThat(record.getMinimum()).as("minimum").isEqualTo(min);
		assertThat(record.getMaximum()).as("maximum").isEqualTo(max);
		assertThat(record.getSampleAverage()).as("average").isEqualTo(avg);
		assertThat(record.getSampleVariance()).as("variance").isEqualTo(variance);
		assertThat(record.getSampleStdDeviation()).as("standardDeviation").isEqualTo(stddev);
	}

	public static void assertRecordHasParametersWithin100(final Statistic record,
														  final String name,
														  final long samples,
														  final long min,
														  final long max,
														  final double avg,
														  final double variance,
														  final double stddev) {
		assertThat(record.getName()).as("name").isEqualTo(name);
		assertThat(record.getSampleCount()).as("sampleCount").isEqualTo(samples);
		assertThat(record.getMinimum()).as("minimum").isCloseTo(min, within(50L));
		assertThat(record.getMaximum()).as("maximum").isCloseTo(max, within(50L));
		assertThat(record.getSampleAverage()).as("average").isCloseTo(avg, within(50d));
		assertThat(record.getSampleVariance()).as("variance").isCloseTo(variance, within(50d));
		assertThat(record.getSampleStdDeviation()).as("standardDeviation").isCloseTo(stddev, within(50d));
	}
}
