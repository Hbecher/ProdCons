package jus.poc.prodcons.v3;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.common.Consommateur;
import jus.poc.prodcons.common.Producteur;
import jus.poc.prodcons.v2.TestProdConsV2;

public class TestProdConsV3 extends TestProdConsV2
{
	private final ProdConsV3 tampon;

	public TestProdConsV3(Observateur observateur)
	{
		super(observateur);

		tampon = new ProdConsV3(observateur, producers(), consumers(), DEFAULT_CONFIG.getBufferSize());
	}

	public static void main(String[] args)
	{
		new TestProdConsV3(new Observateur()).start();
	}

	@Override
	protected Producteur newProducer() throws ControlException
	{
		return new ProducteurV3(observateur, tampon);
	}

	@Override
	protected Consommateur newConsumer() throws ControlException
	{
		return new ConsommateurV3(observateur, tampon);
	}

	@Override
	protected void init() throws ControlException
	{
		observateur.init(producers(), consumers(), tampon.taille());
	}
}
