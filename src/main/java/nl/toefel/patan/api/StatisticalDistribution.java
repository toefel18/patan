package nl.toefel.patan.api;

/**
 * Structure describing a statistical distribution.
 * <p>
 * The name of the statistic is not stored in this object itself because it's always mapped by a name through the API
 * (users store by name, find by name and the {@link Snapshot} contains a map by name), storing the name therefore feels
 * redundant.
 */
public interface StatisticalDistribution {

	/**
	 * Creates a new statistical distribution object based on this distribution and the extra
	 * value that is being added.
	 *
	 * @param sampleValue the value to merge with this record
	 * @return new statistical distribution.
	 */
	StatisticalDistribution newWithExtraSample(long sampleValue);

	/**
	 * @return false if distribution does not contain samples
	 */
	boolean isEmpty();

	/**
	 * @return the number of recorded samples in the distribution
	 */
	long getSampleCount();

	/**
	 * @return the lowest value in the distribution
	 */
	long getMinimum();

	/**
	 * @return the highest value in the distribution
	 */
	long getMaximum();

	/**
	 * @return the average of all samples in the distribution
	 */
	double getAverage();

	/**
	 * @return the variance of all samples in the distribution
	 */
	double getVariance();

	/**
	 * @return the standard deviation of all samples in the distribution
	 */
	double getStdDeviation();
}
