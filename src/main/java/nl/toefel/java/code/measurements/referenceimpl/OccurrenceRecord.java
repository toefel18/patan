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
		if (o == null) {
			return -1;
		} else {
			return name.compareTo(o.getName());
		}
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		OccurrenceRecord that = (OccurrenceRecord) o;

		return name.equals(that.name);

	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return "OccurrenceRecord [" +
				"name='" + name + '\'' +
				", count=" + count +
				']';
	}
}
