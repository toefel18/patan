package nl.toefel.patan.singlethreadedimpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class MavenPropertiesLoader {

    static String GROUP_ID = "groupId";
    static String ARTIFACT_ID = "artifactId";
    static String VERSION = "version";

    static String GROUP = "nl.toefel";
    static String ARTIFACT = "patan";

    static String POM_PROPERTIES_PATH = String.format("/META-INF/maven/%s/%s/pom.properties", GROUP, ARTIFACT);


	private MavenPropertiesLoader() {
		// uitil class
	}

	static Properties load() throws IOException {
        return load(MavenPropertiesLoader.class.getResourceAsStream(POM_PROPERTIES_PATH));
	}

	static Properties load(InputStream stream) throws IOException {
		Properties props = new Properties();
		try {
			props.load(stream);
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		return props;
	}
}
