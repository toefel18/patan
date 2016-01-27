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
 * This is the interface that implementations should implement.
 */
public interface Statistics extends DurationStore, OccurrenceStore, SampleStore {

	/**
	 * @return a snapshot of all statistics, including occurrences, durations and samples sorted by key
	 */
	SortedMap<String, Statistic> getSortedSnapshot();

	/**
	 * Retrieves the current collected statistics and returns them. All the current statistics are cleared.
	 *
	 * @return a snapshot of all statistics, including occurrences, durations and samples sorted by key
	 */
	SortedMap<String, Statistic> getSortedSnapshotAndReset();

}
