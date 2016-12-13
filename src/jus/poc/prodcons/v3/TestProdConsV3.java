package jus.poc.prodcons.v3;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;

public class TestProdConsV3 extends Simulateur
{
	private static final int PRODUCERS = DEFAULT_CONFIG.getProducers(), CONSUMERS = DEFAULT_CONFIG.getConsumers(), BUFFER_SIZE = DEFAULT_CONFIG.getBufferSize();
	private final ProdConsV3 tampon;

	public TestProdConsV3(Observateur observateur)
	{
		super(observateur);

		tampon = new ProdConsV3(observateur, PRODUCERS, CONSUMERS, BUFFER_SIZE);
	}

	public static void main(String[] args)
	{
		new TestProdConsV3(new Observateur()).start();
	}

	@Override
	protected final void run() throws Exception
	{
		observateur.init(PRODUCERS, CONSUMERS, BUFFER_SIZE);

		for(int i = 0; i < PRODUCERS; i++)
		{
			new ProducteurV3(observateur, tampon).start();
		}

		for(int i = 0; i < CONSUMERS; i++)
		{
			new ConsommateurV3(observateur, tampon).start();
		}
	}
}
