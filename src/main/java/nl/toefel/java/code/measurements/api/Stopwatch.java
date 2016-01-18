/*
 *    Copyright 2016 Christophe Hesters
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package nl.toefel.java.code.measurements.api;

/**
 * Interface for an object that measures elapsed time. The time does not have to related to any time system. The resolution
 * of the stopwatch should be nanoseconds, but the precision depends on the underlying implementation.
 */
public interface Stopwatch {

	/**
	 * @return the elapsed time since the creation of this StopWatch in nanosecond resolution.
	 */
	long elapsedNanos();

	/**
	 * @return the elapsed time since the creation of this StopWatch in millisecond resolution.
	 */
	long elapsedMillis();
}
