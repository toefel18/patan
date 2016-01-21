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

package nl.toefel.java.code.measurements.referenceimpl;

import nl.toefel.java.code.measurements.api.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static nl.toefel.java.code.measurements.referenceimpl.StatisticRecord.copyOf;

public class StatisticRecordStore  {

	private Map<String, StatisticRecord> recordsByName = new ConcurrentHashMap<String, StatisticRecord>();

	public void addSample(String eventName, long value) {
		// not threadsafe
		if (recordsByName.containsKey(eventName)) {
			recordsByName.put(eventName, recordsByName.get(eventName).addSample(value));
		} else {
			recordsByName.put(eventName, StatisticRecord.createWithSingleSample(eventName, value));
		}
	}

	public Statistic findStatistic(final String eventName) {
		Statistic statistic = recordsByName.get(eventName);
		if (statistic == null) {
			return StatisticRecord.createEmpty(eventName);
		} else {
			return statistic;
		}
	}

	public SortedMap<String, Statistic> getSortedSnapshot() {
		SortedMap<String, Statistic> snapshot = new TreeMap<String, Statistic>();
		for (String name: recordsByName.keySet()) {
			snapshot.put(name, copyOf(recordsByName.get(name)));
		}
		return snapshot;
	}

	public SortedMap<String, Statistic> getSortedSnapshotAndReset() {
		try {
			return getSortedSnapshot();
		} finally {
			reset();
		}
	}

	public void reset() {
		recordsByName = new HashMap<String, StatisticRecord>();
	}
}
