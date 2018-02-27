package collagebuilder;

public class GoogleImagesSearchOptions
{
	protected Long page;
	protected String sizeCategory;
	protected String type;
	protected String dominantColor;
	protected String colorType;
	protected String safetyLevel;

	GoogleImagesSearchOptions(Long page, String sizeCategory, String type, String dominantColor, String colorType,
	        String safetyLevel)
	{
		this.page = page;
		this.sizeCategory = sizeCategory;
		this.type = type;
		this.dominantColor = dominantColor;
		this.colorType = colorType;
		this.safetyLevel = safetyLevel;
	}

	public static class GoogleImagesSearchOptionsBuilder
	{
		private Long page;
		private String sizeCategory;
		private String type;
		private String dominantColor;
		private String colorType;
		private String safetyLevel;

		GoogleImagesSearchOptionsBuilder()
		{
		};

		public GoogleImagesSearchOptionsBuilder page (Long page)
		{
			this.page = page;
			return this;
		}

		public GoogleImagesSearchOptionsBuilder sizeCategory (String sizeCategory)
		{
			this.sizeCategory = sizeCategory;
			return this;
		}

		public GoogleImagesSearchOptionsBuilder type (String type)
		{
			this.type = type;
			return this;
		}

		public GoogleImagesSearchOptionsBuilder dominantColor (String dominantColor)
		{
			this.dominantColor = dominantColor;
			return this;
		}

		public GoogleImagesSearchOptionsBuilder colorType (String colorType)
		{
			this.colorType = colorType;
			return this;
		}

		public GoogleImagesSearchOptionsBuilder safetyLevel (String safetyLevel)
		{
			this.safetyLevel = safetyLevel;
			return this;
		}

		public GoogleImagesSearchOptions build ()
		{
			return new GoogleImagesSearchOptions(page, sizeCategory, type, dominantColor, colorType, safetyLevel);
		}
	}
}
