package jus.poc.prodcons.v3;

import jus.poc.prodcons.*;
import jus.poc.prodcons.v2.ProdConsV2;

public class ProdConsV3 extends ProdConsV2
{
	public ProdConsV3(Observateur observateur, int producers, int consumers, int bufferSize)
	{
		super(observateur, producers, consumers, bufferSize);
	}

	@Override
	protected void deposit(_Producteur producteur, Message message) throws ControlException
	{
		observateur.depotMessage(producteur, message);
	}

	@Override
	protected void retrieve(_Consommateur consommateur, Message message) throws ControlException
	{
		observateur.retraitMessage(consommateur, message);
	}
}
