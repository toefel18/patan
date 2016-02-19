package nl.toefel.java.code.measurements.api;

/**
 * Useful to be able to measure Java8 style String retValue = stats.recordElapsedTime("test.duration", () ->
 * expensiveMethodTakingMillis(100));
 *
 * @param <T>
 */
public interface TimedTask<T> {
    // @FunctionalInterface
    T get();
}