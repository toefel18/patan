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

package nl.toefel.java.code.measurements.referenceimpl.concurrency;

import nl.toefel.java.code.measurements.referenceimpl.ConcurrencyTest;

import java.util.concurrent.CountDownLatch;

class SamplePoster extends EventPostingTask {

    public SamplePoster(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
        super(starter, finisher, eventName, timesToPost);
    }

    @Override
    protected void doPost(String eventName) {
        ConcurrencyTest.subject.addSample(eventName, 1 + getTimesPosted());
    }
}
