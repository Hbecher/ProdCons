package jus.poc.prodcons.v2;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons.common.Consommateur;
import jus.poc.prodcons.common.Producteur;
import jus.poc.prodcons.v1.TestProdConsV1;

public class TestProdConsV2 extends TestProdConsV1
{
	public TestProdConsV2(Observateur observateur)
	{
		super(observateur);
	}

	public static void main(String[] args)
	{
		new TestProdConsV2(new Observateur()).start();
	}

	@Override
	protected Tampon newBuffer()
	{
		return new ProdConsV2(observateur, PRODUCERS, CONSUMERS, BUFFER_SIZE);
	}

	@Override
	protected Producteur newProducer() throws ControlException
	{
		return new ProducteurV2(observateur, (ProdConsV2) buffer);
	}

	@Override
	protected Consommateur newConsumer() throws ControlException
	{
		return new ConsommateurV2(observateur, (ProdConsV2) buffer);
	}
}
