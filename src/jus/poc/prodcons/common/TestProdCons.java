package jus.poc.prodcons.common;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;
import jus.poc.prodcons.Tampon;

public abstract class TestProdCons extends Simulateur
{
	protected static final int PRODUCERS = DEFAULT_CONFIG.getProducers(), CONSUMERS = DEFAULT_CONFIG.getConsumers(), BUFFER_SIZE = DEFAULT_CONFIG.getBufferSize();
	protected final Tampon buffer = newBuffer();

	public TestProdCons(Observateur observateur)
	{
		super(observateur);
	}

	@Override
	protected final void run() throws Exception
	{
		init();

		for(int i = 0; i < PRODUCERS; i++)
		{
			newProducer().start();
		}

		for(int i = 0; i < CONSUMERS; i++)
		{
			newConsumer().start();
		}
	}

	protected abstract Tampon newBuffer();

	protected abstract Producteur newProducer() throws ControlException;

	protected abstract Consommateur newConsumer() throws ControlException;

	protected void init() throws ControlException
	{
	}
}
