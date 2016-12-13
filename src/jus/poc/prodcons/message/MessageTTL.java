package jus.poc.prodcons.message;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.v4.ProducteurV4;

/**
 * Ce message représente un {@link jus.poc.prodcons.message.MessageX} à plusieurs exemplaires.<br />
 * A chaque fois qu'un exemplaire est consommé, la méthode {@code dec()} est appelée.<br />
 * Un producteur produisant ce type de message se bloque en attendant que tous les exemplaires soient consommés.<br />
 * Au dernier exemplaire lu (lorsque {@code isLast() == true}), le producteur de ce message est réveillé.
 *
 * @see jus.poc.prodcons.message.MessageX
 */
public class MessageTTL extends MessageX
{
	private static final Aleatoire ALEATOIRE = new Aleatoire(DEFAULT_CONFIG.getConsMessagesMean(), DEFAULT_CONFIG.getConsMessagesDev());
	/**
	 * Le nombre d'exemplaires de ce message
	 */
	private final int ttl;
	/**
	 * Le nombre d'exemplaires restants
	 */
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

	/**
	 * Retourne le nombre initial d'exemplaires de ce message.
	 *
	 * @return Le nombre initial d'exemplaires
	 */
	public int getInitTTL()
	{
		return ttl;
	}

	/**
	 * Retourne le nombre d'exemplaires restants de ce message.
	 *
	 * @return Le nombre d'exemplaires restants
	 */
	public int getTTL()
	{
		return c;
	}

	/**
	 * Décrémente le nombre d'exemplaires par un, typiquement à la consommation.
	 */
	public void dec()
	{
		c--;
	}

	/**
	 * Indique si le message n'a plus d'exemplaires restants.
	 * @return {@code true} s'il ne reste plus d'exemplaires
	 */
	public boolean isLast()
	{
		return c == 0;
	}

	@Override
	public String toString()
	{
		return String.format("MessageX{pid=%d, id=%d, ttl=%d, msg=%s}", getSender().identification(), getID(), ttl, getMessage() == null ? "<empty>" : getMessage());
	}
}
