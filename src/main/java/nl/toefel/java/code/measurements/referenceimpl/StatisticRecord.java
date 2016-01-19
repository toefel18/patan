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
 * Structure to hold the stats values
 */
public class StatisticRecord implements Statistic {

	private final String name;
	private long sampleCount = 0;
	private long minimum = Long.MAX_VALUE;
	private long maximum = Long.MIN_VALUE;
	private double average = 0.0d;
	private double sampleVariance = 0.0d;
	private double sampleStdDeviation = 0.0d;

	public static StatisticRecord create(String eventName) {
		return new StatisticRecord(eventName);
	}

	public static StatisticRecord copyOf(StatisticRecord source) {
		StatisticRecord statisticRecordCopy = new StatisticRecord(source.name);
		statisticRecordCopy.sampleCount = source.sampleCount;
		statisticRecordCopy.minimum = source.minimum;
		statisticRecordCopy.maximum = source.maximum;
		statisticRecordCopy.average= source.average;
		statisticRecordCopy.sampleVariance = source.sampleVariance;
		statisticRecordCopy.sampleStdDeviation = source.sampleStdDeviation;
		return statisticRecordCopy;
	}

	private StatisticRecord(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean isValid() {
		return sampleCount > 0;
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
		return average;
	}

	@Override
	public double getVariance() {
		return sampleVariance;
	}

	@Override
	public double getStdDev() {
		return sampleStdDeviation;
	}

	void setSampleCount(final long sampleCount) {
		this.sampleCount = sampleCount;
	}

	void setMinimum(final long minimum) {
		this.minimum = minimum;
	}

	void setMaximum(final long maximum) {
		this.maximum = maximum;
	}

	void setAverage(final double average) {
		this.average = average;
	}

	void setSampleVariance(final double sampleVariance) {
		this.sampleVariance = sampleVariance;
	}

	void setSampleStdDeviation(final double sampleStdDeviation) {
		this.sampleStdDeviation = sampleStdDeviation;
	}

	@Override
	public int compareTo(final Statistic o) {
		if (o == null ) {
			return -1;
		} else {
			return name.compareTo(o.getName());
		}
	}
}
