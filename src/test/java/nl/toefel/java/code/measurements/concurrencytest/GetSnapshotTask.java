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

package nl.toefel.java.code.measurements.concurrencytest;

import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

class GetSnapshotTask extends ConcurrentTask {

    public GetSnapshotTask(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
        super(starter, finisher, eventName, timesToPost);
    }

    @Override
    protected void doPost(String eventName) {
        try {
            assertThat(ConcurrencyTestBase.subject.getSnapshot()).isNotNull();
        } catch (Throwable t) {
            failed = true;
            System.out.println(t.getMessage());
        }
    }
}
