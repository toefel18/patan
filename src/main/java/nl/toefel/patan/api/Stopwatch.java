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

package nl.toefel.patan.api;

/**
 * Interface for an object that measures elapsed time. The time does not have to be related to any time system. NB. The precision
 * of the stopwatch is in nanoseconds (typically implementations will use {@link System#nanoTime()}.
 */
public interface Stopwatch {

	/**
	 * @return The elapsed time since the creation of this StopWatch in milliseconds with nanosecond precision.
	 */
	double elapsedMillis();
}
