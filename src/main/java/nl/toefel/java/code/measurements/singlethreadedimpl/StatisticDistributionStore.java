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

package nl.toefel.java.code.measurements.singlethreadedimpl;

import nl.toefel.java.code.measurements.api.SampleStore;
import nl.toefel.java.code.measurements.api.StatisticalDistribution;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class StatisticDistributionStore implements SampleStore {

	private Map<String, StatisticalDistribution> distributionsByName = new ConcurrentHashMap<String, StatisticalDistribution>();

	@Override
	public void addSample(String name, long value) {
		if (distributionsByName.containsKey(name)) {
			distributionsByName.put(name, distributionsByName.get(name).newWithExtraSample(value));
		} else {
			distributionsByName.put(name, ImmutableStatisticalDistribution.createWithSingleSample(value));
		}
	}

	@Override
	public StatisticalDistribution findSampleDistribution(final String name) {
		StatisticalDistribution statisticalDistribution = distributionsByName.get(name);
		if (statisticalDistribution == null) {
			return ImmutableStatisticalDistribution.createEmpty();
		} else {
			return statisticalDistribution;
		}
	}

	@Override
	public SortedMap<String, StatisticalDistribution> getAllSamplesSnapshot() {
		SortedMap<String, StatisticalDistribution> snapshot = new TreeMap<String, StatisticalDistribution>();
		for (String name: distributionsByName.keySet()) {
			snapshot.put(name, distributionsByName.get(name));
		}
		return snapshot;
	}

	@Override
	public SortedMap<String, StatisticalDistribution> getAllSamplesSnapshotAndReset() {
		SortedMap<String, StatisticalDistribution> snapshot = getAllSamplesSnapshot();
		reset();
		return snapshot;
	}


	@Override
	public void reset() {
		distributionsByName = new HashMap<String, StatisticalDistribution>();
	}
}
