package jus.poc.prodcons.v1;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons.common.Consommateur;
import jus.poc.prodcons.common.Producteur;
import jus.poc.prodcons.common.TestProdCons;

public class TestProdConsV1 extends TestProdCons
{
	public TestProdConsV1(Observateur observateur)
	{
		super(observateur);
	}

	public static void main(String[] args)
	{
		new TestProdConsV1(new Observateur()).start();
	}

	@Override
	protected Tampon newBuffer()
	{
		return new ProdConsV1(observateur, PRODUCERS, CONSUMERS, BUFFER_SIZE);
	}

	@Override
	protected Producteur newProducer() throws ControlException
	{
		return new ProducteurV1(observateur, (ProdConsV1) buffer);
	}

	@Override
	protected Consommateur newConsumer() throws ControlException
	{
		return new ConsommateurV1(observateur, (ProdConsV1) buffer);
	}
}