package jus.poc.prodcons.v1;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.common.ProdCons;

public class ProdConsV1 extends ProdCons
{
	public ProdConsV1(Observateur observateur, int producers, int consumers, int bufferSize)
	{
		super(observateur, producers, consumers, bufferSize);
	}

	@Override
	public synchronized void put(_Producteur producteur, Message message) throws Exception
	{
		while(enAttente() == taille())
		{
			wait();
		}

		putMessage(producteur, message);

		notifyAll();
	}

	@Override
	public synchronized Message get(_Consommateur consommateur) throws Exception
	{
		while(enAttente() == 0 && producers() > 0)
		{
			wait();
		}

		try
		{
			return getMessage(consommateur);
		}
		finally
		{
			notifyAll();
		}
	}
}
