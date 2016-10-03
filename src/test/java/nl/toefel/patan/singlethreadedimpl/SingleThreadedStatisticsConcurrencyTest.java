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

import nl.toefel.patan.StatisticsFactory;
import nl.toefel.patan.api.Statistics;
import nl.toefel.patan.concurrencytest.ConcurrencyTestBase;
import org.junit.Test;

public class SingleThreadedStatisticsConcurrencyTest extends ConcurrencyTestBase {
    @Override
    protected Statistics createStatistics() {
        return StatisticsFactory.createSingleThreadedStatistics();
    }

    /**
     * Pseudo unit test, Throwable is not always thrown. Meant for running it manually several times.
     */
    @Test (expected = Throwable.class)
    public void testConcurrencyTenThreadsEach() {
        runConcurrencyTest(10, 10000);
    }
}
