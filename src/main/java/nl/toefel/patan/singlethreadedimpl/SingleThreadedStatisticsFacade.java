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

import nl.toefel.patan.api.*;

import java.util.Map;

/**
 * Statistics implementation suitable for single-threaded applications.
 */
public class SingleThreadedStatisticsFacade implements Statistics {

	private final StatisticDistributionStore sampleStore = new StatisticDistributionStore();

	private final StatisticDistributionStore durationStore = new StatisticDistributionStore();

	private final CounterStore counterStore = new CounterStore();

	@Override
	public Stopwatch startStopwatch() {
		return ForeverRunningStopwatch.startNewStopwatch();
	}

	@Override
	public void recordElapsedTime(final String eventName, final Runnable runnable) {
		Stopwatch stopwatch = startStopwatch();
		try {
			runnable.run();
			recordElapsedTime(eventName + ".ok", stopwatch);
		} catch (RuntimeException e) {
			recordElapsedTime(eventName + ".failed", stopwatch);
			throw e;
		}
	}

	@Override
	public <T> T recordElapsedNanos(String eventName, TimedTask<T> runnable) {
		Stopwatch stopwatch = startStopwatch();
		try {
			T val = runnable.get();
			recordElapsedNanos(eventName + ".ok", stopwatch);
			return val;
		} catch (RuntimeException e) {
			recordElapsedNanos(eventName + ".failed", stopwatch);
			throw e;
		}
	}

	@Override
	public void recordElapsedNanos(String eventName, Runnable runnable) {
		Stopwatch stopwatch = startStopwatch();
		try {
			runnable.run();
			recordElapsedNanos(eventName + ".ok", stopwatch);
		} catch (RuntimeException e) {
			recordElapsedNanos(eventName + ".failed", stopwatch);
			throw e;
		}
	}

	@Override
	public <T> T recordElapsedTime(final String eventName, final TimedTask<T> runnable) {
		Stopwatch stopwatch = startStopwatch();
		try {
			T val = runnable.get();
			recordElapsedTime(eventName + ".ok", stopwatch);
			return val;
		} catch (RuntimeException e) {
			recordElapsedTime(eventName + ".failed", stopwatch);
			throw e;
		}
	}

	@Override
	public double recordElapsedTime(final String eventName, final Stopwatch stopwatch) {
		double elapsedMillis = stopwatch.elapsedMillis();
		durationStore.addSample(eventName, elapsedMillis);
		return elapsedMillis;
	}

	@Override
	public long recordElapsedNanos(String eventName, Stopwatch stopwatch) {
		long elapsedNanos = stopwatch.elapsedNanos();
		durationStore.addSample(eventName, elapsedNanos);
		return elapsedNanos;
	}

	@Override
	public StatisticalDistribution findDuration(final String name) {
		return durationStore.findSampleDistribution(name);
	}

	@Override
	public Map<String, StatisticalDistribution> getAllDurationsSnapshot() {
		return durationStore.getAllSamplesSnapshot();
	}

	@Override
	public Map<String, StatisticalDistribution> getAllDurationsSnapshotAndReset() {
		return durationStore.getAllSamplesSnapshotAndReset();
	}

	@Override
	public void addOccurrence(final String eventName) {
		counterStore.addOccurrence(eventName);
	}

	@Override
	public void addOccurrences(final String eventName, final long timesOccurred) {
		counterStore.addOccurrences(eventName, timesOccurred);
	}

	@Override
	public void addSample(final String eventName, final double value) {
		sampleStore.addSample(eventName, value);
	}

	@Override
	public StatisticalDistribution findSampleDistribution(final String eventName) {
		return sampleStore.findSampleDistribution(eventName);
	}

	@Override
	public Map<String, StatisticalDistribution> getAllSamplesSnapshot() {
		return sampleStore.getAllSamplesSnapshot();
	}

	@Override
	public Map<String, StatisticalDistribution> getAllSamplesSnapshotAndReset() {
		return sampleStore.getAllSamplesSnapshotAndReset();
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
	public void reset() {
		counterStore.reset();
		durationStore.reset();
		sampleStore.reset();
	}

	@Override
	public Snapshot getSnapshot() {
		return new DetachedSnapshot(getAllSamplesSnapshot(), getAllOccurrencesSnapshot(), getAllDurationsSnapshot());
	}

	@Override
	public Snapshot getSnapshotAndReset() {
		return new DetachedSnapshot(getAllSamplesSnapshotAndReset(), getAllOccurrencesSnapshotAndReset(), getAllDurationsSnapshotAndReset());
	}

}
