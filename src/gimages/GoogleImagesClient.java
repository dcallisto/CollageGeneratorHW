package gimages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import gimages.GoogleImagesSearchOptions.GoogleImagesSearchOptionsBuilder;
import gimages.GoogleImageDataContainer.GoogleImageDataContainerBuilder;

/**
 * A Java port of the NPM package `google-images'.
 * 
 * Source project can be found here:
 * https://github.com/vadimdemedes/google-images/
 * 
 * @author Eddo W. Hintoso
 */
public class GoogleImagesClient
{
	private final static String endpoint = "https://www.googleapis.com";
	private String cseId;
	private String apiKey;

	protected static class NoCseIdException extends Exception
	{
		private static final long serialVersionUID = -3173644237979556821L;

		NoCseIdException(String msg)
		{
			super(msg);
		}
	}

	protected static class NoApiKeyException extends Exception
	{
		private static final long serialVersionUID = 2618088795834573589L;

		NoApiKeyException(String msg)
		{
			super(msg);
		}
	}

	protected static class EmptyQueryException extends Exception
	{
		private static final long serialVersionUID = -5086294946483449728L;

		EmptyQueryException(String msg)
		{
			super(msg);
		}
	}

	protected static class InsufficientImageItemsException extends RuntimeException
	{
		private static final long serialVersionUID = 687842118573385020L;

		public InsufficientImageItemsException(String msg)
		{
			super(msg);
		}
	}

	protected static class SearchFailureException extends RuntimeException
	{
		private static final long serialVersionUID = -8159817905625855197L;

		public SearchFailureException(String msg)
		{
			super(msg);
		}
	}

	public GoogleImagesClient(String cseId, String apiKey) throws NoCseIdException, NoApiKeyException
	{
		// XXX
		cseId = "015959703164177076712:ybjv_gscpea";
		apiKey = "AIzaSyBr1C_5QJoGUgpFdRO9zitv8LZd_dbQb0c";

		if (cseId == null || cseId.isEmpty()) {
			throw new NoCseIdException("Expected a Custom Search Engine ID.");
		}

		if (apiKey == null || apiKey.isEmpty()) {
			throw new NoApiKeyException("Expected an API key.");
		}

		this.cseId = cseId;
		this.apiKey = apiKey;
	}

	public Collection<GoogleImageDataContainer> getFirstNImages (String query, final Integer n)
	        throws EmptyQueryException, InsufficientImageItemsException
	{
		GoogleImagesSearchOptions options = new GoogleImagesSearchOptionsBuilder().build();
		return getFirstNImages(query, n, options);
	}

