package org.bullbots.visionprocessing;

import org.bullbots.visionprocessing.camera.Camera;
import org.bullbots.visionprocessing.processor.AutonomousProcessor;
import org.bullbots.visionprocessing.processor.BallFinder;
import org.opencv.core.Core;

public class AbstractVisionProcessor {

	public enum Mode {
		AUTO, TELEOP, UNKNOWN
	}

	protected Settings settings = Settings.getInstance();

	protected Camera camera;
	protected BallFinder ballfinder;
	protected VisionNetworkTable networkTable;
	protected AutonomousProcessor autonomousProcessor;

	public AbstractVisionProcessor() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println(">> Successfully loaded native library [OpenCV "
				+ Core.VERSION + "]");
	}

	protected void init() {
		camera = loadClass(Camera.class,
				settings.getProperty(Settings.CAMERA_CLASS));
		ballfinder = loadClass(BallFinder.class,
				settings.getProperty(Settings.BALLFINDER_CLASS));
		networkTable = loadClass(VisionNetworkTable.class,
				settings.getProperty(Settings.NETWORKTABLE_CLASS));
		autonomousProcessor = loadClass(AutonomousProcessor.class,
				settings.getProperty(Settings.AUTONOMOUS_PROCESSOR));
	}

	private <T> T loadClass(Class T, String className) {
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		T inst = null;
		Class<?> clazz = null;
		try {
			clazz = cl.loadClass(className);
			inst = (T) clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		assert (inst != null);
		return inst;
	}

	protected void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}