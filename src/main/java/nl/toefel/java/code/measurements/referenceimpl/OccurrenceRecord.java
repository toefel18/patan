package nl.toefel.java.code.measurements.referenceimpl;

import nl.toefel.java.code.measurements.api.Statistic;

/**
 * Record that is only concerned with occurrences and not a value distribution.
 */
public class OccurrenceRecord implements Statistic {

	private final String name;
	private final long count;

	public static OccurrenceRecord createCounterStatistic(String name, long count) {
		return new OccurrenceRecord(name, count);
	}

	OccurrenceRecord(final String name, long count) {
		if (name == null) {
			throw new IllegalArgumentException("name cannot be null");
		}
		this.name = name;
		this.count = count;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isEmpty() {
		return count == 0;
	}

	@Override
	public long getSampleCount() {
		return count;
	}

	@Override
	public long getMinimum() {
		return 0;
	}

	@Override
	public long getMaximum() {
		return 0;
	}

	@Override
	public double getSampleAverage() {
		return 0;
	}

	@Override
	public double getSampleVariance() {
		return 0;
	}

	@Override
	public double getSampleStdDeviation() {
		return 0;
	}

	@Override
	public int compareTo(final Statistic o) {
		return 0;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		OccurrenceRecord that = (OccurrenceRecord) o;

		return count==that.count && name.equals(that.name);
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + (int) (count ^ (count >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "OccurrenceRecord [" +
				"name='" + name + '\'' +
				", count=" + count +
				']';
	}
}
