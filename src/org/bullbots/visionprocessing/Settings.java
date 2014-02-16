package org.bullbots.visionprocessing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.bullbots.visionprocessing.processor.impl.Viewer;

public class Settings extends Properties {

	private static String PROPERTIES_FILE = "vision.properties";

	public static final String NETWORKTABLE_CLASS = "networktable";

	public static final String BALLFINDER_CLASS = "ballfinder";

	public static final String TEST_MODE = "testmode";

	public static final String SHOW_IMAGE = "showimage";

	public static final String AUTONOMOUS_PROCESSOR = "autoprocessor";

	public static final String HAARCASCADE = "haarcascade";

	public static final String SAVEIMAGES = "saveimages";

	public static final String BALL_CAMERA_CLASS = "ballCamera";
	public static final String BALL_CAMERA_ADDR = "ballCameraAddress";
	public static final String AUTO_CAMERA_CLASS = "autoCamera";
	public static final String AUTO_CAMERA_ADDR = "autoCameraAddress";

	private static Settings INSTANCE = null;

	private Viewer viewer = null;

	private Settings() {
		super();
		try {
			this.load(new FileInputStream(PROPERTIES_FILE));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't find properties file:"
					+ PROPERTIES_FILE);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't read properties file:"
					+ PROPERTIES_FILE);
			System.exit(1);
		}
		String prop = getProperty(SHOW_IMAGE, "true");
		if (prop.equals("true")) {
			viewer = new Viewer();
		}
	}

	public static Settings getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Settings();
		}
		return INSTANCE;
	}

	public static boolean showImage() {
		String prop = getInstance().getProperty(SHOW_IMAGE, "true");
		if (prop.equals("true"))
			return true;
		else
			return false;
	}

	public static boolean saveImages() {
		String prop = getInstance().getProperty(SAVEIMAGES, "false");
		if (prop.equals("true"))
			return true;
		else
			return false;
	}

	public static String getTestMode() {
		return getInstance().getProperty(TEST_MODE, "AUTO");
	}

	public static Viewer getViewer() {
		return getInstance().viewer;
	}

	public static String getHaarCascade() {
		String vid = getInstance().getProperty(HAARCASCADE);
		assert (vid != null);
		return vid;
	}

}
