package jus.poc.prodcons.v2;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;

public class TestProdConsV2 extends Simulateur
{
	private static final int PRODUCERS = DEFAULT_CONFIG.getProducers(), CONSUMERS = DEFAULT_CONFIG.getConsumers(), BUFFER_SIZE = DEFAULT_CONFIG.getBufferSize();
	private final ProdConsV2 tampon;

	public TestProdConsV2(Observateur observateur)
	{
		super(observateur);

		tampon = new ProdConsV2(PRODUCERS, CONSUMERS, BUFFER_SIZE);
	}

	public static void main(String[] args)
	{
		new TestProdConsV2(new Observateur()).start();
	}

	@Override
	protected final void run() throws Exception
	{
		for(int i = 0; i < PRODUCERS; i++)
		{
			new ProducteurV2(observateur, tampon).start();
		}

		for(int i = 0; i < CONSUMERS; i++)
		{
			new ConsommateurV2(observateur, tampon).start();
		}
	}
}
