package collageTools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Rotation;

import gimages.GoogleImageDataContainer;

/**
 * Code to add borders, rotate pictures, and edit the locations.
 * 
 * @author Noah K. Mitsuhashi
 */
public class ImageTools
{

	static int width;
	// NEEDS TO BE ASSIGNED
	static int height;
	// NEEDS TO BE ASSIGNED

	public static Collection<BufferedImage> convertToBufferedImageFromGoogleImageDataContainer (
	        Collection<GoogleImageDataContainer> collection)
	{
		Collection<BufferedImage> results = new ArrayList<BufferedImage>();
		for (GoogleImageDataContainer gidc : collection) {
			try {
				BufferedImage bimg = ImageIO.read(new URL(gidc.getUrl()));
 				results.add(bimg);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return results;
	}

	public static Collection<BufferedImage> rotateImages (Collection<BufferedImage> collection)
	{
		for (BufferedImage bi : collection)
			Scalr.rotate(bi, Rotation.valueOf(Double.toString(Math.random() * 90 - 45)), (BufferedImageOp[]) null);
		return collection;
	}

	public static Collection<BufferedImage> resizeImages (Collection<BufferedImage> collection)
	{
		for (BufferedImage bi : collection)
			Scalr.resize(bi, width, height, (BufferedImageOp[]) null);
		return collection;
	}

	public static Collection<BufferedImage> translateImages (Collection<BufferedImage> collection)
	{
		for (BufferedImage bi : collection)
			Scalr.apply(bi, (BufferedImageOp[]) null);
		return collection;
	}

	public static Collection<BufferedImage> addBorder (Collection<BufferedImage> collection)
	{
		JPanel gui = new JPanel(new BorderLayout());
		// to contrast the 'picture frame' border created below
		gui.setBorder(new LineBorder(Color.WHITE, 8));
		for (BufferedImage bi : collection) {
			JLabel l = new JLabel(new ImageIcon(bi));
			Border b = new LineBorder(Color.WHITE, 8);
			l.setBorder(b);
			gui.add(l);
		}
		return collection;
	}

	public BufferedImage compileCollage (Collection<BufferedImage> collection)
	{
		return new BufferedImage(0, 0, 0);
	}
}
