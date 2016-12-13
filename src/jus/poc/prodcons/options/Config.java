package jus.poc.prodcons.options;

import java.util.Properties;

/**
 * Cette classe contient les options pouvant être lues depuis le fichiers options.xml.<br />
 * Une configuration par défaut (celle présente dans le paquetage {@code jus.proc.prodcons.options}) est fournie : {@link Config#DEFAULT_CONFIG}.
 */
public class Config
{
	private static final String DEFAULT_XML_FILE = "/jus/poc/prodcons/options/options.xml";
	/**
	 * Configuration par défaut
	 */
	public static final Config DEFAULT_CONFIG = new Config(DEFAULT_XML_FILE)
	{
		private boolean isInitialized = false;

		{
			init();
		}

		@Override
		public void init()
		{
			if(isInitialized)
			{
				System.err.println("Default config cannot be re-initialized!");

				return;
			}

			super.init();

			isInitialized = true;
		}
	};

	private final String configFile;
	private final Properties config = new Properties();

	public Config(String configFile)
	{
		this.configFile = configFile;
	}

	public void init()
	{
		config.clear();

		try
		{
			config.loadFromXML(getClass().getResourceAsStream(configFile));
		}
		catch(Exception e)
		{
			throw new RuntimeException("Failed to read configuration");
		}
	}

	private int getInt(String key, int def)
	{
		String value = config.getProperty(key);

		return value == null ? def : Integer.parseInt(value);
	}

	private boolean getBoolean(String key, boolean def)
	{
		String value = config.getProperty(key);

		return value == null ? def : Boolean.parseBoolean(value);
	}

	public int getProducers()
	{
		return getInt("nbProd", 1);
	}

	public int getConsumers()
	{
		return getInt("nbCons", 10);
	}

	public int getBufferSize()
	{
		return getInt("nbBuffer", 1);
	}

	public int getProdTimeMean()
	{
		return getInt("tempsMoyenProduction", 10);
	}

	public int getProdTimeDev()
	{
		return getInt("deviationTempsMoyenProduction", 1);
	}

	public int getConsTimeMean()
	{
		return getInt("tempsMoyenConsommation", 10);
	}

	public int getConsTimeDev()
	{
		return getInt("deviationTempsMoyenConsommation", 1);
	}

	public int getProdMessagesMean()
	{
		return getInt("nombreMoyenDeProduction", 5);
	}

	public int getProdMessagesDev()
	{
		return getInt("deviationNombreMoyenDeProduction", 1);
	}

	public int getConsMessagesMean()
	{
		return getInt("nombreMoyenNbExemplaire", 5);
	}

	public int getConsMessagesDev()
	{
		return getInt("deviationNombreMoyenNbExemplaire", 3);
	}

	public boolean isPrintingEnabled()
	{
		return getBoolean("impressions", true);
	}
}
