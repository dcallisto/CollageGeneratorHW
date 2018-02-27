package collageTools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.imageio.ImageIO;

import objects.Collage;

public class CollageGenerator
{

	private final static double idealAspectRatio = 1200.0 / 900;

	public static Collage generateCollage (Collection<BufferedImage> collection, String topic)
	{
		BufferedImage backgroundImg = findBackgroundImage(collection);

		final int COLLAGE_WIDTH = backgroundImg.getWidth() + 6;
		final int COLLAGE_HEIGHT = backgroundImg.getHeight() + 6;

		BufferedImage collage = new BufferedImage(COLLAGE_WIDTH, COLLAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D collageGraphics = collage.createGraphics();

		// Fill background of the collage with a white color
		Color prevColor = collageGraphics.getColor();
		collageGraphics.setPaint(Color.WHITE);
		collageGraphics.fillRect(0, 0, COLLAGE_WIDTH, COLLAGE_HEIGHT);
		collageGraphics.setPaint(prevColor);

		// Stick the base background image (to fill up the entire collage space) in the
		// canvas
		collageGraphics.drawImage(backgroundImg, 3, 3, null);

		ImageTools.addBorder(collection);
		ImageTools.rotateImages(collection);
		ImageTools.resizeImages(collection);
		ImageTools.translateImages(collection);

		for (BufferedImage image : collection) {
			collageGraphics.drawImage(image, 3, 3, null);
		}

		collageGraphics.dispose();

		// Save the collage to a file
		String filePath = null;
		try {
			File outputfile = new File("saved_collage.png");
			ImageIO.write(collage, "png", outputfile);
			filePath = outputfile.getAbsolutePath();
		}
		catch (IOException e) {
			// Do nothing
		}

		return new Collage(filePath, COLLAGE_HEIGHT, COLLAGE_WIDTH, topic, false);
	}

	/**
	 * Find an image from the collection with an aspect ratio closest to the ideal aspect ratio.
	 * 
	 * @param collection A collection of buffered images.
	 * @return BufferedImage
	 */
	private static BufferedImage findBackgroundImage (Collection<BufferedImage> collection, double idealAspectRatio)
	{
		double minDiff = Double.MAX_VALUE;
		BufferedImage backgroundImage = null;

		for (BufferedImage currImg : collection) {
			if (currImg == null) continue;
			double imageAspectRatio = currImg.getWidth() / currImg.getHeight();
			double aspectRatioDifference = Math.abs(imageAspectRatio - idealAspectRatio);
			if (aspectRatioDifference < minDiff) {
				minDiff = aspectRatioDifference;
				backgroundImage = currImg;
			}
		}
		return backgroundImage;
	}
	
	private static BufferedImage findBackgroundImage (Collection<BufferedImage> collection)
	{
		return findBackgroundImage(collection, CollageGenerator.idealAspectRatio);
	}

}
