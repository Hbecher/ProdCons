package jus.poc.prodcons.v4;

import static jus.poc.prodcons.common.MessageX.END_MESSAGE;
import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.*;

public class Consommateur extends Acteur implements _Consommateur
{
	private static final Aleatoire ALEATOIRE = new Aleatoire(DEFAULT_CONFIG.getConsTimeMean(), DEFAULT_CONFIG.getConsTimeDev());
	private final Tampon tampon;
	private int nombreMessages = 0;

	public Consommateur(Observateur observateur, Tampon tampon) throws ControlException
	{
		super(Acteur.typeConsommateur, observateur, DEFAULT_CONFIG.getConsTimeMean(), DEFAULT_CONFIG.getConsTimeDev());

		this.tampon = tampon;

		observateur.newConsommateur(this);
	}

	@Override
	public void run()
	{
		while(Producteur.producteursRestants() > 0)
		{
			int sleep = ALEATOIRE.next();

			try
			{
				Message message = tampon.get(this);

				if(message == END_MESSAGE)
				{
					break;
				}

				try
				{
					Thread.sleep(sleep);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}

				observateur.consommationMessage(this, message, sleep);

				System.out.println(identification() + " <- " + message);

				nombreMessages++;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		System.out.println(getName() + " terminé");
	}

	@Override
	public int nombreDeMessages()
	{
		return nombreMessages;
	}
}
