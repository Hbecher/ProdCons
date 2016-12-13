package jus.poc.prodcons.v5;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;

public class TestProdConsV5 extends Simulateur
{
	private static final int PRODUCERS = DEFAULT_CONFIG.getProducers(), CONSUMERS = DEFAULT_CONFIG.getConsumers(), BUFFER_SIZE = DEFAULT_CONFIG.getBufferSize();
	protected final ProdConsV5 tampon;

	public TestProdConsV5(Observateur observateur)
	{
		super(observateur);

		tampon = new ProdConsV5(observateur, PRODUCERS, CONSUMERS, BUFFER_SIZE);
	}

	public static void main(String[] args)
	{
		new TestProdConsV5(new Observateur()).start();
	}

	@Override
	protected final void run() throws Exception
	{
		observateur.init(PRODUCERS, CONSUMERS, BUFFER_SIZE);

		for(int i = 0; i < PRODUCERS; i++)
		{
			new ProducteurV5(observateur, tampon).start();
		}

		for(int i = 0; i < CONSUMERS; i++)
		{
			new ConsommateurV5(observateur, tampon).start();
		}
	}
}
