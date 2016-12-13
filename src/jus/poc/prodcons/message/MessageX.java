package jus.poc.prodcons.message;

import jus.poc.prodcons.Message;
import jus.poc.prodcons._Producteur;

/**
 * C'est le message de base.<br />
 * Il contient :
 * <ul>
 * <li>le producteur qui l'a créé</li>
 * <li>un identifiant propre au producteur (son numéro de production)</li>
 * <li>une chaîne de caractères quelconque</li>
 * </ul>
 */
public class MessageX implements Message
{
	private final _Producteur producteur;
	private final int id;
	private final String message;

	public MessageX(_Producteur producteur, int id)
	{
		this(producteur, id, null);
	}

	public MessageX(_Producteur producteur, int id, String message)
	{
		this.producteur = producteur;
		this.id = id;
		this.message = message;
	}

	public _Producteur getSender()
	{
		return producteur;
	}

	public int getID()
	{
		return id;
	}

	public String getMessage()
	{
		return message;
	}

	@Override
	public String toString()
	{
		return String.format("MessageX{pid=%d, id=%d, msg=%s}", producteur.identification(), id, message == null ? "<empty>" : message);
	}
}
