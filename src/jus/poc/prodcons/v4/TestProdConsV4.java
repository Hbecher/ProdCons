package jus.poc.prodcons.v4;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons.common.Consommateur;
import jus.poc.prodcons.common.Producteur;
import jus.poc.prodcons.v3.TestProdConsV3;

public class TestProdConsV4 extends TestProdConsV3
{
	public TestProdConsV4(Observateur observateur)
	{
		super(observateur);
	}

	public static void main(String[] args)
	{
		new TestProdConsV4(new Observateur()).start();
	}

	@Override
	protected Tampon newBuffer()
	{
		return new ProdConsV4(observateur, PRODUCERS, CONSUMERS, BUFFER_SIZE);
	}

	@Override
	protected Producteur newProducer() throws ControlException
	{
		return new ProducteurV4(observateur, (ProdConsV4) buffer);
	}

	@Override
	protected Consommateur newConsumer() throws ControlException
	{
		return new ConsommateurV4(observateur, (ProdConsV4) buffer);
	}
}
