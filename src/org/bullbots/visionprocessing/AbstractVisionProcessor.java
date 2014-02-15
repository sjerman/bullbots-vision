package org.bullbots.visionprocessing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bullbots.visionprocessing.camera.Camera;
import org.bullbots.visionprocessing.processor.AutonomousProcessor;
import org.bullbots.visionprocessing.processor.BallFinder;
import org.opencv.core.Core;

public class AbstractVisionProcessor {

	public enum Mode {
		AUTO, TELEOP, UNKNOWN
	}

	protected Settings settings = Settings.getInstance();

	protected Camera autoCamera,ballCamera;
	protected BallFinder ballfinder;
	protected VisionNetworkTable networkTable;
	protected AutonomousProcessor autonomousProcessor;

	static Logger logger = LogManager.getLogger(AbstractVisionProcessor.class
			.getName());

	public AbstractVisionProcessor() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		logger.info("Successfully loaded native library [OpenCV "
				+ Core.VERSION + "]");
	}

	protected void init() {
		autoCamera = loadClass(Camera.class,
				settings.getProperty(Settings.AUTO_CAMERA_CLASS));
		String autoCameraAddress =
				settings.getProperty(Settings.AUTO_CAMERA_ADDR);
		ballCamera = loadClass(Camera.class,
				settings.getProperty(Settings.BALL_CAMERA_CLASS));
		String ballCameraAddress = 
				settings.getProperty(Settings.BALL_CAMERA_ADDR);
		if  (autoCameraAddress.equals(ballCameraAddress)) {
			ballCamera = autoCamera;
			ballCamera.setAddress(ballCameraAddress);
		} else {
			ballCamera.setAddress(ballCameraAddress);
			autoCamera.setAddress(autoCameraAddress);
		}

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