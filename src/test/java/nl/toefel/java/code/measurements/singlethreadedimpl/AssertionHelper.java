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

package nl.toefel.java.code.measurements.singlethreadedimpl;

import nl.toefel.java.code.measurements.api.Snapshot;
import nl.toefel.java.code.measurements.api.StatisticalDistribution;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class AssertionHelper {

	public static void assertRecordHasExactParameters(final StatisticalDistribution record,
												final long samples,
												final long min,
												final long max,
												final double avg,
												final double variance,
												final double stddev) {
		assertThat(record).as("StatisticalDistribution should never be null").isNotNull();
		assertThat(record.getSampleCount()).as("sampleCount").isEqualTo(samples);
		assertThat(record.getMinimum()).as("minimum").isEqualTo(min);
		assertThat(record.getMaximum()).as("maximum").isEqualTo(max);
		assertThat(record.getAverage()).as("average").isEqualTo(avg);
		assertThat(record.getVariance()).as("variance").isEqualTo(variance);
		assertThat(record.getStdDeviation()).as("standardDeviation").isEqualTo(stddev);
	}

	public static void assertRecordHasParametersWithin(final StatisticalDistribution record,
														 final long samples,
														 final long min,
														 final long max,
														 final double avg,
														 final long offsetRange) {
		assertThat(record.getSampleCount()).as("sampleCount").isEqualTo(samples);
		assertThat(record.getMinimum()).as("minimum").isCloseTo(min, within(offsetRange));
		assertThat(record.getMaximum()).as("maximum").isCloseTo(max, within(offsetRange));
		assertThat(record.getAverage()).as("average").isCloseTo(avg, within((double)offsetRange));
	}

	public static Snapshot assertEmpty(Snapshot snapshot) {
		assertThat(snapshot.getOccurrences()).isNotNull().isEmpty();
		assertThat(snapshot.getDurations()).isNotNull().isEmpty();
		assertThat(snapshot.getSamples()).isNotNull().isEmpty();
		return snapshot;
	}

	public static Snapshot assertSize(Snapshot snapshot, int counterSize, int durationSize, int sampleSize) {
		assertThat(snapshot.getOccurrences()).isNotNull().as("counterSize").hasSize(counterSize);
		assertThat(snapshot.getDurations()).isNotNull().as("durationSize").hasSize(durationSize);
		assertThat(snapshot.getSamples()).isNotNull().as("sampleSize").hasSize(sampleSize);
		return snapshot;
	}
}
