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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nl.toefel.java.code.measurements.referenceimpl.StatisticRecord.copyOf;

public class StatisticalRecordStore implements Query {

	private Map<String, StatisticRecord> recordsByName = new HashMap<String, StatisticRecord>();

	public void addSample(String eventName, long value) {

	}

	@Override
	public Statistic findStatistic(final String eventName) {
		Statistic statistic = recordsByName.get(eventName);
		if (statistic == null) {
			return StatisticRecord.create(eventName);
		} else {
			return statistic;
		}
	}

	@Override
	public Map<String, Statistic> getSnapshot() {
		Map<String, Statistic> snapshot = new HashMap<String, Statistic>();
		for (String name: recordsByName.keySet()) {
			snapshot.put(name, copyOf(recordsByName.get(name)));
		}
		return snapshot;
	}

	@Override
	public Map<String, Statistic> getSnapshotAndReset() {
		Map<String, Statistic> snapshot = getSnapshot();
		reset();
		return snapshot;
	}

	@Override
	public void reset() {
		recordsByName = new HashMap<String, StatisticRecord>();
	}
}
