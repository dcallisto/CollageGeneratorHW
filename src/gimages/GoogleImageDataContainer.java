package gimages;

/**
 * 
 * @author Eddo W. Hintoso
 */
public class GoogleImageDataContainer
{
	private String type;
	private Integer width;
	private Integer height;
	private Integer size;
	private String url;
	private String thumbnailUrl;
	private Integer thumbnailWidth;
	private Integer thumbnailHeight;
	private String description;
	private String parentPage;

	GoogleImageDataContainer(String type, Integer width, Integer height, Integer size, String url, String thumbnailUrl,
	        Integer thumbnailWidth, Integer thumbnailHeight, String description, String parentPage)
	{
		this.type = type;
		this.width = width;
		this.height = height;
		this.size = size;
		this.url = url;
		this.thumbnailUrl = thumbnailUrl;
		this.thumbnailWidth = thumbnailWidth;
		this.thumbnailHeight = thumbnailHeight;
		this.description = description;
		this.parentPage = parentPage;
	}

	public String getType ()
	{
		return type;
	}

	public Integer getWidth ()
	{
		return width;
	}

	public Integer getHeight ()
	{
		return height;
	}

	public Integer getSize ()
	{
		return size;
	}

	public String getUrl ()
	{
		return url;
	}

	public String getThumbnailUrl ()
	{
		return thumbnailUrl;
	}

	public Integer getThumbnailWidth ()
	{
		return thumbnailWidth;
	}

	public Integer getThumbnailHeight ()
	{
		return thumbnailHeight;
	}

	public String getDescription ()
	{
		return description;
	}

	public String getParentPage ()
	{
		return parentPage;
	}

	public static class GoogleImageDataContainerBuilder
	{
		private String type;
		private Integer width;
		private Integer height;
		private Integer size;
		private String url;
		private String thumbnailUrl;
		private Integer thumbnailWidth;
		private Integer thumbnailHeight;
		private String description;
		private String parentPage;

		GoogleImageDataContainerBuilder()
		{
		}

		public GoogleImageDataContainerBuilder type (String type)
		{
			this.type = type;
			return this;
		}

		public GoogleImageDataContainerBuilder width (Integer width)
		{
			this.width = width;
			return this;
		}

		public GoogleImageDataContainerBuilder height (Integer height)
		{
			this.height = height;
			return this;
		}

		public GoogleImageDataContainerBuilder size (Integer size)
		{
			this.size = size;
			return this;
		}

		public GoogleImageDataContainerBuilder url (String url)
		{
			this.url = url;
			return this;
		}

		public GoogleImageDataContainerBuilder thumbnailUrl (String thumbnailUrl)
		{
			this.thumbnailUrl = thumbnailUrl;
			return this;
		}

		public GoogleImageDataContainerBuilder thumbnailWidth (Integer thumbnailWidth)
		{
			this.thumbnailWidth = thumbnailWidth;
			return this;
		}

		public GoogleImageDataContainerBuilder thumbnailHeight (Integer thumbnailHeight)
		{
			this.thumbnailHeight = thumbnailHeight;
			return this;
		}

		public GoogleImageDataContainerBuilder description (String description)
		{
			this.description = description;
			return this;
		}

		public GoogleImageDataContainerBuilder parentPage (String parentPage)
		{
			this.parentPage = parentPage;
			return this;
		}

		public GoogleImageDataContainer build ()
		{
			return new GoogleImageDataContainer(type, width, height, size, url, thumbnailUrl, thumbnailWidth,
			        thumbnailHeight, description, parentPage);
		}
	}
}
