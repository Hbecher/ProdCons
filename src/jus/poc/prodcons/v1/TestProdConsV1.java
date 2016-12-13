package jus.poc.prodcons.v1;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;

public class TestProdConsV1 extends Simulateur
{
	private static final int PRODUCERS = DEFAULT_CONFIG.getProducers(), CONSUMERS = DEFAULT_CONFIG.getConsumers(), BUFFER_SIZE = DEFAULT_CONFIG.getBufferSize();
	private final ProdConsV1 tampon;

	public TestProdConsV1(Observateur observateur)
	{
		super(observateur);

		tampon = new ProdConsV1(PRODUCERS, CONSUMERS, BUFFER_SIZE);
	}

	public static void main(String[] args)
	{
		new TestProdConsV1(new Observateur()).start();
	}

	@Override
	protected final void run() throws Exception
	{
		for(int i = 0; i < PRODUCERS; i++)
		{
			new ProducteurV1(observateur, tampon).start();
		}

		for(int i = 0; i < CONSUMERS; i++)
		{
			new ConsommateurV1(observateur, tampon).start();
		}
	}
}
