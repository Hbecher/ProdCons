package jus.poc.prodcons.options;

import java.util.Properties;

public class Config
{
	private static final String DEFAULT_XML_FILE = "/jus/poc/prodcons/options/options.xml";
	public static final Config DEFAULT_CONFIG = new Config(DEFAULT_XML_FILE)
	{
		private boolean isInitialized = false;

		@Override
		public void init()
		{
			if(isInitialized)
			{
				System.err.println("Default config cannot be re-initialized!");
			}

			super.init();

			isInitialized = true;
		}
	};

	static
	{
		DEFAULT_CONFIG.init();
	}

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

	public int getConsumers()
	{
		return Integer.parseInt(config.getProperty("nbProd"));
	}

	public int getProducers()
	{
		return Integer.parseInt(config.getProperty("nbCons"));
	}

	public int getBufferSize()
	{
		return Integer.parseInt(config.getProperty("nbBuffer"));
	}

	public int getProdTimeMean()
	{
		return Integer.parseInt(config.getProperty("tempsMoyenProduction"));
	}

	public int getProdTimeDev()
	{
		return Integer.parseInt(config.getProperty("deviationTempsMoyenProduction"));
	}

	public int getConsTimeMean()
	{
		return Integer.parseInt(config.getProperty("tempsMoyenConsommation"));
	}

	public int getConsTimeDev()
	{
		return Integer.parseInt(config.getProperty("deviationTempsMoyenConsommation"));
	}

	public int getProdMessagesMean()
	{
		return Integer.parseInt(config.getProperty("nombreMoyenDeProduction"));
	}

	public int getProdMessagesDev()
	{
		return Integer.parseInt(config.getProperty("deviationNombreMoyenDeProduction"));
	}

	public int getConsMessagesMean()
	{
		return Integer.parseInt(config.getProperty("nombreMoyenNbExemplaire"));
	}

	public int getConsMessagesDev()
	{
		return Integer.parseInt(config.getProperty("deviationNombreMoyenNbExemplaire"));
	}

	public boolean isPrintingEnabled()
	{
		return Boolean.parseBoolean(config.getProperty("impressions"));
	}

	public boolean isLoggingEnabled()
	{
		return Boolean.parseBoolean(config.getProperty("journaux"));
	}
}
