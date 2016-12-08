package jus.poc.prodcons.common;

import jus.poc.prodcons.Message;
import jus.poc.prodcons._Producteur;

public class MessageX implements Message
{
	public static final Message END_MESSAGE = new Message()
	{
	};
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
