package nl.toefel.java.code.measurements.api;

import java.util.Map;

/**
 * Contains a detached snapshot of the collected data. None of the fields can be null.
 */
public class Snapshot {

	private final long timestampTaken;
	/** name of duration => Statistical distribution of all recorded durations */
	private final Map<String, Statistic> durations;
	/** name of counter => times occurred */
	private final Map<String, Long> counters;
	/** name of samle => Statistical distribution of all sampled values*/
	private final Map<String, Statistic> samples;

	/**
	 * @param samples cannot be null
	 * @param counters cannot be null
	 * @param durations cannot be null
	 */
	public Snapshot(final Map<String, Statistic> samples, final Map<String, Long> counters, final Map<String, Statistic> durations) {
		timestampTaken = System.currentTimeMillis();
		this.samples = samples;
		this.counters = counters;
		this.durations = durations;
		if (this.samples == null) {
			throw new IllegalArgumentException("samples cannot be null");
		} else if (this.counters == null) {
			throw new IllegalArgumentException("counters cannot be null");
		} else if (this.durations == null) {
			throw new IllegalArgumentException("durations cannot be null");
		}
	}

	/**
	 * @return instant in time when this snapshot was taken (ms precision) comparable with {@link System#currentTimeMillis()}
	 */
	public long getTimestampTaken() {
		return timestampTaken;
	}

	/**
	 * @return all the recorded durations described as a statistical distribution by name (never null)
	 */
	public Map<String, Statistic> getDurations() {
		return durations;
	}

	/**
	 * @return all the collected counter values (timesOccurred) by their name (never null)
	 */
	public Map<String, Long> getCounters() {
		return counters;
	}

	/**
	 * @return all the collected statistics by name (never null)
	 */
	public Map<String, Statistic> getSamples() {
		return samples;
	}
}
