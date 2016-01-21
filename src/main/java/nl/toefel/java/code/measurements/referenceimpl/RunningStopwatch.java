package nl.toefel.java.code.measurements.referenceimpl;

import nl.toefel.java.code.measurements.api.Stopwatch;

/**
 * @author hestersco
 */
public class RunningStopwatch implements Stopwatch {
	protected final long millisAtStart;

	public static RunningStopwatch startNewStopwatch() {
		return new RunningStopwatch(System.currentTimeMillis());
	}

	protected RunningStopwatch(long millisAtStart) {
		this.millisAtStart = millisAtStart;
	}

	@Override
	public long elapsedMillis() {
		return System.currentTimeMillis() - millisAtStart;
	}
}
