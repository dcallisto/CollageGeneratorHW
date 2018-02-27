package collagebuilder;

import java.io.Serializable;

public class Collage implements Serializable {
		
	private static final long serialVersionUID = 1L;
	
	private String imageFilePath;
	private int height;
	private int width;
	private String topic;
	private boolean error;
	
	public Collage(String imageFilePath, int height, int width, String topic, boolean error) {
		this.imageFilePath = imageFilePath;
		this.height = height;
		this.width = width;
		this.topic = topic;
		this.error = error;
	}
	
	public String getImageFilePath() {
		return this.imageFilePath;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public String getTopic() {
		return this.topic;
	}
	
	public boolean getError() {
		return error;
	}
}