	public Collection<GoogleImageDataContainer> getFirstNImages (String query, final Integer n,
	        GoogleImagesSearchOptions options) throws EmptyQueryException, InsufficientImageItemsException
	{
		assert (n > 0);
		JsonObject searchResponse = searchImages(query, options);

		if (searchResponse.has("errorCode")) {
			throw new SearchFailureException(
			        String.format("Search failure; error code: %s", searchResponse.get("errorCode").getAsInt()));
		}

		// get the total results
		JsonObject queriesData = searchResponse.getAsJsonObject("queries");
		JsonObject requestData = queriesData.getAsJsonArray("request").get(0).getAsJsonObject();
		Long totalResults = requestData.get("totalResults").getAsLong();

		// throw exception if insufficient images are found
		Boolean insufficientImages = totalResults < n;
		if (insufficientImages) {
			throw new InsufficientImageItemsException("Insufficient image items found.");
		}
		// having enough images implies there are the attributes "items"
		assert (searchResponse.has("items"));

		// Store the first `n` images in a JsonArray
		JsonArray imageItems = new JsonArray();

		// Initialize values -- query options need to be updated each iteration of the
		// loop to reflect an increment in search results pagination
		JsonArray currentImageItems;
		JsonObject nextPageData;
		Long nextStartingIndex;
		GoogleImagesSearchOptions nextPageSearchOptions;

		// Adding `n` items into `imageItems`
		// Each item in `imageItems` is of type JsonElement
		while (true) {
			currentImageItems = searchResponse.getAsJsonArray("items");
			if (imageItems.size() + currentImageItems.size() > n) {
				// add to all the image items with slow loop
				for (int i = 0; i < n - imageItems.size(); i++) {
					imageItems.add(currentImageItems.get(i));
				}
			}
			else {
				// else, just add everything conveniently
				imageItems.addAll(currentImageItems);
			}

			// break if there are enough images
			if (imageItems.size() >= n) break;

			// search with option to specify starting index
			nextPageData = queriesData.getAsJsonArray("nextPage").get(0).getAsJsonObject();
			nextStartingIndex = nextPageData.get("startIndex").getAsLong();
			nextPageSearchOptions = new GoogleImagesSearchOptionsBuilder().page(nextStartingIndex).build();
			// update response and queries data
			searchResponse = searchImages(query, nextPageSearchOptions);
			queriesData = searchResponse.getAsJsonObject("queries");
		}

		// Convert JsonArray to Collection<GoogleImageDataContainer>
		Collection<GoogleImageDataContainer> imageDataContainers = new ArrayList<>();
		JsonObject imageDataObject, img;
		GoogleImageDataContainer imageDataContainer;
		String imageType, imageUrl, imageThumbnailUrl, imageDescription;
		Integer imageWidth, imageHeight, imageSize, imageThumbnailWidth, imageThumbnailHeight;
		for (JsonElement imageItem : imageItems) {
			imageDataObject = imageItem.getAsJsonObject();
			img = imageDataObject.get("image").getAsJsonObject();
			imageType = imageDataObject.get("mime").getAsString();
			imageWidth = img.get("width").getAsInt();
			imageHeight = img.get("height").getAsInt();
			imageSize = img.get("byteSize").getAsInt();
			imageUrl = imageDataObject.get("link").getAsString();
			imageThumbnailUrl = img.get("thumbnailLink").getAsString();
			imageThumbnailWidth = img.get("thumbnailWidth").getAsInt();
			imageThumbnailHeight = img.get("thumbnailHeight").getAsInt();
			imageDescription = imageDataObject.get("snippet").getAsString();

			// build the container
			imageDataContainer = new GoogleImageDataContainerBuilder().type(imageType).width(imageWidth)
			        .height(imageHeight).size(imageSize).url(imageUrl).thumbnailUrl(imageThumbnailUrl)
			        .thumbnailWidth(imageThumbnailWidth).thumbnailHeight(imageThumbnailHeight)
			        .description(imageDescription).build();
			// add to collection for easy java data consumption
			imageDataContainers.add(imageDataContainer);
		}

		// this must be held true
		assert (imageDataContainers.size() == n);
		return imageDataContainers;
	}

	private JsonObject searchImages (String query, GoogleImagesSearchOptions options) throws EmptyQueryException
	{
		if (query == null || query.isEmpty()) {
			throw new EmptyQueryException("Expected a query.");
		}

		String urlString = buildUrlString(query, options);

		HttpURLConnection conn = null;
		JsonObject jsonResponse = new JsonObject();
		try {
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			int respStatus = conn.getResponseCode();

			if (respStatus == 200) { // OK
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				String inputLine;
				StringBuffer jsonContent = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					jsonContent.append(inputLine);
				}
				in.close();
				System.out.println(jsonContent);

				jsonResponse = new JsonParser().parse(jsonContent.toString()).getAsJsonObject();
			}
			else {
				jsonResponse.addProperty("errorCode", respStatus);
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		finally {
			// close the connection
			if (conn != null) {
				conn.disconnect();
			}
		}

		return jsonResponse;
	}

	public String buildUrlString (String query, GoogleImagesSearchOptions options)
	{
		return String.format("%s/customsearch/v1?%s", GoogleImagesClient.endpoint, this.buildQuery(query, options));
	}

	private String buildQuery (String query, GoogleImagesSearchOptions options)
	{
		Map<String, String> queryParameters = new HashMap<>();
		queryParameters.put("q", query.replaceAll("\\s+", "+"));
		queryParameters.put("searchType", "image");
		queryParameters.put("cx", cseId);
		queryParameters.put("key", apiKey);

		if (options.page != null) queryParameters.put("start", options.page.toString());
		if (options.sizeCategory != null) queryParameters.put("imgSize", options.sizeCategory);
		if (options.type != null) queryParameters.put("imgType", options.type);
		if (options.dominantColor != null) queryParameters.put("dominantColor", options.dominantColor);
		if (options.colorType != null) queryParameters.put("colorType", options.colorType);
		if (options.safetyLevel != null) queryParameters.put("safe", options.safetyLevel);

		List<String> urlParamsList = new ArrayList<>();
		// build into string
		for (String parameter : queryParameters.keySet()) {
			try {
				urlParamsList.add(
				        String.format("%s=%s", parameter, URLEncoder.encode(queryParameters.get(parameter), "UTF-8")));
			}
			catch (UnsupportedEncodingException uee) {
				uee.printStackTrace();
			}
		}
		return String.join("&", urlParamsList);
	}

}
