package jus.poc.prodcons.message;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.common.Semaphore;
import jus.poc.prodcons.v4.ProducteurV4;

public class MessageTTL extends MessageX
{
	private static final Aleatoire ALEATOIRE = new Aleatoire(DEFAULT_CONFIG.getConsMessagesMean(), DEFAULT_CONFIG.getConsMessagesDev());
	private final Semaphore s = new Semaphore(0);
	private final int ttl;
	private int c;

	public MessageTTL(ProducteurV4 producteur, int id)
	{
		this(producteur, id, null);
	}

	public MessageTTL(ProducteurV4 producteur, int id, String message)
	{
		super(producteur, id, message);

		ttl = ALEATOIRE.next();
		c = ttl;
	}

	@Override
	public ProducteurV4 getSender()
	{
		return (ProducteurV4) super.getSender();
	}

	public int getInitTTL()
	{
		return ttl;
	}

	public int getTTL()
	{
		return c;
	}

	public void dec()
	{
		--c;
	}

	public boolean isLast()
	{
		return c <= 0;
	}

	public void wakeup()
	{
		getSender().wakeup();
	}

	@Override
	public String toString()
	{
		return String.format("%d-%d-%d: %s", producteur.identification(), id, ttl, message == null ? "<empty>" : message);
	}
}
