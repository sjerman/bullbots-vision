package org.bullbots.visionprocessing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Settings extends Properties{
	
	private static String PROPERTIES_FILE = "vision.properties";
	
	private static Settings INSTANCE=null;
	
	private Settings() {
		super();
		try {
			this.load(new FileInputStream(PROPERTIES_FILE));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't find properties file:"+PROPERTIES_FILE);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't read properties file:"+PROPERTIES_FILE);
			System.exit(1);
		}
	}
	
	public static Settings getInstance(){
		if (INSTANCE==null) {
			INSTANCE = new Settings();
		}
		return INSTANCE;
	}


	static String CAMERA_CLASS = "camera";

}
