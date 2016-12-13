package jus.poc.prodcons.v6;

import java.util.ArrayDeque;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Producteur;

/**
 * Cette classe permet de vérifier le bon respect des propriétés du protocole d'échange de la version 3.
 */
public class Verificateur
{
	/**
	 * File gardant en mémoire l'ordre d'arrivée des messages
	 */
	// sens de remplissage : <--out [ MESSAGES ] <--in
	private final ArrayDeque<Message> messages = new ArrayDeque<>();

	public Verificateur()
	{
	}

	/**
	 * Ajoute le message {@code message} à la file des messages.
	 *
	 * @param message le message
	 */
	public void deposit(Message message)
	{
		messages.add(message);
	}

	/**
	 * Vérifie que le message retiré correspond au premier message inséré.
	 *
	 * @param message le message retiré
	 * @throws VerifException Si le message retiré n'est pas le premier inséré
	 */
	public void retrieve(Message message) throws VerifException
	{
		Message msg = messages.poll();

		if(msg != null)
		{
			if(msg != message)
			{
				throw new VerifException();
			}
		}
	}

	/**
	 * Vérifie que le producteur a produit son quota de messages.
	 *
	 * @param producteur le producteur
	 * @throws VerifException Si le producteur n'a pas produit son quota de messages
	 */
	public void endProducer(_Producteur producteur) throws VerifException
	{
		if(producteur.nombreDeMessages() > 0)
		{
			throw new VerifException();
		}
	}

	/**
	 * Vérifie que le tampon est vide, i.e. que tous les messages ont été consommés.
	 *
	 * @param tampon le tampon
	 * @throws VerifException S'il reste des messages dans le tampon
	 */
	public void end(Tampon tampon) throws VerifException
	{
		if(tampon.enAttente() > 0)
		{
			throw new VerifException();
		}
	}
}
