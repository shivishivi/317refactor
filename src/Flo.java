public final class Flo
{

	public static void unpackConfig(StreamLoader streamLoader)
	{
		Stream stream = new Stream(streamLoader.getDataForName("flo.dat"));
		int cacheSize = stream.readUnsignedWord();
		if (cache == null)
			cache = new Flo[cacheSize];
		for (int j = 0; j < cacheSize; j++)
		{
			if (cache[j] == null)
				cache[j] = new Flo();
			cache[j].readValues(stream);
		}

	}

	private void readValues(Stream stream)
	{
		do
		{
			int i = stream.readUnsignedByte();
			boolean dummy;
			if (i == 0)
				return;
			else if (i == 1)
			{
				anInt390 = stream.read3Bytes();
				method262(anInt390);
			}
			else if (i == 2)
				anInt391 = stream.readUnsignedByte();
			else if (i == 3)
				dummy = true;
			else if (i == 5)
				aBoolean393 = false;
			else if (i == 6)
				stream.readString();
			else if (i == 7)
			{
				int j = hue;
				int k = saturation;
				int l = lightness;
				int i1 = anInt397;
				int j1 = stream.read3Bytes();
				method262(j1);
				hue = j;
				saturation = k;
				lightness = l;
				anInt397 = i1;
				anInt398 = i1;
			}
			else
			{
				System.out.println("Error unrecognised config code: " + i);
			}
		} while (true);
	}

	private void method262(int rgb)
	{
		// Extract the individual RGB values from the colour
		double red = (double) (rgb >> 16 & 0xff) / 256D;
		double green = (double) (rgb >> 8 & 0xff) / 256D;
		double blue = (double) (rgb & 0xff) / 256D;
		
		// Calculate the darkest colour from the three
		double cMin = red;
		if (green < cMin)
			cMin = green;
		if (blue < cMin)
			cMin = blue;
		
		// Calculate the lightest colour from the three
		double cMax = red;
		if (green > cMax)
			cMax = green;
		if (blue > cMax)
			cMax = blue;
		
		// Define the hue, saturation and lightness, and calculate the lightness
		double hue = 0.0D;
		double saturation = 0.0D;
		double lightness = (cMin + cMax) / 2D;
		
		if (cMin != cMax)
		{
			// Calculate the saturation
			if (lightness < 0.5D)
				saturation = (cMax - cMin) / (cMax + cMin);
			if (lightness >= 0.5D)
				saturation = (cMax - cMin) / (2D - cMax - cMin);
			
			// Calculate the hue
			if (red == cMax)
				hue = (green - blue) / (cMax - cMin);
			else if (green == cMax)
				hue = 2D + (blue - red) / (cMax - cMin);
			else if (blue == cMax)
				hue = 4D + (red - green) / (cMax - cMin);
		}
		hue /= 6D;
		
		// Finalise the hue, saturation and lightness (make them correct HSL values)
		this.hue = (int) (hue * 256D);
		this.saturation = (int) (saturation * 256D);
		this.lightness = (int) (lightness * 256D);
		
		// Check that the saturation is not lower than 0 or higher than 255
		if (this.saturation < 0)
			this.saturation = 0;
		else if (this.saturation > 255)
			this.saturation = 255;
		
		// Check that the lightness is not lower than 0 or higher than 255
		if (this.lightness < 0)
			this.lightness = 0;
		else if (this.lightness > 255)
			this.lightness = 255;
		
		if (lightness > 0.5D)
			anInt398 = (int) ((1.0D - lightness) * saturation * 512D);
		else
			anInt398 = (int) (lightness * saturation * 512D);
		if (anInt398 < 1)
			anInt398 = 1;
		anInt397 = (int) (hue * (double) anInt398);
		
		// Randomise the hue to affect colour picker bots
		int hueOffset = (this.hue + (int) (Math.random() * 16D)) - 8;
		if (hueOffset < 0)
			hueOffset = 0;
		else if (hueOffset > 255)
			hueOffset = 255;
		
		// Randomise the saturation to affect colour picker bots
		int saturationOffset = (this.saturation + (int) (Math.random() * 48D)) - 24;
		if (saturationOffset < 0)
			saturationOffset = 0;
		else if (saturationOffset > 255)
			saturationOffset = 255;
		
		// Randomise the lightness to affect colour picker bots
		int lightnessOffset = (this.lightness + (int) (Math.random() * 48D)) - 24;
		if (lightnessOffset < 0)
			lightnessOffset = 0;
		else if (lightnessOffset > 255)
			lightnessOffset = 255;
		
		hslColour = packHSL(hueOffset, saturationOffset, lightnessOffset);
	}

	private int packHSL(int hue, int saturation, int lightness)
	{
		if (lightness > 179)
			saturation = (saturation / 2);
		return (hue / 4 << 10) + (saturation / 32 << 7) + lightness / 2;
	}

	private Flo()
	{
		anInt391 = -1;
		aBoolean393 = true;
	}

	public static Flo cache[];
	public int anInt390;
	public int anInt391;
	public boolean aBoolean393;
	public int hue;
	public int saturation;
	public int lightness;
	public int anInt397;
	public int anInt398;
	public int hslColour;
}
