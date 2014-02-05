package org.bullbots.visionprocessing;

import org.bullbots.visionprocessing.camera.Camera;
import org.opencv.core.Core;

public class AbstractVisionProcessor {
	
	public enum Mode {
		AUTO, TELEOP
	}

	Settings settings = Settings.getInstance();
	protected Camera camera;

	public AbstractVisionProcessor() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println(">> Successfully loaded native library [OpenCV " + Core.VERSION + "]");
	}

	protected void init() {
		camera = loadCamera();
	}

	private Camera loadCamera() {
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		Camera camera = null;
		Class<?> clazz = null;
		try {
			clazz = cl.loadClass(settings.getProperty(Settings.CAMERA_CLASS));
			camera = (Camera) clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		assert (camera != null);
		System.out.println("Camera loaded...");
		return camera;
	}

	protected Mode getMode() {
		return Mode.TELEOP;
	}

	protected void sleep() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}