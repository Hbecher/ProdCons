package jus.poc.prodcons.v4;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;

public class TestProdConsV4 extends Simulateur
{
	private static final int PRODUCERS = DEFAULT_CONFIG.getProducers(), CONSUMERS = DEFAULT_CONFIG.getConsumers(), BUFFER_SIZE = DEFAULT_CONFIG.getBufferSize();
	private final ProdConsV4 tampon;

	public TestProdConsV4(Observateur observateur)
	{
		super(observateur);

		tampon = new ProdConsV4(observateur, PRODUCERS, CONSUMERS, BUFFER_SIZE);
	}

	public static void main(String[] args)
	{
		new TestProdConsV4(new Observateur()).start();
	}

	@Override
	protected final void run() throws Exception
	{
		observateur.init(PRODUCERS, CONSUMERS, BUFFER_SIZE);

		for(int i = 0; i < PRODUCERS; i++)
		{
			new ProducteurV4(observateur, tampon).start();
		}

		for(int i = 0; i < CONSUMERS; i++)
		{
			new ConsommateurV4(observateur, tampon).start();
		}
	}
}
