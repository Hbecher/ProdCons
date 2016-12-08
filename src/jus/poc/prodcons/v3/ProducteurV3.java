package jus.poc.prodcons.v3;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.v2.ProducteurV2;

public class ProducteurV3 extends ProducteurV2
{
	public ProducteurV3(Observateur observateur, ProdConsV3 tampon) throws ControlException
	{
		super(observateur, tampon);

		observateur.newProducteur(this);
	}

	@Override
	protected void production(Message message, int time) throws ControlException
	{
		observateur.productionMessage(this, message, time);
	}
}
