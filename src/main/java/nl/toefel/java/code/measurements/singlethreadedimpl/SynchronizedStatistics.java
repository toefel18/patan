/*
 *
 *     Copyright 2016 Christophe Hesters
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */

package nl.toefel.java.code.measurements.singlethreadedimpl;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import nl.toefel.java.code.measurements.api.Snapshot;
import nl.toefel.java.code.measurements.api.StatisticalDistribution;
import nl.toefel.java.code.measurements.api.Statistics;
import nl.toefel.java.code.measurements.api.Stopwatch;
import nl.toefel.java.code.measurements.api.TimedTask;

public class SynchronizedStatistics implements Statistics {
    private final Statistics statistics;

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public SynchronizedStatistics(final Statistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public Stopwatch startStopwatch() {
        // does not need locking because stopwatch creation does not read or write the store
        return statistics.startStopwatch();
    }

    @Override
    public <T> T recordElapsedTime(final String eventName, final TimedTask<T> runnable) {
        Stopwatch stopwatch = statistics.startStopwatch();
        try {
            rwLock.writeLock().lock();
            T val = runnable.get();
            statistics.recordElapsedTime(eventName, stopwatch);
            return val;
        } finally {
            rwLock.writeLock().unlock();
        }

    }

    @Override
    public <T> T recordElapsedTimeWithFailures(final String eventName, final TimedTask<T> runnable) {
        Stopwatch stopwatch = statistics.startStopwatch();
        try {
            rwLock.writeLock().lock();
            T val = runnable.get();
            statistics.recordElapsedTime(eventName, stopwatch);
            return val;
        } catch (RuntimeException e) {
            statistics.recordElapsedTime(eventName + ".failed", stopwatch);
            throw e;
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public long recordElapsedTime(final String eventName, final Stopwatch stopwatch) {
        try {
            rwLock.writeLock().lock();
            return statistics.recordElapsedTime(eventName, stopwatch);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public StatisticalDistribution findDuration(final String name) {
        try {
            rwLock.readLock().lock();
            return statistics.findDuration(name);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public Map<String, StatisticalDistribution> getAllDurationsSnapshot() {
        try {
            rwLock.readLock().lock();
            return statistics.getAllDurationsSnapshot();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public Map<String, StatisticalDistribution> getAllDurationsSnapshotAndReset() {
        try {
            rwLock.writeLock().lock();
            return statistics.getAllDurationsSnapshotAndReset();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public void addOccurrence(final String eventName) {
        try {
            rwLock.writeLock().lock();
            statistics.addOccurrence(eventName);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public void addOccurrences(final String eventName, final long timesOccurred) {
        try {
            rwLock.writeLock().lock();
            statistics.addOccurrences(eventName, timesOccurred);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public StatisticalDistribution findSampleDistribution(final String eventName) {
        try {
            rwLock.readLock().lock();
            return statistics.findSampleDistribution(eventName);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public Map<String, StatisticalDistribution> getAllSamplesSnapshot() {
        try {
            rwLock.readLock().lock();
            return statistics.getAllSamplesSnapshot();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public Map<String, StatisticalDistribution> getAllSamplesSnapshotAndReset() {
        try {
            rwLock.writeLock().lock();
            return statistics.getAllSamplesSnapshotAndReset();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public long findOccurrence(final String eventName) {
        try {
            rwLock.readLock().lock();
            return statistics.findOccurrence(eventName);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public Map<String, Long> getAllOccurrencesSnapshot() {
        try {
            rwLock.readLock().lock();
            return statistics.getAllOccurrencesSnapshot();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public Map<String, Long> getAllOccurrencesSnapshotAndReset() {
        try {
            rwLock.writeLock().lock();
            return statistics.getAllOccurrencesSnapshotAndReset();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public void reset() {
        try {
            rwLock.writeLock().lock();
            statistics.reset();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public void addSample(final String eventName, final long value) {
        try {
            rwLock.writeLock().lock();
            statistics.addSample(eventName, value);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public Snapshot getSnapshot() {
        try {
            rwLock.readLock().lock();
            return statistics.getSnapshot();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    @Override
    public Snapshot getSnapshotAndReset() {
        try {
            rwLock.writeLock().lock();
            return statistics.getSnapshotAndReset();
        } finally {
            rwLock.writeLock().unlock();
        }
    }

}
