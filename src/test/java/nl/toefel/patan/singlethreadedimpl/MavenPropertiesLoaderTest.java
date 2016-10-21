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

package nl.toefel.patan.singlethreadedimpl;

import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static junit.framework.TestCase.assertEquals;
import static nl.toefel.patan.singlethreadedimpl.MavenPropertiesLoader.*;
import static org.junit.Assert.assertTrue;

public class MavenPropertiesLoaderTest {

    /**
     * To test with actual jar file manually.
     *
     * Run with: java -cp target/*:target/test-classes nl.toefel.patan.singlethreadedimpl.MavenPropertiesLoaderTest
     */
    public static void main(String[] args) throws IOException {
        System.out.println("maven-properties: " + MavenPropertiesLoader.load());
    }

    @Test
    public void load() throws IOException {
        Properties props = MavenPropertiesLoader.load();
        assertEquals(GROUP_ID_VALUE, props.getProperty(GROUP_ID));
        assertEquals(ARTIFACT_ID_VALUE, props.getProperty(ARTIFACT_ID));
        assertTrue(props.containsKey(VERSION));
        assertTrue(MavenPropertiesLoader.getArtifactIdAndVersion(props).startsWith(ARTIFACT_ID_VALUE));
        assertTrue(MavenPropertiesLoader.getArtifactIdAndVersion(props).endsWith(props.getProperty(VERSION)));
    }
}
