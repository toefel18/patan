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

public class StatisticRecordStore implements SampleStore {

	private Map<String, StatisticalDistribution> recordsByName = new ConcurrentHashMap<String, StatisticalDistribution>();

	@Override
	public void addSample(String eventName, long value) {
		if (recordsByName.containsKey(eventName)) {
			recordsByName.put(eventName, recordsByName.get(eventName).newWithExtraSample(value));
		} else {
			recordsByName.put(eventName, ImmutableStatisticalDistribution.createWithSingleSample(value));
		}
	}

	@Override
	public StatisticalDistribution findStatistic(final String eventName) {
		StatisticalDistribution statisticalDistribution = recordsByName.get(eventName);
		if (statisticalDistribution == null) {
			return ImmutableStatisticalDistribution.createEmpty();
		} else {
			return statisticalDistribution;
		}
	}

	@Override
	public SortedMap<String, StatisticalDistribution> getAllSamplesSnapshot() {
		SortedMap<String, StatisticalDistribution> snapshot = new TreeMap<String, StatisticalDistribution>();
		for (String name: recordsByName.keySet()) {
			snapshot.put(name, recordsByName.get(name));
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
		recordsByName = new HashMap<String, StatisticalDistribution>();
	}
}
