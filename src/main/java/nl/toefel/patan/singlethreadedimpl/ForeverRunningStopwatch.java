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

package nl.toefel.patan.singlethreadedimpl;

import nl.toefel.patan.api.Stopwatch;

public class ForeverRunningStopwatch implements Stopwatch {

	private static final double MEGA = 1000 * 1000;

	private final long nanosAtStart;


	public static ForeverRunningStopwatch startNewStopwatch() {
		return new ForeverRunningStopwatch(System.nanoTime());
	}

	private ForeverRunningStopwatch(long nanosAtStart) {
		this.nanosAtStart = nanosAtStart;
	}

	@Override
	public double elapsedMillis() {
		return elapsedNanos() / MEGA;
	}

	private long elapsedNanos() {
		return System.nanoTime() - nanosAtStart; // see warning for numerical overflow at System.nanoTime()
	}
}
