package nl.toefel.java.code.measurements.concurrencytest;

import nl.toefel.java.code.measurements.api.StatisticalDistribution;
import nl.toefel.java.code.measurements.singlethreadedimpl.DetachedSnapshot;
import nl.toefel.java.code.measurements.singlethreadedimpl.ImmutableStatisticalDistribution;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;


public class DetachedSnapshotTest {

	private Map<String, StatisticalDistribution> samples;
	private Map<String, Long> occurrences;
	private Map<String, StatisticalDistribution> durations;

	@Before
	public void setupMaps() {
		samples = new TreeMap<String, StatisticalDistribution>();
		samples.put("sample", ImmutableStatisticalDistribution.createEmpty());
		occurrences = new TreeMap<String, Long>();
		occurrences.put("counter", 2L);
		durations = new TreeMap<String, StatisticalDistribution>();
		durations.put("duration", ImmutableStatisticalDistribution.createEmpty());
	}

	@Test (expected = IllegalArgumentException.class)
	public void testConstructorSamplesNull() {
		new DetachedSnapshot(null, occurrences, durations);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCountersNull() {
		new DetachedSnapshot(samples, null, durations);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testConstructorDurationsNull() {
		new DetachedSnapshot(samples, occurrences, null);
	}

	@Test
	public void testGetTimestampTaken() {
		assertThat(new DetachedSnapshot(samples, occurrences, durations).getTimestampTaken()).isCloseTo(System.currentTimeMillis(), within(100L));
	}

	@Test
	public void testGetSamples() {
		assertThat(new DetachedSnapshot(samples, occurrences, durations).getSamples()).isEqualTo(samples);
	}

	@Test
	public void testGetOccurrences() {
		assertThat(new DetachedSnapshot(samples, occurrences, durations).getOccurrences()).isEqualTo(occurrences);
	}

	@Test
	public void testGetDurations() {
		assertThat(new DetachedSnapshot(samples, occurrences, durations).getDurations()).isEqualTo(durations);
	}
}