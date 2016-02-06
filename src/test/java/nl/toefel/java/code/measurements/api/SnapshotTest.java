package nl.toefel.java.code.measurements.api;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;


public class SnapshotTest {

	private Map<String, Statistic> samples;
	private Map<String, Long> counters;
	private Map<String, Statistic> durations;

	@Before
	public void setupMaps() {
		samples = new TreeMap<String, Statistic>();
		counters = new TreeMap<String, Long>();
		durations = new TreeMap<String, Statistic>();
	}

	@Test (expected = IllegalArgumentException.class)
	public void testConstructorSamplesNull() {
		new Snapshot(null, counters, durations);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCountersNull() {
		new Snapshot(samples, null, durations);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testConstructorDurationsNull() {
		new Snapshot(samples, counters, null);
	}

	@Test
	public void testGetTimestampTaken() {
		assertThat(new Snapshot(samples, counters, durations).getTimestampTaken()).isCloseTo(System.currentTimeMillis(), within(100L));
	}

	@Test
	public void testGetSamples() {
		assertThat(new Snapshot(samples, counters, durations).getDurations()).isEqualTo(samples);
	}

	@Test
	public void testGetCounters() {
		assertThat(new Snapshot(samples, counters, durations).getDurations()).isEqualTo(counters);
	}

	@Test
	public void testGetDurations() {
		assertThat(new Snapshot(samples, counters, durations).getDurations()).isEqualTo(durations);
	}
}