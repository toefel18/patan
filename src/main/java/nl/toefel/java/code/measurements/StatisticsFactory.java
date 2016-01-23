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

package nl.toefel.java.code.measurements;

import nl.toefel.java.code.measurements.api.Statistics;
import nl.toefel.java.code.measurements.referenceimpl.SingleThreadStatisticsFacade;
import nl.toefel.java.code.measurements.referenceimpl.SynchronizedStatistics;

/**
 * Factory for statistics instances. All clients should use this factory to create {@link Statistics} instances.
 */
public final class StatisticsFactory {

    /**
     * Creates a thread-safe {@link Statistics} implementation that is unrelated to all other instances of statistics.
     *
     * @return a new thread-safe Statistics instance
     */
    public static Statistics createThreadsafeStatistics () {
        return new SynchronizedStatistics(new SingleThreadStatisticsFacade());
    }

    /**
     * Creates a {@link Statistics} instance that is not safe to use in a multi-threaded environment. This is however
     * more efficient in single-threaded environments.
     *
     * @return a new non-thread-safe instance
     */
    public static Statistics createSingleThreadedStatistics() {
        return new SingleThreadStatisticsFacade();
    }
}
