package jus.poc.prodcons.v1;

import jus.poc.prodcons.*;

public class ProdCons implements Tampon
{
	private final Observateur observateur;
	private final int bufferSize;
	private final Message[] buffer;
	private int messages = 0, nextRead = 0, nextWrite = 0;

	public ProdCons(Observateur observateur, int bufferSize)
	{
		this.observateur = observateur;
		this.bufferSize = bufferSize;
		buffer = new Message[bufferSize];
	}

	@Override
	public synchronized void put(_Producteur producteur, Message message) throws Exception
	{
		while(enAttente() == bufferSize)
		{
			wait();
		}

		put(message);

		notifyAll();
	}

	@Override
	public synchronized Message get(_Consommateur consommateur) throws Exception
	{
		while(enAttente() == 0)
		{
			wait();
		}

		Message message = get();

		notifyAll();

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

	private int suivant(int n)
	{
		return (n + 1) % bufferSize;
	}

	private Message get()
	{
		Message message = buffer[nextRead];
		buffer[nextRead] = null;
		messages--;
		nextRead = suivant(nextRead);

		print();

		return message;
	}

	private void put(Message message)
	{
		buffer[nextWrite] = message;
		messages++;
		nextWrite = suivant(nextWrite);

		print();
	}

	private void print()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("[\t");

		for(int i = 0; i < bufferSize; i++)
		{
			Message message = buffer[i];

			if(i == nextRead)
			{
				sb.append("R");
			}

			if(i == nextWrite)
			{
				sb.append("W");
			}

			sb.append(message == null ? "-" : "M").append("\t");
		}

		sb.append("]");

		System.out.println(sb);
	}
}
