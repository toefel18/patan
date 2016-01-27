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

import nl.toefel.java.code.measurements.api.Statistic;
import nl.toefel.java.code.measurements.api.Statistics;
import nl.toefel.java.code.measurements.api.Stopwatch;

import java.util.Map;
import java.util.SortedMap;

import static nl.toefel.java.code.measurements.referenceimpl.OccurrenceRecord.createCounterStatistic;

/**
 * Statistics implementation suitable for single-threaded applications.
 */
public class SingleThreadedStatisticsFacade implements Statistics {

	private final StatisticRecordStore statsStore = new StatisticRecordStore();

	private final StatisticRecordStore durationStore = new StatisticRecordStore();

	private final CounterStore counterStore = new CounterStore();

	@Override
	public Stopwatch startStopwatch() {
		return RunningStopwatch.startNewStopwatch();
	}

	@Override
	public void recordElapsedTime(final String eventName, final Stopwatch stopwatch) {
		durationStore.addSample(eventName, stopwatch.elapsedMillis());
	}

	@Override
	public Statistic findDuration(final String name) {
		return durationStore.findStatistic(name);
	}

	@Override
	public Map<String, Statistic> getAllDurationsSnapshot() {
		return null; // TODO IMPLEMENT
	}

	@Override
	public Map<String, Statistic> getAllDurationsSnapshotAndReset() {
		return null; // TODO IMPLEMENT
	}

	@Override
	public void addOccurrence(final String eventName) {
		counterStore.addOccurrence(eventName);
	}

	@Override
	public void addOccurrences(final String eventName, final int timesOccurred) {
		counterStore.addOccurrences(eventName, timesOccurred);
	}

	@Override
	public void addSample(final String eventName, final long value) {
		statsStore.addSample(eventName, value);
	}

	@Override
	public Statistic findStatistic(final String eventName) {
		return statsStore.findStatistic(eventName);
	}

	@Override
	public Map<String, Statistic> getAllSamplesSnapshot() {
		return null;
	}

	@Override
	public Map<String, Statistic> getAllSamplesSnapshotAndReset() {
		return null;
	}

	@Override
	public long findOccurrence(final String eventName) {
		return counterStore.findOccurrence(eventName);
	}

	@Override
	public Map<String, Long> getAllOccurrencesSnapshot() {
		return counterStore.getAllOccurrencesSnapshot();
	}

	@Override
	public Map<String, Long> getAllOccurrencesSnapshotAndReset() {
		return counterStore.getAllOccurrencesSnapshotAndReset();
	}

	@Override
	public SortedMap<String, Statistic> getSortedSnapshot() {
		SortedMap<String, Statistic> statistics = statsStore.getSortedSnapshot();
		Map<String, Long> counters = counterStore.getAllOccurrencesSnapshot();
		addCountersAsStatisticRecords(statistics, counters);
		return statistics;
	}

	@Override
	public SortedMap<String, Statistic> getSortedSnapshotAndReset() {
		try {
			return getSortedSnapshot();
		} finally {
			reset();
		}
	}

	@Override
	public void reset() {
		counterStore.reset();
		statsStore.reset();
	}

	private void addCountersAsStatisticRecords(final Map<String, Statistic> statistics, final Map<String, Long> counters) {
		for (Map.Entry<String, Long> counter: counters.entrySet()) {
			if (statistics.containsKey(counter.getKey())){
				String newName = createNonExistingName(statistics, counter.getKey());
				Statistic counterRecord = createCounterStatistic(newName, counter.getValue());
				statistics.put(newName, counterRecord);
			} else {
				statistics.put(counter.getKey(), createCounterStatistic(counter.getKey(), counter.getValue()));
			}
		}
	}

	/** Creates a new name appending a # sign to the original name*/
	private String createNonExistingName(final Map<String, Statistic> statistics, final String name) {
		if (statistics.containsKey(name)) {
			return createNonExistingName(statistics, "#" + name);
		} else {
			return name;
		}
	}
}
