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

package nl.toefel.patan.singlethreadedimpl;

import nl.toefel.patan.api.SampleStore;
import nl.toefel.patan.api.StatisticalDistribution;

import java.util.SortedMap;
import java.util.TreeMap;

public class StatisticDistributionStore implements SampleStore {

	private SortedMap<String, StatisticalDistribution> distributionsByName = new TreeMap<String, StatisticalDistribution>();

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
		return new TreeMap<String, StatisticalDistribution>(distributionsByName);
	}

	@Override
	public SortedMap<String, StatisticalDistribution> getAllSamplesSnapshotAndReset() {
		SortedMap<String, StatisticalDistribution> snapshot = distributionsByName; // we can
		reset();
		return snapshot;
	}


	@Override
	public void reset() {
		distributionsByName = new TreeMap<String, StatisticalDistribution>();
	}
}
