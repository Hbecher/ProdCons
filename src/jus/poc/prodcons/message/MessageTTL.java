package jus.poc.prodcons.message;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.v4.ProducteurV4;

public class MessageTTL extends MessageX
{
	private static final Aleatoire ALEATOIRE = new Aleatoire(DEFAULT_CONFIG.getConsTimeMean(), DEFAULT_CONFIG.getConsTimeDev());
	private int ttl;

	public MessageTTL(ProducteurV4 producteur, int id)
	{
		this(producteur, id, null);
	}

	public MessageTTL(ProducteurV4 producteur, int id, String message)
	{
		super(producteur, id, message);

		ttl = ALEATOIRE.next();
	}

	@Override
	public ProducteurV4 getSender()
	{
		return (ProducteurV4) super.getSender();
	}

	public int getTTL()
	{
		return ttl;
	}

	public void decTTL()
	{
		ttl--;
	}

	public boolean isLast()
	{
		return ttl <= 0;
	}

	@Override
	public String toString()
	{
		return String.format("%d-%d-%d: %s", getSender().identification(), getID(), ttl, getMessage() == null ? "<empty>" : getMessage());
	}
}
