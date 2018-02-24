package collageTools;

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

import org.imgscalr.AsyncScalr;
import org.imgscalr.Main;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Rotation;

import gimages.GoogleImageDataContainer;

public class ImageTools {
	
	
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
}
