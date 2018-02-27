package gimages;

import java.util.Collection;

import gimages.GoogleImagesClient.EmptyQueryException;
import gimages.GoogleImagesClient.NoApiKeyException;
import gimages.GoogleImagesClient.NoCseIdException;

public class UsageExample
{
	public static void main (String[] args)
	{
		try {
			String cseId = "015959703164177076712:ybjv_gscpea";
			String apiKey = "AIzaSyBr1C_5QJoGUgpFdRO9zitv8LZd_dbQb0c";
			GoogleImagesClient giClient = new GoogleImagesClient(cseId, apiKey);

			Collection<GoogleImageDataContainer> imgData = giClient.getFirstNImages("floweriza", 30);
			for (GoogleImageDataContainer img : imgData) {
				System.out.println(String.format(
				        "Type: %s\nDimensions: %s x %s\nSize (in bytes): %s\nUrl: %s\nThumbnail Url: %s\nThumbnail Dimensions: %s x %s\nDescription: %s\n\n",
				        img.getType(), img.getWidth(), img.getHeight(), img.getSize(), img.getUrl(),
				        img.getThumbnailUrl(), img.getThumbnailWidth(), img.getThumbnailHeight(),
				        img.getDescription()));
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
	}
}
