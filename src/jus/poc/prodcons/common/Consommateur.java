package jus.poc.prodcons.common;

import static jus.poc.prodcons.message.MessageEnd.MESSAGE_END;
import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.*;
import jus.poc.prodcons.message.MessageEnd;

public class Consommateur extends Acteur implements _Consommateur
{
	protected static final Aleatoire ALEATOIRE = DEFAULT_CONFIG.areTimesInMilliseconds() ? new Aleatoire(DEFAULT_CONFIG.getConsTimeMean(), DEFAULT_CONFIG.getConsTimeDev()) : new Aleatoire(DEFAULT_CONFIG.getConsTimeMean() * 1000, DEFAULT_CONFIG.getConsTimeDev() * 1000);
	private final ProdCons tampon;
	protected int nombreMessages = 0;

	public Consommateur(Observateur observateur, ProdCons tampon) throws ControlException
	{
		super(Acteur.typeConsommateur, observateur, DEFAULT_CONFIG.getConsTimeMean(), DEFAULT_CONFIG.getConsTimeDev());

		this.tampon = tampon;
	}

	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				Message message = read();

				if(shouldEnd(message))
				{
					break;
				}

				int time = ALEATOIRE.next();

				sleep(time);
				consumption(message, time);
				process(message);
				next();
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

	protected Message read() throws Exception
	{
		return tampon.get(this);
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

	protected void process(Message message)
	{
		System.out.println(identification() + " <- " + message);
	}

	protected boolean shouldEnd(Message message)
	{
		// (message == MessageEnd.MESSAGE_END) == (message instanceof MessageEnd)
		return message == MESSAGE_END || message instanceof MessageEnd;
	}

	protected void next()
	{
		nombreMessages++;
	}

	protected void end()
	{
		System.out.println("Consommateur n°" + identification() + " terminé");

		tampon.decConsumers();
	}

	protected void consumption(Message message, int time) throws ControlException
	{
	}
}
