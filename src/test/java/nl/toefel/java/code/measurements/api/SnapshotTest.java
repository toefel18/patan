package nl.toefel.java.code.measurements.api;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;


public class SnapshotTest {

	private Map<String, Statistic> samples;
	private Map<String, Long> occurrences;
	private Map<String, Statistic> durations;

	@Before
	public void setupMaps() {
		samples = new TreeMap<String, Statistic>();
		samples.put("sample", Statistic.createEmpty());
		occurrences = new TreeMap<String, Long>();
		occurrences.put("counter", 2L);
		durations = new TreeMap<String, Statistic>();
		durations.put("duration", Statistic.createEmpty());
	}

	@Test (expected = IllegalArgumentException.class)
	public void testConstructorSamplesNull() {
		new Snapshot(null, occurrences, durations);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCountersNull() {
		new Snapshot(samples, null, durations);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testConstructorDurationsNull() {
		new Snapshot(samples, occurrences, null);
	}

	@Test
	public void testGetTimestampTaken() {
		assertThat(new Snapshot(samples, occurrences, durations).getTimestampTaken()).isCloseTo(System.currentTimeMillis(), within(100L));
	}

	@Test
	public void testGetSamples() {
		assertThat(new Snapshot(samples, occurrences, durations).getSamples()).isEqualTo(samples);
	}

	@Test
	public void testGetOccurrences() {
		assertThat(new Snapshot(samples, occurrences, durations).getOccurrences()).isEqualTo(occurrences);
	}

	@Test
	public void testGetDurations() {
		assertThat(new Snapshot(samples, occurrences, durations).getDurations()).isEqualTo(durations);
	}
}