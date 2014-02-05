package org.bullbots.visionprocessing;

import org.bullbots.visionprocessing.camera.Camera;
import org.opencv.core.Mat;

import java.lang.ClassLoader;

public class VisionProcessor {
	Settings settings = Settings.getInstance();
	
	Camera camera;
	
	void init(){
		 camera = loadCamera();
	}

	private Camera loadCamera() {
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		Camera camera = null;
		Class<?> clazz=null;
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
		assert(camera != null);
		System.out.println("Camera loaded...");
		return camera;
	}
	
	//processor
	void run(){
		// main loop
		init();
		
		while (true){
			Mat img = camera.getImage();
			System.out.println(">>"+img.height()+" "+img.width());
		}
	}
	
	//Main file
	public static void main(String[] args) {
		new VisionProcessor().run();
	}

}
