import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.util.Pair;

public class CollageGenerator {
	
	public static Image generateCollage(BufferedImage[] images) {
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
		return collage;
	}
	
}
