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

import java.util.Map;

/**
 * Methods for measuring elapsed time during program execution.
 */
public interface DurationStore extends Resettable{

	/**
	 * Starts a new stop watch. The elapsed time can be recorded later on by calling {@link #recordElapsedTime(String, Stopwatch)}.
	 * The stopwatch is not stored within the store, clients must keep track of the stopwatch.
	 *
	 * @return a new instance, directly started when this method is invoked.
	 */
	Stopwatch startStopwatch();

	/**
	 * Records the elapsed time in nano-seconds.
	 *
	 * @param eventName eventName to store the elapsed time under
	 * @param stopwatch the {@link Stopwatch} that measures the elapsed time
	 */
	void recordElapsedTime(String eventName, Stopwatch stopwatch);

	/**
	 * Finds the current statistical value for the recored duration with the given name. If the eventName has not been found,
	 * a empty statistic will be returned, use the {@link Statistic#isEmpty()} method to check if a statistic with values
	 * was returned.
	 *
	 * @param name the name of the event to lookup
	 * @return a copy of the internal statistic, never null
	 */
	Statistic findDuration(String name);


	/**
	 * Returns a snapshot of all the recorded durations
	 *
	 * @return a copy of the internal state that is detached from the implementation
	 */
	Map<String, Statistic> getAllDurationsSnapshot();

	/**
	 * Returns a snapshot of all the recorded durations and clears the internal state
	 *
	 * @return a copy of the internal state that is detached from the implementation
	 */
	Map<String, Statistic> getAllDurationsSnapshotAndReset();
}
