package jus.poc.prodcons.v4;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.message.MessageTTL;
import jus.poc.prodcons.v3.ProducteurV3;

public class ProducteurV4 extends ProducteurV3
{
	public ProducteurV4(Observateur observateur, ProdConsV4 tampon) throws ControlException
	{
		super(observateur, tampon);
	}

	@Override
	protected Message newMessage(int id)
	{
		return new MessageTTL(this, id);
	}
}
