package collagebuilder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.imageio.ImageIO;

import javafx.util.Pair;

public class CollageGenerator {
	
	public static Collage generateCollage(BufferedImage[] images, String topic) {
		final double idealRatio = 1200.0 / 900;
		ArrayList<Pair<Integer, Double>> ratios = new ArrayList<>(30);
		
		for (int i = 0; i < images.length; ++i) {
			BufferedImage currImg = images[i];
			double imgWidth = currImg.getWidth();
			double imgHeight = currImg.getHeight();
			double imgRatio = imgWidth / imgHeight;
			double diff = Math.abs(imgRatio - idealRatio);
			
			Pair<Integer, Double> indexAndDiff = new Pair<>(i, diff);
			ratios.set(i, indexAndDiff);
		}
		Collections.sort(ratios, new Comparator<Pair<Integer, Double>>() {
			public int compare(Pair<Integer, Double> pair1, Pair<Integer, Double> pair2) {
				return pair1.getValue().compareTo(pair2.getValue());
			}
		});
		
		int baseImgIndex = ratios.get(0).getKey();
		BufferedImage backgroundImg = images[baseImgIndex];
		
		final int COLLAGE_WIDTH = backgroundImg.getWidth() + 6;
		final int COLLAGE_HEIGHT = backgroundImg.getHeight() + 6;
		
		BufferedImage collage = new BufferedImage(COLLAGE_WIDTH,
												  COLLAGE_HEIGHT,
												  BufferedImage.TYPE_INT_RGB);
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
	
}
