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

import nl.toefel.java.code.measurements.api.Statistic;
import nl.toefel.java.code.measurements.api.Statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static nl.toefel.java.code.measurements.concurrencytest.PosterFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class ConcurrencyTestBase {

    private static final String OCCURRENCE_EVENT = "occurrence";
    private static final String SAMPLE_EVENT = "sample";
    private static final String DURATION_EVENT = "duration";

    private CountDownLatch starter;
    private CountDownLatch finisher;

    private List<EventPostingTask> concurrentTasks;

    public static Statistics subject;

    protected abstract Statistics createStatistics();

    void setupTestForAllSampleMethods(int occurrencePosters,
                                      int samplePosters,
                                      int durationPosters,
                                      int loopsPerPoster) {
        int totalThreads = occurrencePosters + samplePosters + durationPosters;
        subject = createStatistics();
        starter = new CountDownLatch(1);
        finisher = new CountDownLatch(totalThreads);
        concurrentTasks = new ArrayList<EventPostingTask>();
        addConcurrentTasks(OCCURRENCE_POSTER_FACTORY, OCCURRENCE_EVENT, occurrencePosters, loopsPerPoster);
        addConcurrentTasks(SAMPLE_POSTER_FACTORY, SAMPLE_EVENT, samplePosters, loopsPerPoster);
        addConcurrentTasks(DURATION_POSTER_FACTORY, DURATION_EVENT, durationPosters, loopsPerPoster);
        assertThat(concurrentTasks).hasSize(totalThreads);
        prepareThreads();
        System.out.println(String.format("created %d threads ready to fire", concurrentTasks.size()));
    }

    void addConcurrentTasks(PosterFactory factory, String eventName, int instances, int loops) {
        for (int i = 0; i < instances; i++) {
            concurrentTasks.add(factory.create(starter, finisher, eventName, loops));
        }
    }

    void prepareThreads() {
        for (EventPostingTask task : concurrentTasks) {
            new Thread(task).start();
        }
    }

    private void start() {
        starter.countDown();
    }

    private void waitTillAllTasksFinish() {
        try {
            finisher.await();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    private int countEventsPostedByTasks(String occurrenceEvent) {
        int eventsPostedForKey = 0;
        for (EventPostingTask task : concurrentTasks) {
            if (occurrenceEvent.equals(task.getEventName())){
                eventsPostedForKey += task.getEventsPosted();
            }
        }
        return eventsPostedForKey;
    }

    protected void runConcurrencyTest(int occurrenceThreads, int samplesThreads, int durationsThreads, int loopsPerThread) {
        setupTestForAllSampleMethods(occurrenceThreads, samplesThreads, durationsThreads, loopsPerThread);

        start();
        waitTillAllTasksFinish();

        Map<String, Statistic> statistics = subject.getSortedSnapshot();
        assertThat(statistics).hasSize(3).containsOnlyKeys(OCCURRENCE_EVENT, SAMPLE_EVENT, DURATION_EVENT);
        assertSampleCountIsAsExpected(occurrenceThreads, samplesThreads, durationsThreads, loopsPerThread, statistics);
        assertSampleCountIsSameAsPostedEventsByTasks(statistics);
    }

    private void assertSampleCountIsAsExpected(int occurrenceThreads,
                                               int samplesThreads,
                                               int durationsThreads,
                                               int loopsPerThread, Map<String, Statistic> statistics) {
        assertThat(statistics.get(OCCURRENCE_EVENT).getSampleCount()).as("expected occurrences").isEqualTo(occurrenceThreads * loopsPerThread);
        assertThat(statistics.get(SAMPLE_EVENT).getSampleCount()).as("expected samples").isEqualTo(samplesThreads * loopsPerThread);
        assertThat(statistics.get(DURATION_EVENT).getSampleCount()).as("expected durations").isEqualTo(durationsThreads * loopsPerThread);
    }

    private void assertSampleCountIsSameAsPostedEventsByTasks(Map<String, Statistic> statistics) {
        int occurrencesPosted = countEventsPostedByTasks(OCCURRENCE_EVENT);
        int samplesPosted = countEventsPostedByTasks(SAMPLE_EVENT);
        int durationsPosted = countEventsPostedByTasks(DURATION_EVENT);

        assertThat(statistics.get(OCCURRENCE_EVENT).getSampleCount()).as("").isEqualTo(occurrencesPosted);
        assertThat(statistics.get(SAMPLE_EVENT).getSampleCount()).as("recored samples").isEqualTo(samplesPosted);
        assertThat(statistics.get(DURATION_EVENT).getSampleCount()).as("recored durations").isEqualTo(durationsPosted);
    }
}

