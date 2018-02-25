package collageTools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Code to add borders, rotate pictures, and edit the locations.
 * 
 * 
 * @author Noah K. Mitsuhashi
 */

import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.imgscalr.AsyncScalr;
import org.imgscalr.Main;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Rotation;

import gimages.GoogleImageDataContainer;

public class ImageTools {
	
	int width;
	int height;
	
	public ImageTools(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public Collection<BufferedImage> convertToBufferedImageFromGoogleImageDataContainer(Collection<GoogleImageDataContainer> collection){
		Collection<BufferedImage> results = new ArrayList<BufferedImage>();
		for(GoogleImageDataContainer gidc:collection)
			try {
				results.add(ImageIO.read(new File(gidc.getUrl())));
			} catch (IOException e) {
				e.printStackTrace();
			}
		return results;
	}
	
	public Collection<BufferedImage> rotateImages(Collection<BufferedImage> collection){
		for(BufferedImage bi: collection)
			Scalr.rotate(bi, Rotation.valueOf(Double.toString(Math.random()*90-45)), (BufferedImageOp[])null);
		return collection;
	}
	
	public Collection<BufferedImage> resizeImages(Collection<BufferedImage> collection){
		for(BufferedImage bi: collection)
			Scalr.resize(bi, width, height, (BufferedImageOp[])null);
		return collection;
	}
	
	public Collection<BufferedImage> translateImages(Collection<BufferedImage> collection){
		for(BufferedImage bi: collection)
			Scalr.apply(bi, (BufferedImageOp[])null);
		return collection;
	}
	
	public Collection<BufferedImage> addBorder(Collection<BufferedImage> collection){
		JPanel gui = new JPanel(new BorderLayout());
        // to contrast the 'picture frame' border created below
        gui.setBorder(new LineBorder(Color.WHITE, 8));
        for(BufferedImage bi: collection) {
	        JLabel l = new JLabel(new ImageIcon(bi));
	        Border b = new LineBorder(Color.WHITE, 8);
	        l.setBorder(b);
	        gui.add(l);
        }
		return collection;
	}
	
	public BufferedImage compileCollage(Collection<BufferedImage> collection) {
		return new BufferedImage(0, 0, 0);
	}
}
