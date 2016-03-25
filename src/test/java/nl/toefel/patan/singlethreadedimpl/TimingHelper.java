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

package nl.toefel.patan.singlethreadedimpl;

public class TimingHelper {

	public static String expensiveMethodTakingMillis(final int millis) {
		try {
			Thread.sleep(millis);
			return "hi";
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	public static String expensiveMethodTakingMillisException(final int millis) {
		try {
			Thread.sleep(millis);
			throw new IllegalArgumentException();
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}
}