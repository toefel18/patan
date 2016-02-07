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
		samples.put("sample", ImmutableStatisticalDistribution.createEmpty().newWithExtraSample(1));
		occurrences = new TreeMap<String, Long>();
		occurrences.put("counter", 2L);
		durations = new TreeMap<String, StatisticalDistribution>();
		durations.put("duration", ImmutableStatisticalDistribution.createEmpty().newWithExtraSample(1));
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

	@Test
	public void testFindOccurrence() {
		assertThat(new DetachedSnapshot(samples, occurrences, durations).findOccurrence("counter")).isEqualTo(2);
	}

	@Test
	public void testFindOccurrenceNotExisting() {
		assertThat(new DetachedSnapshot(samples, occurrences, durations).findOccurrence("nonexisting")).isEqualTo(0);
	}

	@Test
	public void testFindDuration() {
		assertThat(new DetachedSnapshot(samples, occurrences, durations).findDuration("duration").getSampleCount()).isEqualTo(1);
	}

	@Test
	public void testFindDurationNotExisting() {
		assertThat(new DetachedSnapshot(samples, occurrences, durations).findDuration("nonexisting").isEmpty()).isTrue();
	}

	@Test
	public void testFindSampleDistribution() {
		assertThat(new DetachedSnapshot(samples, occurrences, durations).findSampleDistribution("sample").getSampleCount()).isEqualTo(1);
	}

	@Test
	public void testFindSampleDistributionNotExisting() {
		assertThat(new DetachedSnapshot(samples, occurrences, durations).findSampleDistribution("nonexisting").isEmpty()).isTrue();
	}
}