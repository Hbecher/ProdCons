package jus.poc.prodcons.v3;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.v2.ConsommateurV2;

public class ConsommateurV3 extends ConsommateurV2
{
	public ConsommateurV3(Observateur observateur, ProdConsV3 tampon) throws ControlException
	{
		super(observateur, tampon);

		observateur.newConsommateur(this);
	}

	@Override
	protected void consumption(Message message, int time) throws ControlException
	{
		observateur.consommationMessage(this, message, time);
	}
}
