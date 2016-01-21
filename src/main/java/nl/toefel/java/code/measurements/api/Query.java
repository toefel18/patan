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

import java.util.SortedMap;

/**
 * Methods for querying the statistics store
 */
public interface Query {

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
	 * Finds the current amount of times the event occurred. If the eventName has not been found,
	 * a empty statistic will be returned, use the {@link Statistic#isEmpty()} method to check if a statistic with values
	 * was returned.
	 *
	 * @param eventName the name of the event to lookup
	 * @return a copy of the internal statistic, never null
	 */
	Statistic findOccurrence(String eventName);

	/**
	 * @return a snapshot of all
	 */
	SortedMap<String, Statistic> getSortedSnapshot();

	/**
	 * Retrieves the current collected statistics and returns them. All the current statistics are cleared.
	 * @return the snapshot and clears the cur
	 */
	SortedMap<String, Statistic> getSortedSnapshotAndReset();

	/**
	 * Clears all currently collected statistics
	 */
	void reset();

}
