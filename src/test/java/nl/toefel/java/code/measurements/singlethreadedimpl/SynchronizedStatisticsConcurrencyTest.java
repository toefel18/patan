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

package nl.toefel.java.code.measurements.singlethreadedimpl;

import nl.toefel.java.code.measurements.StatisticsFactory;
import nl.toefel.java.code.measurements.api.Statistics;
import nl.toefel.java.code.measurements.concurrencytest.ConcurrencyTestBase;
import org.junit.Test;

public class SynchronizedStatisticsConcurrencyTest extends ConcurrencyTestBase {
    @Override
    protected Statistics createStatistics() {
        return StatisticsFactory.createThreadsafeStatistics();
    }

    @Test
    public void testConcurrencyOneThreadEach() {
        runConcurrencyTest(1, 10000);
    }

    @Test
    public void testConcurrencyTwoThreadEach() {
        runConcurrencyTest(2, 10000);
    }

    @Test
    public void testConcurrencyTenThreadsEach() {
        runConcurrencyTest(10, 10000);
    }

    @Test
    public void testConcurrencyHundredThreadsEach() {
        runConcurrencyTest(100, 10000);
    }

    @Test
    public void testConcurrencyFiveHundredThreadsEach() {
        runConcurrencyTest(500, 10000);
    }
}
