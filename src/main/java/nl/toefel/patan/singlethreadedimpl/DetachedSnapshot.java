package nl.toefel.patan.singlethreadedimpl;

import nl.toefel.patan.api.Snapshot;
import nl.toefel.patan.api.StatisticalDistribution;

import java.util.Map;
import java.util.Properties;

import static nl.toefel.patan.singlethreadedimpl.MavenPropertiesLoader.ARTIFACT_ID;
import static nl.toefel.patan.singlethreadedimpl.MavenPropertiesLoader.VERSION;

/**
 * Contains a detached snapshot of the collected data. None of the fields can be null.
 */
public class DetachedSnapshot implements Snapshot {

	private final long timestampTaken;
	/**
	 * name of duration => Statistical distribution of all recorded durations
	 */
	private final Map<String, StatisticalDistribution> durations;
	/**
	 * name of counter => times occurred
	 */
	private final Map<String, Long> occurrences;
	/**
	 * name of sample => Statistical distribution of all sampled values
	 */
	private final Map<String, StatisticalDistribution> samples;


	/**
	 * @param samples cannot be null
	 * @param occurrences cannot be null
	 * @param durations cannot be null
	 */
	public DetachedSnapshot(final Map<String, StatisticalDistribution> samples, final Map<String, Long> occurrences, final Map<String, StatisticalDistribution> durations) {
		this.timestampTaken = System.currentTimeMillis();
		this.samples = samples;
		this.occurrences = occurrences;
		this.durations = durations;
		if (this.samples == null) {
			throw new IllegalArgumentException("samples cannot be null");
		} else if (this.occurrences == null) {
			throw new IllegalArgumentException("occurrences cannot be null");
		} else if (this.durations == null) {
			throw new IllegalArgumentException("durations cannot be null");
		}
	}

	@Override
	public long getTimestampTaken() {
		return timestampTaken;
	}

	@Override
	public Map<String, StatisticalDistribution> getDurations() {
		return durations;
	}

	@Override
	public Map<String, Long> getOccurrences() {
		return occurrences;
	}

	@Override
	public Map<String, StatisticalDistribution> getSamples() {
		return samples;
	}

	@Override
	public long findOccurrence(final String name) {
		Long occurrenceCounter = occurrences.get(name);
		return occurrenceCounter == null ? 0 : occurrenceCounter;
	}

	@Override
	public StatisticalDistribution findDuration(final String name) {
		return getOrEmpty(durations.get(name));
	}

	@Override
	public StatisticalDistribution findSampleDistribution(final String name) {
		return getOrEmpty(samples.get(name));

	}

	private StatisticalDistribution getOrEmpty(StatisticalDistribution distribution) {
		return distribution == null ? ImmutableStatisticalDistribution.createEmpty() : distribution;
	}

	@Override
	public String getVersion() {
		try {
			Properties props = MavenPropertiesLoader.load();
			return props.getProperty(ARTIFACT_ID) + "-" + props.getProperty(VERSION);
		} catch (Exception ex) {
			return ex.toString(); // patan doesnt log nor should throw an exception if no version can be found, so output the exception to give some clue on why it failed
		}
	}
}
