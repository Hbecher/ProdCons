package jus.poc.prodcons.v3;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons.common.Consommateur;
import jus.poc.prodcons.common.Producteur;
import jus.poc.prodcons.v2.TestProdConsV2;

public class TestProdConsV3 extends TestProdConsV2
{
	public TestProdConsV3(Observateur observateur)
	{
		super(observateur);
	}

	public static void main(String[] args)
	{
		new TestProdConsV3(new Observateur()).start();
	}

	@Override
	protected Tampon newBuffer()
	{
		return new ProdConsV3(observateur, PRODUCERS, CONSUMERS, BUFFER_SIZE);
	}

	@Override
	protected Producteur newProducer() throws ControlException
	{
		return new ProducteurV3(observateur, (ProdConsV3) buffer);
	}

	@Override
	protected Consommateur newConsumer() throws ControlException
	{
		return new ConsommateurV3(observateur, (ProdConsV3) buffer);
	}

	@Override
	protected void init() throws ControlException
	{
		observateur.init(PRODUCERS, CONSUMERS, buffer.taille());
	}
}
