package jus.poc.prodcons.v3;

import static jus.poc.prodcons.message.MessageEnd.MESSAGE_END;

import jus.poc.prodcons.*;
import jus.poc.prodcons.print.Printer;
import jus.poc.prodcons.v2.Semaphore;

public class ProdConsV3 implements Tampon
{
	private final Observateur observateur;
	private final int bufferSize;
	private final Message[] buffer;
	private int producers, consumers;
	private int messages = 0, nextRead = 0, nextWrite = 0;
	private final Semaphore mutexIn = new Semaphore(1), mutexOut = new Semaphore(1), notEmpty = new Semaphore(0), notFull;

	public ProdConsV3(Observateur observateur, int producers, int consumers, int bufferSize)
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

		Printer.printPut(producteur, message);

		mutexIn.release();
		notEmpty.release();
	}

	@Override
	public Message get(_Consommateur consommateur) throws Exception
	{
		notEmpty.acquire();
		mutexOut.acquire();

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

			Printer.printGet(consommateur, message);
		}

		mutexOut.release();

		if(producers == 0)
		{
			notEmpty.release();
		}

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

	public void decConsumers()
	{
		consumers--;
	}

	public void decProducers()
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
