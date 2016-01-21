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

package nl.toefel.java.code.measurements.referenceimpl;

import nl.toefel.java.code.measurements.api.Statistic;

/**
 * Structure to hold the stats values in a distribution.
 */
public class StatisticRecord implements Statistic {

	private final String name;
	// TODO make all fields final
	private long sampleCount = 0;
	private long minimum = Long.MAX_VALUE;
	private long maximum = Long.MIN_VALUE;
	private double sampleAverage = 0.0d;
	private double sampleVariance = 0.0d;
	private double sampleStdDeviation = 0.0d;

	public static StatisticRecord createEmpty(String eventName) {
		return new StatisticRecord(eventName);
	}

	public static StatisticRecord createWithSingleSample(String eventName, long sample) {
		return createEmpty(eventName).addSample(sample);
	}

	public static StatisticRecord copyOf(StatisticRecord source) {
		StatisticRecord statisticRecordCopy = new StatisticRecord(source.name);
		statisticRecordCopy.sampleCount = source.sampleCount;
		statisticRecordCopy.minimum = source.minimum;
		statisticRecordCopy.maximum = source.maximum;
		statisticRecordCopy.sampleAverage = source.sampleAverage;
		statisticRecordCopy.sampleVariance = source.sampleVariance;
		statisticRecordCopy.sampleStdDeviation = source.sampleStdDeviation;
		return statisticRecordCopy;
	}

	/**
	 * Creates a new statistical record based on this record and the sample added to the distribution
	 *
	 * Average, sampleVariance and sampleStdDeviation calculations are taken from
	 * <a href="http://en.wikipedia.org/wiki/Standard_deviation">http://en.wikipedia.org/wiki/Standard_deviation</a>
	 *
	 * @param addedSample the sample to merge with this record
	 * @return new record
	 */
	public StatisticRecord addSample(long addedSample) {
		StatisticRecord previous = this; // for readability
		StatisticRecord updated = new StatisticRecord(name);
		updated.sampleCount = previous.sampleCount + 1;
		updated.minimum = addedSample < previous.minimum ? addedSample : previous.minimum;
		updated.maximum = addedSample > previous.maximum ? addedSample : previous.maximum;
		updated.sampleAverage = previous.sampleAverage + ((addedSample - previous.sampleAverage) / updated.sampleCount);
		updated.sampleVariance = previous.sampleVariance + ((addedSample - previous.sampleAverage) * (addedSample - updated.sampleAverage));
		updated.sampleStdDeviation = Math.sqrt(updated.sampleVariance / (updated.sampleCount - 1));
		return updated;
	}

	StatisticRecord(final String name) {
		if (name == null) {
			throw new IllegalArgumentException("name cannot be null");
		}
		this.name = name;
	}

	public String getName() {
		return name;
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

	public double getSampleAverage() {
		return sampleAverage;
	}

	@Override
	public double getSampleVariance() {
		return sampleVariance;
	}

	@Override
	public double getSampleStdDeviation() {
		return sampleStdDeviation;
	}

	@Override
	public int compareTo(final Statistic o) {
		if (o == null ) {
			return -1;
		} else {
			return name.compareTo(o.getName());
		}
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		StatisticRecord that = (StatisticRecord) o;

		return name.equals(that.name);

	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return "StatisticRecord [" +
				"name='" + name + '\'' +
				", sampleCount=" + sampleCount +
				", minimum=" + minimum +
				", maximum=" + maximum +
				", sampleAverage=" + sampleAverage +
				", sampleVariance=" + sampleVariance +
				", sampleStdDeviation=" + sampleStdDeviation +
				']';
	}
}
