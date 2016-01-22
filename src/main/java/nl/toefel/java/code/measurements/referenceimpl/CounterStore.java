package nl.toefel.java.code.measurements.referenceimpl;

import nl.toefel.java.code.measurements.api.OccurrenceStore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Holds counters in a more efficient manner than using locking.
 */
public class CounterStore implements OccurrenceStore {

	private static final Long ZERO = 0L;

	private Map<String, AtomicLong> counters = new ConcurrentHashMap<String, AtomicLong>();

	@Override
	public void addOccurrence(final String eventName) {
		AtomicLong counter = counters.get(eventName);
		if (counter == null) {
			// TODO not thread-safe rewrite
			counters.put(eventName, new AtomicLong(1));
		} else {
			counter.incrementAndGet();
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
		AtomicLong atomicLong = counters.get(eventName);
		if (atomicLong == null) {
			return ZERO;
		} else {
			return atomicLong.get();
		}
	}

	public Map<String, Long> getSnapshot() {
		Map<String, Long> countersSnapshot = new HashMap<String, Long>();
		for (Map.Entry<String, AtomicLong> entry : counters.entrySet()) {
			countersSnapshot.put(entry.getKey(), entry.getValue().longValue());
		}
		return countersSnapshot;
	}

}
