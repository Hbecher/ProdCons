package jus.poc.prodcons.v1;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.common.Consommateur;
import jus.poc.prodcons.common.Producteur;
import jus.poc.prodcons.common.TestProdCons;

public class TestProdConsV1 extends TestProdCons
{
	private final ProdConsV1 tampon;

	public TestProdConsV1(Observateur observateur)
	{
		super(observateur);

		this.tampon = new ProdConsV1(observateur, producers(), consumers(), DEFAULT_CONFIG.getBufferSize());
	}

	public static void main(String[] args)
	{
		new TestProdConsV1(new Observateur()).start();
	}

	@Override
	protected Producteur newProducer() throws ControlException
	{
		return new ProducteurV1(observateur, tampon);
	}

	@Override
	protected Consommateur newConsumer() throws ControlException
	{
		return new ConsommateurV1(observateur, tampon);
	}
}
