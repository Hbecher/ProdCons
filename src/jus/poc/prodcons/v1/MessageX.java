package jus.poc.prodcons.v1;

import jus.poc.prodcons.Message;
import jus.poc.prodcons._Producteur;

public class MessageX implements Message
{
	private final String message;

	public MessageX(_Producteur producteur, int id)
	{
		this.message = producteur.identification() + "-" + id;
	}

	@Override
	public String toString()
	{
		return message;
	}
}
