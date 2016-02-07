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

package nl.toefel.java.code.measurements.singlethreadedimpl;

import nl.toefel.java.code.measurements.api.StatisticalDistribution;

/**
 * Immutable structure describing a statistical distribution, making it threadsafe.
 * <p>
 * Equals and hashcode are not overridden because default behaviour makes more sense in this case.
 */
public final class ImmutableStatisticalDistribution implements StatisticalDistribution {

	private static final StatisticalDistribution EMPTY_STATISTICAL_DISTRIBUTION = new ImmutableStatisticalDistribution();

	private final long sampleCount;
	private final long minimum;
	private final long maximum;
	private final double sampleAverage;
	private final double sampleVariance;
	private final double sampleStdDeviation;

	/**
	 * @return an empty statistic;
	 */
	public static StatisticalDistribution createEmpty() {
		return EMPTY_STATISTICAL_DISTRIBUTION;
	}

	/**
	 * @param sampleValue the value to initialize the distribution with
	 * @return a statistic with one sample
	 */
	public static StatisticalDistribution createWithSingleSample(long sampleValue) {
		return EMPTY_STATISTICAL_DISTRIBUTION.newWithExtraSample(sampleValue);
	}

	/**
	 * Creates a new statistical distribution object based on this distribution and the extra
	 * value that is being added.
	 * <p>
	 * Average, sampleVariance and sampleStdDeviation calculations are taken from
	 * <a href="http://en.wikipedia.org/wiki/Standard_deviation">http://en.wikipedia.org/wiki/Standard_deviation</a>
	 *
	 * @param sampleValue the value to merge with this record
	 * @return new, immutable, distribution
	 */
	@Override
	public StatisticalDistribution newWithExtraSample(long sampleValue) {
		ImmutableStatisticalDistribution previous = this; // for readability
		long updatedSampleCount = previous.sampleCount + 1;
		long updatedMinimum = sampleValue < previous.minimum ? sampleValue : previous.minimum;
		long updatedMaximum = sampleValue > previous.maximum ? sampleValue : previous.maximum;
		double updatedSampleAverage = previous.sampleAverage + ((sampleValue - previous.sampleAverage) / updatedSampleCount);
		double updatedSampleVariance = previous.sampleVariance + ((sampleValue - previous.sampleAverage) * (sampleValue - updatedSampleAverage));
		double updatedSampleStdDeviation = Math.sqrt(updatedSampleVariance / (updatedSampleCount - 1));
		return new ImmutableStatisticalDistribution(updatedSampleCount, updatedMinimum, updatedMaximum, updatedSampleAverage, updatedSampleVariance, updatedSampleStdDeviation);
	}

	/**
	 * Private constructor to enforce immutability, use factory methods
	 */
	private ImmutableStatisticalDistribution() {
		sampleCount = 0;
		minimum = Long.MAX_VALUE;
		maximum = Long.MIN_VALUE;
		sampleAverage = 0.0d;
		sampleVariance = 0.0d;
		sampleStdDeviation = 0.0d;
	}

	/**
	 * Private constructor to enforce immutability, use factory methods
	 */
	private ImmutableStatisticalDistribution(final long sampleCount, final long minimum, final long maximum, final double sampleAverage, final double sampleVariance, final double sampleStdDeviation) {
		this.sampleCount = sampleCount;
		this.minimum = minimum;
		this.maximum = maximum;
		this.sampleAverage = sampleAverage;
		this.sampleVariance = sampleVariance;
		this.sampleStdDeviation = sampleStdDeviation;
	}


	@Override
	public boolean isEmpty() {
		return sampleCount <= 0;
	}


	@Override
	public long getSampleCount() {
		return sampleCount;
	}


	@Override
	public long getMinimum() {
		return minimum;
	}


	@Override
	public long getMaximum() {
		return maximum;
	}


	@Override
	public double getAverage() {
		return sampleAverage;
	}


	@Override
	public double getVariance() {
		return sampleVariance;
	}

	@Override
	public double getStdDeviation() {
		return sampleStdDeviation;
	}

	public String toString() {
		return "StatisticalDistribution [" +
				"sampleCount=" + sampleCount +
				", min=" + minimum +
				", max=" + maximum +
				", avg=" + sampleAverage +
				", variance=" + sampleVariance +
				", stddev=" + sampleStdDeviation +
				']';
	}
}
