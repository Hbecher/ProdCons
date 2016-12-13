package jus.poc.prodcons.v5;

import static jus.poc.prodcons.message.MessageEnd.MESSAGE_END;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jus.poc.prodcons.*;

public class ProdConsV5 implements Tampon
{
	private final Observateur observateur;
	private final int bufferSize;
	private final Message[] buffer;
	private int producers, consumers;
	private int messages = 0, nextRead = 0, nextWrite = 0;
	private final Lock lock = new ReentrantLock();
	protected final Condition notEmpty = lock.newCondition(), notFull = lock.newCondition();

	public ProdConsV5(Observateur observateur, int producers, int consumers, int bufferSize)
	{
		this.observateur = observateur;
		this.bufferSize = bufferSize;
		this.producers = producers;
		this.consumers = consumers;
		buffer = new Message[bufferSize];
	}

	@Override
	public void put(_Producteur producteur, Message message) throws Exception
	{
		lock.lock();

		try
		{
			while(messages == bufferSize)
			{
				notFull.await();
			}

			buffer[nextWrite] = message;

			observateur.depotMessage(producteur, message);

			messages++;
			nextWrite = next(nextWrite);

			notEmpty.signal();
		}
		finally
		{
			lock.unlock();
		}
	}

	@Override
	public Message get(_Consommateur consommateur) throws Exception
	{
		lock.lock();

		try
		{
			while(messages == 0 && producers > 0)
			{
				notEmpty.await();
			}

			Message message;

			if(producers == 0 && messages == 0)
			{
				message = MESSAGE_END;
			}
			else
			{
				message = buffer[nextRead];

				observateur.retraitMessage(consommateur, message);

				buffer[nextRead] = null;
				messages--;
				nextRead = next(nextRead);
			}

			notFull.signal();

			return message;
		}
		finally
		{
			if(producers == 0)
			{
				notEmpty.signal();
			}

			lock.unlock();
		}
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
	}

	private int next(int n)
	{
		return (n + 1) % bufferSize;
	}
}
