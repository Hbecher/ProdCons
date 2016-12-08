package jus.poc.prodcons.common;

import static jus.poc.prodcons.common.MessageX.END_MESSAGE;

import jus.poc.prodcons.*;

public abstract class ProdCons implements Tampon
{
	protected final Observateur observateur;
	private final int bufferSize;
	private final Message[] buffer;
	private int producers, consumers;
	private int messages = 0, nextRead = 0, nextWrite = 0;

	public ProdCons(Observateur observateur, int producers, int consumers, int bufferSize)
	{
		this.observateur = observateur;
		this.bufferSize = bufferSize;
		this.producers = producers;
		this.consumers = consumers;
		buffer = new Message[bufferSize];
	}

	@Override
	public abstract void put(_Producteur producteur, Message message) throws Exception;

	@Override
	public abstract Message get(_Consommateur consommateur) throws Exception;

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

	public int consumers()
	{
		return consumers;
	}

	public int producers()
	{
		return producers;
	}

	public void print()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("[\t");

		for(int i = 0; i < bufferSize; i++)
		{
			Message message = buffer[i];

			sb.append(i == nextRead ? "R" : "-").append(i == nextWrite ? "W" : "-").append(message == null ? "-" : "M").append("\t");

		}

		sb.append("]");

		System.out.println(sb);
	}

	private int suivant(int n)
	{
		return (n + 1) % bufferSize;
	}

	protected Message getMessage(_Consommateur consommateur) throws ControlException
	{
		if(producers == 0 && messages == 0)
		{
			return END_MESSAGE;
		}

		Message message = buffer[nextRead];

		retrieve(consommateur, message);
		nextRead();

		return message;
	}

	protected void putMessage(_Producteur producteur, Message message) throws ControlException
	{
		buffer[nextWrite] = message;

		deposit(producteur, message);
		nextWrite();

		if(producteur.nombreDeMessages() <= 1)
		{
			producers--;
		}
	}

	protected final void nextRead()
	{
		buffer[nextRead] = null;
		messages--;
		nextRead = suivant(nextRead);
	}

	protected final void nextWrite()
	{
		messages++;
		nextWrite = suivant(nextWrite);
	}

	protected void deposit(_Producteur producteur, Message message) throws ControlException
	{
	}

	protected void retrieve(_Consommateur consommateur, Message message) throws ControlException
	{
	}
}
