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
import nl.toefel.java.code.measurements.api.Statistic;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class StatisticRecordStore implements SampleStore {

	private Map<String, Statistic> recordsByName = new ConcurrentHashMap<String, Statistic>();

	@Override
	public void addSample(String eventName, long value) {
		if (recordsByName.containsKey(eventName)) {
			recordsByName.put(eventName, recordsByName.get(eventName).addSampleValue(value));
		} else {
			recordsByName.put(eventName, Statistic.createWithSingleSample(value));
		}
	}

	@Override
	public Statistic findStatistic(final String eventName) {
		Statistic statistic = recordsByName.get(eventName);
		if (statistic == null) {
			return Statistic.createEmpty();
		} else {
			return statistic;
		}
	}

	@Override
	public SortedMap<String, Statistic> getAllSamplesSnapshot() {
		SortedMap<String, Statistic> snapshot = new TreeMap<String, Statistic>();
		for (String name: recordsByName.keySet()) {
			snapshot.put(name, recordsByName.get(name));
		}
		return snapshot;
	}

	@Override
	public SortedMap<String, Statistic> getAllSamplesSnapshotAndReset() {
		SortedMap<String, Statistic> snapshot = getAllSamplesSnapshot();
		reset();
		return snapshot;
	}


	@Override
	public void reset() {
		recordsByName = new HashMap<String, Statistic>();
	}
}
