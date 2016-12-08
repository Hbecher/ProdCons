package jus.poc.prodcons.v4;

import static jus.poc.prodcons.message.MessageEnd.MESSAGE_END;

import jus.poc.prodcons.*;
import jus.poc.prodcons.common.Semaphore;
import jus.poc.prodcons.message.MessageTTL;
import jus.poc.prodcons.v3.ProdConsV3;

public class ProdConsV4 extends ProdConsV3
{
	private final Semaphore waitC = new Semaphore(0), waitP = new Semaphore(0);
	public ProdConsV4(Observateur observateur, int producers, int consumers, int bufferSize)
	{
		super(observateur, producers, consumers, bufferSize);
	}

	@Override
	protected Message getMessage(_Consommateur consommateur) throws ControlException
	{
		if(producers == 0 && messages == 0)
		{
			return MESSAGE_END;
		}

		MessageTTL message = (MessageTTL) buffer[nextRead];

		retrieve(consommateur, message);

		message.decTTL();

		if(message.getTTL() <= 0)
		{
			message.getSender();

			nextRead();


		}
		else
		{
			notEmpty.V();
		}

		print();

		return message;
	}

	@Override
	protected void putMessage(_Producteur producteur, Message message) throws ControlException
	{
		super.putMessage(producteur, message);

		print();
	}
}
