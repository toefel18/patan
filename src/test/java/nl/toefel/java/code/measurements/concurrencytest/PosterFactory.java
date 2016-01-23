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

public enum PosterFactory {

    OCCURRENCE_POSTER_FACTORY {
        @Override
        public EventPostingTask create(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
            return new OccurrencePoster(starter, finisher, eventName, timesToPost);
        }
    },
    DURATION_POSTER_FACTORY{
        @Override
        public EventPostingTask create(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
            return new DurationPoster(starter, finisher, eventName, timesToPost);
        }
    },
    SAMPLE_POSTER_FACTORY {
        @Override
        public EventPostingTask create(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost) {
            return new SamplePoster(starter, finisher, eventName, timesToPost);
        }
    };

    public abstract EventPostingTask create(CountDownLatch starter, CountDownLatch finisher, String eventName, int timesToPost);
}
