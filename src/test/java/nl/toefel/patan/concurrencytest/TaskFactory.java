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
 * Life will be a little bit simpler with Java 8
 */
public enum TaskFactory {

    OCCURRENCE_TASK_FACTORY {
        @Override
        public OccurrenceTask create(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
            return new OccurrenceTask(starter, finisher, eventName, timesToPost);
        }
    },
    OCCURRENCES_TASK_FACTORY {
        @Override
        public OccurrencesTask create(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
            return new OccurrencesTask(starter, finisher, eventName, timesToPost);
        }
    },
    DURATION_TASK_FACTORY {
        @Override
        public DurationTask create(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
            return new DurationTask(starter, finisher, eventName, timesToPost);
        }
    },
    SAMPLE_TASK_FACTORY {
        @Override
        public SampleTask create(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
            return new SampleTask(starter, finisher, eventName, timesToPost);
        }
    },
    GET_SNAPSHOT_TASK_FACTORY {
        @Override
        public GetSnapshotsTask create(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
            return new GetSnapshotsTask(starter, finisher, eventName, timesToPost);
        }
    },
    FIND_SAMPLE_DISTRIBUTION_TASK_FACTORY {
        @Override
        public FindSampleDistributionTask create(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
            return new FindSampleDistributionTask(starter, finisher, eventName, timesToPost);
        }
    },
    FIND_OCCURRENCE_TASK_FACTORY {
        @Override
        public FindOccurrenceTask create(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
            return new FindOccurrenceTask(starter, finisher, eventName, timesToPost);
        }
    },
    FIND_DURATION_TASK_FACTORY {
        @Override
        public FindDurationTask create(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
            return new FindDurationTask(starter, finisher, eventName, timesToPost);
        }
    },
    RESET_SNAPSHOT_TASK_FACTORY {
        @Override
        public ResetGetSnapshotTask create(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
            return new ResetGetSnapshotTask(starter, finisher, eventName, timesToPost);
        }
    },
    RESET_ALLOCCURRENCE_SNAPSHOT_TASK_FACTORY {
        @Override
        public ResetGetAllOccurrencesSnapshotTask create(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
            return new ResetGetAllOccurrencesSnapshotTask(starter, finisher, eventName, timesToPost);
        }
    },
    RESET_ALLDURATIONS_SNAPSHOT_TASK_FACTORY {
        @Override
        public ResetGetAllDurationsSnapshotTask create(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
            return new ResetGetAllDurationsSnapshotTask(starter, finisher, eventName, timesToPost);
        }
    },
    RESET_ALLSAMPLES_SNAPSHOT_TASK_FACTORY {
        @Override
        public ResetGetAllSamplesSnapshotTask create(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
            return new ResetGetAllSamplesSnapshotTask(starter, finisher, eventName, timesToPost);
        }
    };

    public abstract ConcurrentTask create(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost);
}
