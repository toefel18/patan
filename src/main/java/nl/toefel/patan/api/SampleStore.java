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

package nl.toefel.patan.api;

import java.util.Map;

/**
 * Methods for adding samples of value to the store. The values can then be summarized in a statistics record.
 */
public interface SampleStore extends Resettable {

	/**
	 * Adds a sample to the statistical distribution identified by the given name. The statistical distribution is created
	 * if it does not exist yet for the given name.
	 *
	 * @param name the name of the sample
	 * @param value current value to record into the distribution
	 */
	void addSample(String name, double value);

	/**
	 * Finds the statistical distribution by name. If the name has not been found, a empty statistic will be returned,
	 * use the {@link StatisticalDistribution#isEmpty()} method to check for emptiness.
	 *
	 * @param name the name of the event to lookup
	 * @return a copy of the internal statistic, never null
	 */
	StatisticalDistribution findSampleDistribution(String name);

	/**
	 * @return the snapshot of all stored samples. The snapshot can be modified and is detached from the implementation.
	 */
	Map<String, StatisticalDistribution> getAllSamplesSnapshot();

	/**
	 * Retrieves the current collected statistics and returns them. All the current statistics are cleared.
	 *
	 * @return a snapshot of all samples. The snapshot can be modified and is detached from the implementation.
	 */
	Map<String, StatisticalDistribution> getAllSamplesSnapshotAndReset();
}
