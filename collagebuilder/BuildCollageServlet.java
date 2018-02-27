package collagebuilder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import collagebuilder.GoogleImagesClient.EmptyQueryException;
import collagebuilder.GoogleImagesClient.NoApiKeyException;
import collagebuilder.GoogleImagesClient.NoCseIdException;

/**
 * Servlet implementation class BuildCollageServlet
 */
@WebServlet("/BuildCollageServlet")
public class BuildCollageServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String topic = request.getParameter("topic");
		
		ArrayList<GoogleImageDataContainer> imgData = new ArrayList<GoogleImageDataContainer>();
		
		// Get 30 images from Google Images
		try {
			GoogleImagesClient giClient = new GoogleImagesClient("", "");

			imgData = (ArrayList<GoogleImageDataContainer>)giClient.getFirstNImages(topic, 30);
//			for (GoogleImageDataContainer img : imgData) {
//				System.out.println(String.format(
//				        "Type: %s\nDimensions: %s x %s\nSize (in bytes): %s\nUrl: %s\nThumbnail Url: %s\nThumbnail Dimensions: %s x %s\nDescription: %s\n\n",
//				        img.getType(), img.getWidth(), img.getHeight(), img.getSize(), img.getUrl(),
//				        img.getThumbnailUrl(), img.getThumbnailWidth(), img.getThumbnailHeight(),
//				        img.getDescription()));
//			}
		}
		catch (NoCseIdException ncie) {
			ncie.printStackTrace();
		}
		catch (NoApiKeyException nake) {
			nake.printStackTrace();
		}
		catch (EmptyQueryException eqe) {
			eqe.printStackTrace();
		}
		
		if (imgData.size() == 30) {
			// We have 30 images as URLs from the internet, we need to download all of them as buffered images
			BufferedImage images[] = new BufferedImage[30];
			for (int i = 0; i < 30; ++i) {
				GoogleImageDataContainer currImg = imgData.get(i);
				URL url = new URL(currImg.getUrl());
				images[i] = ImageIO.read(url);
			}
			
			// Turn the 30 images into a collage
			Collage col = CollageGenerator.generateCollage(images, topic);
			
			// Serialize collage data into JSON
			Gson gson = new Gson();
			String collageAsJson = gson.toJson(col);
			
			System.out.println(col.getImageFilePath());
			
			// Send the JSON string back to the client
			PrintWriter out = response.getWriter();
			out.println(collageAsJson);
		}
		else {
			// We couldn't get 30 images with the query, send back an error to the client
			Collage error = new Collage("", 0, 0, topic, true);
			Gson gson = new Gson();
			String collageAsJson = gson.toJson(error);
			
			// Send the JSON string back to the client
			PrintWriter out = response.getWriter();
			out.println(collageAsJson);
		}
	}
}
