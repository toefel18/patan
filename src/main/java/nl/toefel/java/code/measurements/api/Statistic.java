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
 * Represents the calculated statistics for the sample with the name {@link #getName()}. The statistics should be
 * interpreted as statistics of a sample, not the full population.
 */
public interface Statistic extends Comparable<Statistic> {

	/**
	 * @return the event name
	 */
	String getName();

	/**
	 * @return true if there were any samples actually recorded, false indicates that the statistical methods will return defaults.
	 */
	boolean isEmpty();

	/**
	 * @return the number of times this event has occurred.
	 */
	long getSampleCount();

	/**
	 * @return the minimum value
	 */
	long getMinimum();

	/**
	 * @return the maximum value
	 */
	long getMaximum();

	/**
	 * @return average of all samples
	 */
	double getSampleAverage();

	/**
	 * @return sample variance
	 */
	double getSampleVariance();

	/**
	 * @return sample standard deviation.
	 */
	double getSampleStdDeviation();

}
