package jus.poc.prodcons.common;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.*;

public class Producteur extends Acteur implements _Producteur
{
	private static final Aleatoire ALEATOIRE = new Aleatoire(DEFAULT_CONFIG.getProdTimeMean(), DEFAULT_CONFIG.getProdTimeDev());
	private final ProdCons tampon;
	private int nombreMessages = Aleatoire.valeur(DEFAULT_CONFIG.getProdMessagesMean(), DEFAULT_CONFIG.getProdMessagesDev());

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

				put(message);
				process(message);
				production(message, time);
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

	protected Message newMessage(int number)
	{
		return new MessageX(this, number);
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
	}

	protected void production(Message message, int time) throws ControlException
	{
	}
}
