package jus.poc.prodcons.message;

import jus.poc.prodcons.Message;
import jus.poc.prodcons._Producteur;

public class MessageX implements Message
{
	protected final _Producteur producteur;
	protected final int id;
	protected final String message;

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
		return String.format("%d-%d: %s", producteur.identification(), id, message == null ? "<empty>" : message);
	}
}
