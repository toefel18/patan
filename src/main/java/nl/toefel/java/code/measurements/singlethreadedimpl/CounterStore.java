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

import nl.toefel.java.code.measurements.api.OccurrenceStore;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Holds counters in a more efficient manner than using locking.
 */
public class CounterStore implements OccurrenceStore {

	private static final long ZERO = 0L;

	private Map<String, Long> counters = new HashMap<String, Long>();

	@Override
	public void addOccurrence(final String name) {
		counters.put(name, findOccurrence(name) + 1);
	}

	@Override
	public void addOccurrences(final String name, final long timesOccurred) {
		counters.put(name, findOccurrence(name) + timesOccurred);
	}

	@Override
	public long findOccurrence(final String name) {
		Long counter = counters.get(name);
		if (counter == null) {
			return ZERO;
		} else {
			return counter;
		}
	}

	@Override
	public SortedMap<String, Long> getAllOccurrencesSnapshot() {
		return new TreeMap<String, Long>(counters); //String and Long are immutable
	}

	@Override
	public SortedMap<String, Long> getAllOccurrencesSnapshotAndReset() {
		SortedMap<String, Long> snapshot = getAllOccurrencesSnapshot();
		reset();
		return snapshot;
	}

	public void reset() {
		counters.clear();
	}
}
