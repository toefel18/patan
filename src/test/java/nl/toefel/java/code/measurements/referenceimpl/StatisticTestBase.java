package nl.toefel.java.code.measurements.referenceimpl;

import nl.toefel.java.code.measurements.api.Statistic;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StatisticTestBase {

	public void testEqualsHashcodeSameName(Statistic stat1, Statistic stat2) {
		assertThat(stat1).isEqualTo(stat2);
		assertThat(stat1.hashCode()).isNotEqualTo(stat2);
	}

	public void testEqualsNull(Statistic stat) {
		assertThat(stat.equals(null)).isFalse();
	}

	public void testEqualsHashcodeOtherName(Statistic stat1, Statistic stat2) {
		assertThat(stat1).isNotEqualTo(stat2);
		assertThat(stat1.hashCode()).isNotEqualTo(stat2);
	}

	public void testComparable(Statistic a, Statistic b) {
		List<Statistic> records = new ArrayList<Statistic>();
		records.add(b);
		records.add(a);
		assertThat(records).containsSequence(b, a);
		Collections.sort(records);
		assertThat(records).containsSequence(a, b).isSorted();
	}

	public void testComparableAgainstNull(Statistic a) {
		assertThat(a.compareTo(null)).isEqualTo(-1);
	}

	public String s(Number nr) {
		return String.valueOf(nr);
	}
}
