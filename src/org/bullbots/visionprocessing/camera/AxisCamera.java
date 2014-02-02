package org.bullbots.visionprocessing.camera;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;


public class AxisCamera {

	private String addr, user = "frc", pass = "frc";
	private byte[] curFrame;

	private HttpURLConnection conn;
	private BufferedInputStream httpIn;
	private ByteArrayOutputStream jpgOut;
	private BufferedImage cameraImage;
	
	private boolean finished = false;
	
    int prev = 0;
    int cur = 0;
    
	private URL url;
	private Base64Encoder base64 = new Base64Encoder();
	
	public AxisCamera(String addr) {
		this.addr = addr;
		setupConnection();
		System.out.println(">> Connection to camera was successfully established.");
	}
	
	public void setupConnection() {
		try {
			url = new URL(addr);
		} catch (MalformedURLException e) {
			System.err.println("Invalid URL");
			return;
		}

		try {
			// Sets up a connection with the camera
			conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestProperty("Authorization",
//					"Basic " + base64.encode(user + ":" + pass));
			httpIn = new BufferedInputStream(conn.getInputStream(), 8192);
			
		} catch (IOException e) {
			System.err.println("Unable to connect: " + e.getMessage());
			return;
		}
	}

	private void grabImage() {		
		// Actual grabbing of bytes below below
		finished = false;
	    
		try {
			while (httpIn != null &&  (cur = httpIn.read()) >= 0 && !finished) {
				if (prev == 0xFF && cur == 0xD8) { // Looks for the start of the byte stream
					jpgOut = new ByteArrayOutputStream(8192);
					jpgOut.write((byte) prev);
				}
				if (jpgOut != null) {
					jpgOut.write((byte) cur);
				}
				if (prev == 0xFF && cur == 0xD9) { // Looks for the end of the byte stream
					curFrame = jpgOut.toByteArray();
					jpgOut.close();
					finished=true;
				}
				prev = cur;
			}
		} catch (IOException e) {
			System.err.println("I/O Error: " + e.getMessage() + "\nAn IO Error occured... Closing down application");
			System.exit(0);
		}
		
		try {
			// Converts byte array into a BufferedImage
			ByteArrayInputStream jpgIn = new ByteArrayInputStream(curFrame);
			cameraImage = ImageIO.read(jpgIn);
			jpgIn.close();
		} catch (IOException e) {
			System.err.println("Error acquiring the frame: " + e.getMessage());
		}
	}
	
	public BufferedImage getImage() {
		grabImage();
		return cameraImage;
	}
}
