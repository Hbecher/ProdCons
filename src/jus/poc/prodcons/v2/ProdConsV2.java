package jus.poc.prodcons.v2;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.common.Semaphore;
import jus.poc.prodcons.v1.ProdConsV1;

public class ProdConsV2 extends ProdConsV1
{
	protected final Semaphore mutexIn = new Semaphore(1), mutexOut = new Semaphore(1), notEmpty = new Semaphore(0), notFull;

	public ProdConsV2(Observateur observateur, int producers, int consumers, int bufferSize)
	{
		super(observateur, producers, consumers, bufferSize);

		notFull = new Semaphore(bufferSize);
	}

	@Override
	public void put(_Producteur producteur, Message message) throws Exception
	{
		notFull.P();
		mutexIn.P();

		putMessage(producteur, message);

		mutexIn.V();
		notEmpty.V();
	}

	@Override
	public Message get(_Consommateur consommateur) throws Exception
	{
		notEmpty.P();
		mutexOut.P();

		try
		{
			return getMessage(consommateur);
		}
		finally
		{
			if(producers == 0)
			{
				notEmpty.V();
			}

			mutexOut.V();
			notFull.V();
		}
	}

	@Override
	public synchronized void decProducers()
	{
		producers--;

		if(producers == 0)
		{
			notEmpty.V();
		}
	}
}
