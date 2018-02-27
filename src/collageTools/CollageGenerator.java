package collageTools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.imageio.ImageIO;


import objects.Collage; 

public class CollageGenerator {
	
	public static Collage generateCollage(Collection<BufferedImage> collection, String topic) {
		
		BufferedImage backgroundImg = findBackground(collection);
		
		final int COLLAGE_WIDTH = backgroundImg.getWidth() + 6;
		final int COLLAGE_HEIGHT = backgroundImg.getHeight() + 6;
		
		BufferedImage collage = new BufferedImage(COLLAGE_WIDTH,COLLAGE_HEIGHT,BufferedImage.TYPE_INT_RGB);
		Graphics2D collageGraphics = collage.createGraphics();
		
		// Fill background of the collage with a white color
		Color prevColor = collageGraphics.getColor();
		collageGraphics.setPaint(Color.WHITE);
		collageGraphics.fillRect(0, 0, COLLAGE_WIDTH, COLLAGE_HEIGHT);
		collageGraphics.setPaint(prevColor);
		
		// Stick the base background image (to fill up the entire collage space) in the canvas
		collageGraphics.drawImage(backgroundImg, 3, 3, null);
		
		collageGraphics.dispose();
		
		// Save the collage to a file
		String filePath = null;
		try {
		    File outputfile = new File("saved_collage.png");
		    ImageIO.write(collage, "png", outputfile);
		    filePath = outputfile.getAbsolutePath();
		} catch (IOException e) {
		    // Do nothing
		}
		
		return new Collage(filePath, COLLAGE_HEIGHT, COLLAGE_WIDTH, topic, false);
	}
	
	private static BufferedImage findBackground(Collection<BufferedImage> collection) {
		final double idealRatio = 1200.0 / 900;
		
		double minDiff = Double.MAX_VALUE;
		BufferedImage backgroundImage = null;
		
		for(BufferedImage currImg:collection) {
			if(currImg == null)
				continue;
			double imgRatio = currImg.getWidth() / currImg.getHeight();
			double diff = Math.abs(imgRatio - idealRatio);
			if (diff < minDiff) {
				minDiff = diff;
				backgroundImage = currImg;
			}
			
		}
		
		return backgroundImage;
	}
	
}
