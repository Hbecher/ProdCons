package jus.poc.prodcons.common;

import static jus.poc.prodcons.common.MessageX.END_MESSAGE;
import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.*;

public class Consommateur extends Acteur implements _Consommateur
{
	private static final Aleatoire ALEATOIRE = new Aleatoire(DEFAULT_CONFIG.getConsTimeMean(), DEFAULT_CONFIG.getConsTimeDev());
	private final ProdCons tampon;
	private int nombreMessages = 0;

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
				process(message);
				consumption(message, time);
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
		return message == END_MESSAGE;
	}

	protected void next()
	{
		nombreMessages++;
	}

	protected void end()
	{
		System.out.println("Consommateur n°" + identification() + " terminé");
	}

	protected void consumption(Message message, int time) throws ControlException
	{
	}
}
