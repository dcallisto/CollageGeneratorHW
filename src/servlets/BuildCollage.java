package servlets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import collageTools.CollageGenerator;
import objects.Collage;
import gimages.GoogleImagesClient;
import gimages.GoogleImagesClient.EmptyQueryException;
import gimages.GoogleImagesClient.NoApiKeyException;
import gimages.GoogleImagesClient.NoCseIdException;
import gimages.GoogleImageDataContainer;

/**
 * Servlet implementation class BuildCollageServlet
 * 
 * @author
 */
@WebServlet("/BuildCollage")
public class BuildCollage extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static final int maxNumImages = 30;
	private static final String cseId = "015959703164177076712:ybjv_gscpea";
	private static final String apiKey = "AIzaSyBr1C_5QJoGUgpFdRO9zitv8LZd_dbQb0c";
	private static final Gson gson = new Gson();

	private static String getCollageAsJson (String topic)
	{
		// Get 30 images from Google Images
		Collection<GoogleImageDataContainer> imageDataContainers = null;
		try {
			imageDataContainers = new GoogleImagesClient(cseId, apiKey).getFirstNImages(topic, maxNumImages);
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

		// XXX
		System.out.println(imageDataContainers.size());

		String collageAsJson;
		if (imageDataContainers.size() == maxNumImages) {
			Collection<BufferedImage> bufferedImages = imageDataContainers.stream()
			        .map(GoogleImageDataContainer::getBufferedImage).collect(Collectors.toCollection(ArrayList::new));

			// Turn the 30 images into a collage
			Collage col = CollageGenerator.generateCollage(bufferedImages, topic);

			// Serialize collage data into JSON
			collageAsJson = gson.toJson(col);

			// XXX
			System.out.println(col.getImageFilePath());
		}
		else {
			// We couldn't get 30 images with the query, send back an error to the client
			Collage error = new Collage("", 0, 0, topic, true);
			collageAsJson = gson.toJson(error);
		}

		return collageAsJson;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service (HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException
	{
		String topic = request.getParameter("topic");
		String collageAsJson = getCollageAsJson(topic);

		// Send the JSON string back to the client
		PrintWriter out = response.getWriter();
		out.println(collageAsJson);
	}

	public static void main (String[] args)
	{
		String topic = "dogs";
		String collageAsJson = getCollageAsJson(topic);
		System.out.println(collageAsJson);
	}
}
