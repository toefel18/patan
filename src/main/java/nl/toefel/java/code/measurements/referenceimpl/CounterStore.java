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

package nl.toefel.java.code.measurements.referenceimpl;

import nl.toefel.java.code.measurements.api.OccurrenceStore;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds counters in a more efficient manner than using locking.
 */
public class CounterStore implements OccurrenceStore {

	private static final Long ZERO = 0L;

	private Map<String, Long> counters = new HashMap<String, Long>();

	@Override
	public void addOccurrence(final String eventName) {
		Long counter = counters.get(eventName);
		if (counter == null) {
			counters.put(eventName, 1L);
		} else {
			counters.put(eventName, counter + 1);
		}
	}

	@Override
	public void addOccurrences(final String eventName, final int timesOccurred) {
		for (int i = 0; i < timesOccurred; i++) {
			addOccurrence(eventName);
		}
	}

	public void reset() {
		counters.clear();
	}

	public Long findCounter(String eventName) {
		Long counter = counters.get(eventName);
		if (counter == null) {
			return ZERO;
		} else {
			return counter;
		}
	}

	public Map<String, Long> getSnapshot() {
		return new HashMap<String, Long>(counters); //String and Long are immutable
	}
}
