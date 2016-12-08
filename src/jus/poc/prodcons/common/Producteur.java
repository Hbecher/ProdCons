package jus.poc.prodcons.common;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.*;
import jus.poc.prodcons.message.MessageX;

public class Producteur extends Acteur implements _Producteur
{
	protected static final Aleatoire ALEATOIRE = DEFAULT_CONFIG.areTimesInMilliseconds() ? new Aleatoire(DEFAULT_CONFIG.getProdTimeMean(), DEFAULT_CONFIG.getProdTimeDev()) : new Aleatoire(DEFAULT_CONFIG.getProdTimeMean() * 1000, DEFAULT_CONFIG.getProdTimeDev() * 1000);
	private final ProdCons tampon;
	protected int nombreMessages = Aleatoire.valeur(DEFAULT_CONFIG.getProdMessagesMean(), DEFAULT_CONFIG.getProdMessagesDev());

	public Producteur(Observateur observateur, ProdCons tampon) throws ControlException
	{
		super(Acteur.typeProducteur, observateur, DEFAULT_CONFIG.getProdTimeMean(), DEFAULT_CONFIG.getProdTimeDev());

		this.tampon = tampon;
	}

	@Override
	public void run()
	{
		int id = 1;

		while(keepGoing())
		{
			try
			{
				int time = ALEATOIRE.next();

				sleep(time);

				Message message = newMessage(id);

				production(message, time);
				process(message);
				put(message);
				next();

				id++;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		end();
	}

	@Override
	public int nombreDeMessages()
	{
		return nombreMessages;
	}

	protected boolean keepGoing()
	{
		return nombreMessages > 0;
	}

	protected void sleep(int ms)
	{
		try
		{
			Thread.sleep(ms);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	protected Message newMessage(int id)
	{
		return new MessageX(this, id);
	}

	protected void put(Message message) throws Exception
	{
		tampon.put(this, message);
	}

	protected void process(Message message)
	{
		System.out.println(identification() + " -> " + message);
	}

	protected void next()
	{
		nombreMessages--;
	}

	protected void end()
	{
		System.out.println("Producteur n°" + identification() + " terminé");

		tampon.decProducers();
	}

	protected void production(Message message, int time) throws ControlException
	{
	}
}
