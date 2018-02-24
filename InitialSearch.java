package Servlets;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gimages.GoogleImageDataContainer;
import gimages.GoogleImagesClient;
import gimages.GoogleImagesClient.EmptyQueryException;
import gimages.GoogleImagesClient.NoApiKeyException;
import gimages.GoogleImagesClient.NoCseIdException;

/**
 * Servlet implementation class InitialSearch
 */
@WebServlet("/InitialSearch")
public class InitialSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InitialSearch() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("Gets here");
		//Take the search term and make an api call with it, take that information and send it to a jsp to display collage.
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String query = request.getParameter("topicInput");
		System.out.println("query = " + query);
		
		try {
			GoogleImagesClient giClient = new GoogleImagesClient("", "");

			Collection<GoogleImageDataContainer> imgData = giClient.getFirstNImages(query, 30);
			for (GoogleImageDataContainer img : imgData) {
				System.out.println(String.format(
				        "Type: %s\nDimensions: %s x %s\nSize (in bytes): %s\nUrl: %s\nThumbnail Url: %s\nThumbnail Dimensions: %s x %s\nDescription: %s\n\n",
				        img.getType(), img.getWidth(), img.getHeight(), img.getSize(), img.getUrl(),
				        img.getThumbnailUrl(), img.getThumbnailWidth(), img.getThumbnailHeight(),
				        img.getDescription()));
				
				System.out.println("returned image");
			}
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
		
		//doGet(request, response);
	}

}
