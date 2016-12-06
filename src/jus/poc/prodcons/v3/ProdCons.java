package jus.poc.prodcons.v3;

import java.util.concurrent.Semaphore;

import jus.poc.prodcons.*;

public class ProdCons implements Tampon
{
	private final Semaphore mutexIn = new Semaphore(1, true), mutexOut = new Semaphore(1, true), notEmpty = new Semaphore(0, true), notFull;
	private final Observateur observateur;
	private final int bufferSize;
	private final Message[] buffer;
	private int messages = 0, nextRead = 0, nextWrite = 0;

	public ProdCons(Observateur observateur, int bufferSize)
	{
		this.observateur = observateur;
		this.bufferSize = bufferSize;
		buffer = new Message[bufferSize];
		notFull = new Semaphore(bufferSize, true);
	}

	@Override
	public void put(_Producteur producteur, Message message) throws Exception
	{
		notFull.acquire();
		mutexIn.acquire();

		buffer[nextWrite] = message;
		messages++;
		nextWrite = suivant(nextWrite);

		observateur.depotMessage(producteur, message);

		print();

		mutexIn.release();
		notEmpty.release();
	}

	@Override
	public Message get(_Consommateur consommateur) throws Exception
	{
		notEmpty.acquire();
		mutexOut.acquire();

		Message message = buffer[nextRead];
		buffer[nextRead] = null;
		messages--;
		nextRead = suivant(nextRead);

		observateur.retraitMessage(consommateur, message);

		print();

		mutexOut.release();
		notFull.release();

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
