package org.bullbots.visionprocessing.camera.impl;

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

import org.bullbots.visionprocessing.camera.Camera;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;



public class AxisCamera implements Camera{

	private String addr="http://10.18.91.11/mjpg/video.mjpg",
			user = "frc", pass = "frc";
	private byte[] curFrame;

	private HttpURLConnection conn;
	private BufferedInputStream httpIn;

	private boolean finished = false;
	
    int prev = 0;
    int cur = 0;
    
	private URL url;
	private Base64Encoder base64 = new Base64Encoder();
	
	public AxisCamera() {
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

	private BufferedImage grabImage() {
	 BufferedImage cameraImage=null;
	 ByteArrayOutputStream jpgOut=null;
		
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
			// Converts byte array into a BufferedImage
			ByteArrayInputStream jpgIn = new ByteArrayInputStream(curFrame);
			cameraImage = ImageIO.read(jpgIn);
			jpgIn.close();
		} catch (IOException e) {
			System.err.println("I/O Error: " + e.getMessage() + "\nAn IO Error occured... Closing down application");
			System.exit(0);
		}
		return cameraImage;
	}
	
	public Mat getImage() {
		return convBuff2Mat(grabImage());
	}
	
	public BufferedImage convMat2Buff(Mat mat) {
		// Code for converting Mat to BufferedImage
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if(mat.channels() > 1) {
			Mat mat2 = new Mat();
			Imgproc.cvtColor(mat,  mat2, Imgproc.COLOR_BGR2RGB);
			type = BufferedImage.TYPE_3BYTE_BGR;
			mat = mat2;
		}
		byte[] b = new byte[mat.channels() * mat.cols() * mat.rows()];
		mat.get(0, 0, b); // Get all the pixels
		BufferedImage bImage = new BufferedImage(mat.cols(), mat.rows(), type);
		bImage.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), b);
		return bImage;
	}
	
	public Mat convBuff2Mat(BufferedImage image) {
		byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
    	Mat matImage = new Mat(image.getHeight(),image.getWidth(),CvType.CV_8UC3);
		matImage.put(0, 0, pixels);
		return matImage;
	}

	public BufferedImage getBIImage() {
		return grabImage();
	}
}