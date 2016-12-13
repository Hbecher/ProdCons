package jus.poc.prodcons.message;

import jus.poc.prodcons.Message;

/**
 * Ce message indique aux consommateurs que les producteurs ont tous fini de produire.
 */
public class MessageEnd implements Message
{
	public static final MessageEnd MESSAGE_END = new MessageEnd();

	private MessageEnd()
	{
	}

	@Override
	public String toString()
	{
		return "MESSAGE_END";
	}
}
