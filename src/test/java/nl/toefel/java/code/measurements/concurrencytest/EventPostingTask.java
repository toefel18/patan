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

public abstract class EventPostingTask implements Runnable {
    private final CountDownLatch starter;
    private final CountDownLatch finisher;

    private final String eventName;
    private final int timesToPost;
    private volatile int timesPosted;

    public EventPostingTask(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
        this.starter = starter;
        this.finisher = finisher;
        this.eventName = eventName;
        this.timesToPost = timesToPost;
        if (timesToPost <= 0 || starter == null || finisher == null || eventName == null) {
            throw new IllegalArgumentException("provide valid arguments! somethings wrong");
        }
    }

    @Override
    public void run() {
        try {
            starter.await(); // wait for start signal
            for (int i = 0; i < timesToPost; i++) {
                doPost(eventName);
                timesPosted++;
            }
            finisher.countDown(); //wait for end signal
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    protected abstract void doPost(final String eventName);

    public String getEventName() {
        return eventName;
    }

    public int getEventsPosted() {
        return timesPosted;
    }
}
