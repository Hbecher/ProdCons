package jus.poc.prodcons.v1;

import static jus.poc.prodcons.message.MessageEnd.MESSAGE_END;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.print.Printer;

public class ProdConsV1 implements Tampon
{
	private final int bufferSize;
	private final Message[] buffer;
	private int producers, consumers;
	// respectivement le nombre de messages, le pointeur de lecture et celui d'écriture
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
		// tant que le buffer est plein, on attend
		while(messages == bufferSize)
		{
			wait();
		}

		// un producteur se réveille et écrit son message

		buffer[nextWrite] = message;
		messages++;
		nextWrite = next(nextWrite); // incrémentation du pointeur d'écriture

		Printer.printPut(producteur, message);

		notifyAll(); // on notifie tout le monde que le buffer a été mis à jour
	}

	@Override
	public synchronized Message get(_Consommateur consommateur) throws Exception
	{
		// tant qu'il n'y a pas de message ET qu'il reste des producteurs, on attend
		while(messages == 0 && producers > 0)
		{
			wait();
		}

		if(producers == 0 && messages == 0)
		{
			// s'il ne reste plus de messages ni de producteurs, on indique au consommateur qu'il doit s'arrêter
			return MESSAGE_END;
		}

		// un consommateur se réveille, il récupère le prochain message

		Message message = buffer[nextRead];

		buffer[nextRead] = null; // on efface le message lu du buffer
		messages--;
		nextRead = next(nextRead); // on incrémente le pointeur de lecture

		Printer.printGet(consommateur, message);

		notifyAll(); // on notifie tout le monde que le buffer a été mis à jour

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

	/**
	 * Décrémente le nombre de consommateurs.
	 */
	public synchronized void decConsumers()
	{
		consumers--;
	}

	/**
	 * Décrémente le nombre de producteurs.
	 */
	public synchronized void decProducers()
	{
		producers--;
	}

	/**
	 * Calcule le prochain pointeur.
	 *
	 * @param n la valeur actuelle du pointeur
	 * @return Sa nouvelle valeur
	 */
	private int next(int n)
	{
		return (n + 1) % bufferSize;
	}
}
