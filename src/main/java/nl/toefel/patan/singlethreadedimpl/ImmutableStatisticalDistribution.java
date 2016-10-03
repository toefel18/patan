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

package nl.toefel.patan.singlethreadedimpl;

import nl.toefel.patan.api.StatisticalDistribution;

/**
 * Immutable structure describing a statistical distribution, making it threadsafe.
 * <p>
 * Equals and hashcode are not overridden because default behaviour makes more sense in this case.
 */
public final class ImmutableStatisticalDistribution implements StatisticalDistribution {

	private final long sampleCount;
	private final double minimum;
	private final double maximum;
	private final double sum;
	private final double shift;
	private final double shiftedSum;
	private final double shiftedSumSqr;

	/**
	 * @return an empty statistic;
	 */
	public static StatisticalDistribution createEmpty() {
		return new ImmutableStatisticalDistribution();

	}

	/**
	 * @param sampleValue the value to initialize the distribution with
	 * @return a statistic with one sample
	 */
	public static StatisticalDistribution createWithSingleSample(long sampleValue) {
		return new ImmutableStatisticalDistribution().newWithExtraSample(sampleValue);
	}

	/**
	 * Creates a new statistical distribution object based on this distribution and the extra
	 * value that is being added.
	 * <p>
	 * Mean and stdDeviation calculations are taken from
	 * <a href="https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance#Computing_shifted_data">omputing shifted data</a>
	 *
	 * @param sampleValue the value to merge with this record
	 * @return new, immutable, distribution
	 */
	@Override
	public StatisticalDistribution newWithExtraSample(double sampleValue) {
		ImmutableStatisticalDistribution previous = this; // for readability
		double shift = previous.isEmpty() ? sampleValue : previous.shift;
		long updatedCount = previous.sampleCount + 1;
		double updatedMinimum = sampleValue < previous.minimum ? sampleValue : previous.minimum;
		double updatedMaximum = sampleValue > previous.maximum ? sampleValue : previous.maximum;
		double updatedSum = previous.sum + sampleValue;
		double updatedShiftedSum = shiftedSum + sampleValue - shift;
		double updatedShiftedSumSqr = shiftedSumSqr + (sampleValue - shift) * (sampleValue - shift);
		ImmutableStatisticalDistribution newDist = new ImmutableStatisticalDistribution(updatedCount, updatedMinimum, updatedMaximum, updatedSum, shift, updatedShiftedSum, updatedShiftedSumSqr);
		return newDist;
	}

	/**
	 * Private constructor to enforce immutability, use factory methods
	 */
	private ImmutableStatisticalDistribution() {
		this(0, Double.MAX_VALUE, Double.MIN_VALUE, 0, 0, 0, 0);
	}

	/**
	 * Private constructor to enforce immutability, use factory methods
	 */
	private ImmutableStatisticalDistribution(final long sampleCount, final double minimum, final double maximum, final double sum,
											 final double shift, final double shiftedSum, final double shiftedSumSqr) {
		this.sampleCount = sampleCount;
		this.minimum = minimum;
		this.maximum = maximum;
		this.sum = sum;
		this.shift = shift;
		this.shiftedSum = shiftedSum;
		this.shiftedSumSqr = shiftedSumSqr;
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
	public double getMinimum() {
		return minimum;
	}


	@Override
	public double getMaximum() {
		return maximum;
	}

	@Override
	public double getMean() {
		return sum / sampleCount;
	}

	@Override
	public double getAverage() {
		return getMean();
	}

	@Override
	public double getStdDeviation() {
		return Math.sqrt((shiftedSumSqr - shiftedSum * shiftedSum / sampleCount)/(sampleCount - 1));
	}

	@Override
	public String toString() {
		return "StatisticalDistribution [" +
				"sampleCount=" + sampleCount +
				", min=" + minimum +
				", max=" + maximum +
				", mean=" + getMean() +
				", stddev=" + getStdDeviation() +
				']';
	}
}
