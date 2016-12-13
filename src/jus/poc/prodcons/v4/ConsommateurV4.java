package jus.poc.prodcons.v4;

import static jus.poc.prodcons.message.MessageEnd.MESSAGE_END;
import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.*;
import jus.poc.prodcons.message.MessageEnd;

public class ConsommateurV4 extends Acteur implements _Consommateur
{
	private static final Aleatoire ALEATOIRE = new Aleatoire(DEFAULT_CONFIG.getConsTimeMean(), DEFAULT_CONFIG.getConsTimeDev());
	private final ProdConsV4 tampon;
	private int nombreMessages = 0;

	public ConsommateurV4(Observateur observateur, ProdConsV4 tampon) throws ControlException
	{
		super(Acteur.typeConsommateur, observateur, DEFAULT_CONFIG.getConsTimeMean(), DEFAULT_CONFIG.getConsTimeDev());

		this.tampon = tampon;

		observateur.newConsommateur(this);
	}

	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				Message message = tampon.get(this);

				if(message == MESSAGE_END || message instanceof MessageEnd)
				{
					break;
				}

				int time = ALEATOIRE.next();

				Thread.sleep(time);

				observateur.consommationMessage(this, message, time);

				System.out.println(identification() + " <- " + message);

				nombreMessages++;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		System.out.println("Consommateur n°" + identification() + " terminé");

		tampon.decConsumers();
	}

	@Override
	public int nombreDeMessages()
	{
		return nombreMessages;
	}
}
