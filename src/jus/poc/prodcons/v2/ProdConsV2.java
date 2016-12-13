package jus.poc.prodcons.v2;

import static jus.poc.prodcons.message.MessageEnd.MESSAGE_END;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.print.Printer;

public class ProdConsV2 implements Tampon
{
	private final int bufferSize;
	private final Message[] buffer;
	private int producers, consumers;
	private int messages = 0, nextRead = 0, nextWrite = 0;
	// respectivement les sémaphores qui gèrent les exclusions mutuelles en entrée et sortie et ceux gérant les attentes de buffer non vide et de buffer non plein
	private final Semaphore mutexIn = new Semaphore(1), mutexOut = new Semaphore(1), notEmpty = new Semaphore(0), notFull;

	public ProdConsV2(int producers, int consumers, int bufferSize)
	{
		this.bufferSize = bufferSize;
		this.producers = producers;
		this.consumers = consumers;
		buffer = new Message[bufferSize];
		notFull = new Semaphore(bufferSize);
	}

	@Override
	public void put(_Producteur producteur, Message message) throws Exception
	{
		notFull.acquire(); // on attend qu'il y ait au moins une place libre dans le buffer
		mutexIn.acquire(); // exclusion mutuelle

		// le producteur écrit son message

		buffer[nextWrite] = message;
		messages++;
		nextWrite = next(nextWrite);

		Printer.printPut(producteur, message);

		mutexIn.release(); // fin exclusion mutuelle
		notEmpty.release(); // on réveille un consommateur pour lire le message publié
	}

	@Override
	public Message get(_Consommateur consommateur) throws Exception
	{
		notEmpty.acquire(); // on attend qu'il y ait au moins un message dans le buffer
		mutexOut.acquire(); // exclusion mutuelle

		// on récupère le message (cf. ProdConsV1)

		Message message;

		if(producers == 0 && messages == 0)
		{
			message = MESSAGE_END;
		}
		else
		{
			message = buffer[nextRead];

			buffer[nextRead] = null;
			messages--;
			nextRead = next(nextRead);

			Printer.printGet(consommateur, message);
		}

		mutexOut.release(); // fin exclusion mutuelle

		if(producers == 0)
		{
			// s'il n'y a plus de producteurs, on réveille le consommateur suivant pour qu'il s'arrête
			notEmpty.release();
		}

		notFull.release(); // on réveille un producteur
		// note : si plus de producteurs, aucun effet

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
			// s'il ne reste plus de producteurs, on réveille un consommateur pour qu'il s'arrête
			// il initiera une réaction en chaîne provocant l'arrêt complet du système
			notEmpty.release();
		}
	}

	private int next(int n)
	{
		return (n + 1) % bufferSize;
	}
}
