package jus.poc.prodcons.v1;

import static jus.poc.prodcons.message.MessageEnd.MESSAGE_END;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdConsV1 implements Tampon
{
	private final int bufferSize;
	private final Message[] buffer;
	private int producers, consumers;
	private int messages = 0, nextRead = 0, nextWrite = 0;

	public ProdConsV1(int producers, int consumers, int bufferSize)
	{
		this.bufferSize = bufferSize;
		this.producers = producers;
		this.consumers = consumers;
		buffer = new Message[bufferSize];
	}

	@Override
	public synchronized void put(_Producteur producteur, Message message) throws Exception
	{
		while(messages == bufferSize)
		{
			wait();
		}

		buffer[nextWrite] = message;
		messages++;
		nextWrite = next(nextWrite);

		notifyAll();
	}

	@Override
	public synchronized Message get(_Consommateur consommateur) throws Exception
	{
		while(messages == 0 && producers > 0)
		{
			wait();
		}

		try
		{
			if(producers == 0 && messages == 0)
			{
				return MESSAGE_END;
			}

			Message message = buffer[nextRead];

			buffer[nextRead] = null;
			messages--;
			nextRead = next(nextRead);

			return message;
		}
		finally
		{
			notifyAll();
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

	public void print()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(Thread.currentThread().getName()).append("\t[ ");

		for(int i = 0; i < bufferSize; i++)
		{
			sb.append(i == nextRead ? "R" : "-").append(i == nextWrite ? "W" : "-").append(buffer[i] == null ? "-" : "M").append(" ");
		}

		sb.append("]");

		System.err.println(sb);
	}

	private int next(int n)
	{
		return (n + 1) % bufferSize;
	}
}
