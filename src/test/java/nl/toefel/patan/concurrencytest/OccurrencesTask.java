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

package nl.toefel.patan.concurrencytest;

import java.util.concurrent.CountDownLatch;

/**
 * Posts two occurrences per task
 */
class OccurrencesTask extends ConcurrentTask {

    public static final int OCCURRENCES_PER_RUN = 2;

    public OccurrencesTask(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
        super(starter, finisher, eventName, timesToPost, WRITES_TO_STATISTICS);
    }

    @Override
    protected void doTask(String eventName) {
        ConcurrencyTestBase.subject.addOccurrences(eventName, OCCURRENCES_PER_RUN);
    }
}
