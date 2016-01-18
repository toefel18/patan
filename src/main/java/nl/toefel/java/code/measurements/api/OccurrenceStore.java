/*
 *    Copyright 2016 Christophe Hesters
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package nl.toefel.java.code.measurements.api;


/**
 * Counts occurrences of 'events' that happened. Example events:
 * <ul>
 *     <li>the number of messages received</li>
 *     <li>the number of messages sent</li>
 *     <li>timers elapsed</li>
 *     <li>the number of times web form validation failed</li>
 * </ul>
 *
 * Event names are unique identifiers for events. It is encouraged to use the same hierarchical structure as the java
 * package names, for example: <i>jms.messages.statusupdate.received</i>. This allows for easy aggregating counters for
 * the higher levels levels if it makes sense.
 *
 * An example of aggregation: say your app receives status updates and picture posts, some event names could be:
 * <ul>
 *     <li>jms.messages.received.statusupdate.ok = 155</li>
 *     <li>jms.messages.received.statusupdate.failed = 1</li>
 *     <li>jms.messages.received.statusupdate.malformed = 6</li>
 *     <li>jms.messages.received.picturepost.ok = 77</li>
 *     <li>jms.messages.received.picturepost.toobig = 9</li>
 * </ul>
 *
 * If logical, the library could automatically calculate aggregate values if it makes sense. This could look like:
 * <ul>
 *     <li>jms.messages.received.statusupdate.ok = 155</li>
 *     <li>jms.messages.received.statusupdate.failed = 1</li>
 *     <li>jms.messages.received.statusupdate.malformed = 6</li>
 *     <li>jms.messages.received.statusupdate = 162</li>
 *     <li>jms.messages.received.picturepost.ok = 77</li>
 *     <li>jms.messages.received.picturepost.toobig = 9</li>
 *     <li>jms.messages.received.picturepost = 86</li>
 *     <li>jms.messages.received = 248</li>
 * </ul>
 *
 */
public interface OccurrenceStore {

	/**
	 * Adds a single occurrence for the given eventName to the store.
	 *
	 * @param eventName event name to store the occurrence under
	 */
	void addOccurrence(final String eventName);

	/**
	 * Adds multiple occurrences for the given eventName to the store.
	 *
	 * @param eventName event name to store the occurrence under
	 * @param timesOccurred times the event occurred.
	 */
	void addOccurrences(final String eventName, int timesOccurred);
}
