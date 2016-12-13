package jus.poc.prodcons.v4;

import static jus.poc.prodcons.message.MessageEnd.MESSAGE_END;

import jus.poc.prodcons.*;
import jus.poc.prodcons.common.Semaphore;
import jus.poc.prodcons.message.MessageTTL;

public class ProdConsV4 implements Tampon
{
	private final Observateur observateur;
	private final int bufferSize;
	private final Message[] buffer;
	private int producers, consumers;
	private int messages = 0, nextRead = 0, nextWrite = 0;
	private final Semaphore mutexIn = new Semaphore(1), mutexOut = new Semaphore(1), notEmpty = new Semaphore(0), notFull;

	public ProdConsV4(Observateur observateur, int producers, int consumers, int bufferSize)
	{
		this.observateur = observateur;
		this.bufferSize = bufferSize;
		this.producers = producers;
		this.consumers = consumers;
		buffer = new Message[bufferSize];
		notFull = new Semaphore(bufferSize);
	}

	@Override
	public void put(_Producteur producteur, Message message) throws Exception
	{
		notFull.acquire();
		mutexIn.acquire();

		buffer[nextWrite] = message;

		observateur.depotMessage(producteur, message);

		messages++;
		nextWrite = next(nextWrite);

		mutexIn.release();
		notEmpty.release();
	}

	@Override
	public Message get(_Consommateur consommateur) throws Exception
	{
		notEmpty.acquire();
		mutexOut.acquire();

		boolean wakeupC = false, wakeupP = false;
		Message message;

		if(producers == 0 && messages == 0)
		{
			message = MESSAGE_END;
		}
		else
		{
			MessageTTL ttl = (MessageTTL) buffer[nextRead];

			observateur.retraitMessage(consommateur, ttl);

			ttl.dec();

			if(ttl.isLast())
			{
				buffer[nextRead] = null;
				messages--;
				nextRead = next(nextRead);

				ttl.wakeup();

				wakeupP = true;
			}
			else
			{
				wakeupC = true;
			}

			message = ttl;
		}

		mutexOut.release();

		if(producers == 0 || wakeupC)
		{
			notEmpty.release();
		}

		if(wakeupP)
		{
			notFull.release();
		}

		return message;
	}

	@Override
	public int enAttente()
	{
		return messages;
	}

	@Override
	public int taille()
	{
		return bufferSize;
	}

	public synchronized void decConsumers()
	{
		consumers--;
	}

	public synchronized void decProducers()
	{
		producers--;

		if(producers == 0)
		{
			notEmpty.release();
		}
	}

	private int next(int n)
	{
		return (n + 1) % bufferSize;
	}
}
