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

package nl.toefel.java.code.measurements.api;

/**
 * Immutable structure describing a distribution, making it threadsafe.
 * <p>
 * The name of the statistic is not stored in this object itself because it's always mapped by a name through the API
 * (users store by name, find by name and the {@link Snapshot} contains a map by name), storing the name therefore feels
 * redundant. Equals and hashcode are not overridden because default behaviour makes more sense in this case.
 */
public final class Statistic {

	private static final Statistic EMPTY_STATISTIC = new Statistic();

	private final long sampleCount;
	private final long minimum;
	private final long maximum;
	private final double sampleAverage;
	private final double sampleVariance;
	private final double sampleStdDeviation;

	/**
	 * @return an empty statistic;
	 */
	public static Statistic createEmpty() {
		return EMPTY_STATISTIC;
	}

	/**
	 * @param sampleValue
	 * @return a statistic with one sample
	 */
	public static Statistic createWithSingleSample(long sampleValue) {
		return EMPTY_STATISTIC.addSampleValue(sampleValue);
	}

	/**
	 * Creates a new statistical record based on this record and the sample added to the distribution
	 * <p>
	 * Average, sampleVariance and sampleStdDeviation calculations are taken from
	 * <a href="http://en.wikipedia.org/wiki/Standard_deviation">http://en.wikipedia.org/wiki/Standard_deviation</a>
	 *
	 * @param sampleValue the value to merge with this record
	 * @return new, immutable, record
	 */
	public Statistic addSampleValue(long sampleValue) {
		Statistic previous = this; // for readability
		long updatedSampleCount = previous.sampleCount + 1;
		long updatedMinimum = sampleValue < previous.minimum ? sampleValue : previous.minimum;
		long updatedMaximum = sampleValue > previous.maximum ? sampleValue : previous.maximum;
		double updatedSampleAverage = previous.sampleAverage + ((sampleValue - previous.sampleAverage) / updatedSampleCount);
		double updatedSampleVariance = previous.sampleVariance + ((sampleValue - previous.sampleAverage) * (sampleValue - updatedSampleAverage));
		double updatedSampleStdDeviation = Math.sqrt(updatedSampleVariance / (updatedSampleCount - 1));
		return new Statistic(updatedSampleCount, updatedMinimum, updatedMaximum, updatedSampleAverage, updatedSampleVariance, updatedSampleStdDeviation);
	}

	/**
	 * Private constructor to enforce immutability, use factory methods
	 */
	private Statistic() {
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
	private Statistic(final long sampleCount, final long minimum, final long maximum, final double sampleAverage, final double sampleVariance, final double sampleStdDeviation) {
		this.sampleCount = sampleCount;
		this.minimum = minimum;
		this.maximum = maximum;
		this.sampleAverage = sampleAverage;
		this.sampleVariance = sampleVariance;
		this.sampleStdDeviation = sampleStdDeviation;
	}

	/**
	 * @return false if distribution does not contain samples
	 */
	public boolean isEmpty() {
		return sampleCount <= 0;
	}

	/**
	 * @return the number of recorded samples in the distribution
	 */
	public long getSampleCount() {
		return sampleCount;
	}

	/**
	 * @return the lowest value in the distribution
	 */
	public long getMinimum() {
		return minimum;
	}

	/**
	 * @return the highest value in the distribution
	 */
	public long getMaximum() {
		return maximum;
	}

	/**
	 * @return the average of all samples in the distribution
	 */
	public double getSampleAverage() {
		return sampleAverage;
	}

	/**
	 * @return the variance of all samples in the distribution
	 */
	public double getSampleVariance() {
		return sampleVariance;
	}

	/**
	 * @return the standard deviation of all samples in the distribution
	 */
	public double getSampleStdDeviation() {
		return sampleStdDeviation;
	}

	public String toString() {
		return "Statistic [" +
				"sampleCount=" + sampleCount +
				", min=" + minimum +
				", max=" + maximum +
				", avg=" + sampleAverage +
				", variance=" + sampleVariance +
				", stddev=" + sampleStdDeviation +
				']';
	}
}
