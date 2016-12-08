package jus.poc.prodcons.v2;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.common.Consommateur;
import jus.poc.prodcons.common.Producteur;
import jus.poc.prodcons.common.TestProdCons;

public class TestProdConsV2 extends TestProdCons
{
	private final ProdConsV2 tampon;

	public TestProdConsV2(Observateur observateur)
	{
		super(observateur);

		tampon = new ProdConsV2(observateur, producers(), consumers(), DEFAULT_CONFIG.getBufferSize());
	}

	public static void main(String[] args)
	{
		new TestProdConsV2(new Observateur()).start();
	}

	@Override
	protected Producteur newProducer() throws ControlException
	{
		return new ProducteurV2(observateur, tampon);
	}

	@Override
	protected Consommateur newConsumer() throws ControlException
	{
		return new ConsommateurV2(observateur, tampon);
	}
}
