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

package nl.toefel.java.code.measurements.referenceimpl;

import nl.toefel.java.code.measurements.api.Statistic;
import nl.toefel.java.code.measurements.api.Statistics;
import nl.toefel.java.code.measurements.referenceimpl.concurrency.EventPostingTask;
import nl.toefel.java.code.measurements.referenceimpl.concurrency.PosterFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static nl.toefel.java.code.measurements.referenceimpl.concurrency.PosterFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ConcurrencyTest {

    private static final String OCCURRENCE_EVENT = "occurrence";
    private static final String SAMPLE_EVENT = "sample";
    private static final String DURATION_EVENT = "duration";

    private CountDownLatch starter;
    private CountDownLatch finisher;

    private List<EventPostingTask> concurrentTasks;

    public static Statistics subject;

    void setupTestForAllSampleMethods(int occurrencePosters,
                                      int samplePosters,
                                      int durationPosters,
                                      int loopsPerPoster) {
        int totalThreads = occurrencePosters + samplePosters + durationPosters;
        subject = new StatisticsFacade();
        starter = new CountDownLatch(1);
        finisher = new CountDownLatch(totalThreads);
        concurrentTasks = new ArrayList<EventPostingTask>();
        addConcurrentTasks(OCCURRENCE_POSTER_FACTORY, OCCURRENCE_EVENT, occurrencePosters, loopsPerPoster);
        addConcurrentTasks(SAMPLE_POSTER_FACTORY, SAMPLE_EVENT, samplePosters, loopsPerPoster);
        addConcurrentTasks(DURATION_POSTER_FACTORY, DURATION_EVENT, durationPosters, loopsPerPoster);
        assertThat(concurrentTasks).hasSize(totalThreads);
        prepareThreads();
        System.out.println(String.format("creatd %d threads ready to fire", concurrentTasks.size()));
    }

    void addConcurrentTasks(PosterFactory factory, String eventName, int instances, int loops) {
        for (int i = 0; i < instances; i++) {
            concurrentTasks.add(factory.create(starter, finisher, eventName, loops));
        }
    }

    void prepareThreads() {
        for (EventPostingTask task : concurrentTasks) {
            new Thread(task).start();
            System.out.println(String.format("creating posting task for event '%s' of type %s", task.getEventName(), task.getClass().getSimpleName()));
        }
    }

    private void start() {
        starter.countDown();
    }

    void runConcurrencyTest(int occurrenceThreads, int samplesThreads, int durationsThreads, int loopsPerThread) {
        setupTestForAllSampleMethods(occurrenceThreads, samplesThreads, durationsThreads, loopsPerThread);
        start();
        waitTillAllTasksFinish();
        Map<String, Statistic> statistics = subject.getSortedSnapshot();
        assertThat(statistics).hasSize(3).containsOnlyKeys(OCCURRENCE_EVENT, SAMPLE_EVENT, DURATION_EVENT);
        assertThat(statistics.get(OCCURRENCE_EVENT).getSampleCount()).as("recored occurrences").isEqualTo(occurrenceThreads * loopsPerThread);
        assertThat(statistics.get(SAMPLE_EVENT).getSampleCount()).as("recored samples").isEqualTo(samplesThreads * loopsPerThread);
        assertThat(statistics.get(DURATION_EVENT).getSampleCount()).as("recored durations").isEqualTo(durationsThreads * loopsPerThread);
    }

    private void waitTillAllTasksFinish() {
        try {
            finisher.await();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testConcurrencyOneThreadEach() {
        runConcurrencyTest(1, 1, 1, 1000);
    }

    @Test
    public void testConcurrencyTwoThreadEach() {
        runConcurrencyTest(2, 2, 2, 1000);
    }

    @Test
    public void testConcurrencyTenThreadsEach() {
        runConcurrencyTest(10, 10, 10, 1000);
    }
}

