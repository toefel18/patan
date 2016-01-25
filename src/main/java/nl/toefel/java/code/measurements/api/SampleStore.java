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
 * Methods for adding samples of value to the store. The values can then be summarized in a statistics record.
 */
public interface SampleStore {

	/**
	 * Adds a sample for the variable with the name 'eventName'.
	 *
	 * @param eventName the name of the sample
	 * @param value current value to record into the distribution
	 */
	void addSample(String eventName, long value);

	/**
	 * Finds the current statistical value for the event with the given eventName. If the eventName has not been found,
	 * a empty statistic will be returned, use the {@link Statistic#isEmpty()} method to check if a statistic with values
	 * was returned.
	 *
	 * @param eventName the name of the event to lookup
	 * @return a copy of the internal statistic, never null
	 */
	Statistic findStatistic(String eventName);

	/**
	 * Clears all currently collected statistics
	 */
	void reset();
}
