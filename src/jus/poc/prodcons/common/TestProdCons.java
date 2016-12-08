package jus.poc.prodcons.common;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;

public abstract class TestProdCons extends Simulateur
{
	private final Producteur[] producers = new Producteur[DEFAULT_CONFIG.getProducers()];
	private final Consommateur[] consumers = new Consommateur[DEFAULT_CONFIG.getConsumers()];

	public TestProdCons(Observateur observateur)
	{
		super(observateur);
	}

	@Override
	protected final void run() throws Exception
	{
		init();

		for(int i = 0; i < producers.length; i++)
		{
			Producteur producteur = newProducer();
			producers[i] = producteur;
			producteur.start();
		}

		for(int i = 0; i < consumers.length; i++)
		{
			Consommateur consommateur = newConsumer();
			consumers[i] = consommateur;
			consommateur.start();
		}
	}

	protected final int producers()
	{
		return producers.length;
	}

	protected final int consumers()
	{
		return consumers.length;
	}

	protected abstract Producteur newProducer() throws ControlException;

	protected abstract Consommateur newConsumer() throws ControlException;

	protected void init() throws ControlException
	{
	}
}
