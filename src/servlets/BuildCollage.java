package servlets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import collageTools.CollageGenerator;
import collageTools.ImageTools;
import objects.Collage;
import gimages.GoogleImagesClient;
import gimages.GoogleImagesClient.EmptyQueryException;
import gimages.GoogleImagesClient.NoApiKeyException;
import gimages.GoogleImagesClient.NoCseIdException;
import gimages.GoogleImageDataContainer;
/**
 * Servlet implementation class BuildCollageServlet
 */
@WebServlet("/BuildCollage")
public class BuildCollage extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String topic = request.getParameter("topic");
		
		ArrayList<GoogleImageDataContainer> imgData = new ArrayList<GoogleImageDataContainer>();
		
		// Get 30 images from Google Images
		try {
			imgData = (ArrayList<GoogleImageDataContainer>)(new GoogleImagesClient("", "")).getFirstNImages(topic, 30);
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
			Collection<BufferedImage> images = ImageTools.convertToBufferedImageFromGoogleImageDataContainer(imgData);
			
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
