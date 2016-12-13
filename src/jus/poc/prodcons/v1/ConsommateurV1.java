package jus.poc.prodcons.v1;

import static jus.poc.prodcons.message.MessageEnd.MESSAGE_END;
import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.*;
import jus.poc.prodcons.message.MessageEnd;
import jus.poc.prodcons.print.Printer;

public class ConsommateurV1 extends Acteur implements _Consommateur
{
	private static final Aleatoire ALEATOIRE = new Aleatoire(DEFAULT_CONFIG.getConsTimeMean(), DEFAULT_CONFIG.getConsTimeDev());
	private final ProdConsV1 tampon;
	private int nombreMessages = 0;

	public ConsommateurV1(Observateur observateur, ProdConsV1 tampon) throws ControlException
	{
		super(Acteur.typeConsommateur, observateur, DEFAULT_CONFIG.getConsTimeMean(), DEFAULT_CONFIG.getConsTimeDev());

		this.tampon = tampon;

		Printer.printNewConsumer(this);
	}

	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				// on récupère un message
				Message message = tampon.get(this);

				// si c'est le message de fin, on s'arrête
				// double vérification du message au cas où (c'est inutile)
				if(message == MESSAGE_END || message instanceof MessageEnd)
				{
					break;
				}

				int time = ALEATOIRE.next();

				// on s'endort pour simuler un traitement
				Thread.sleep(time);

				Printer.printConsumption(this, message, time);

				// on passe au message suivant
				nombreMessages++;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		Printer.printEndConsumer(this);

		// à la fin, on décrémente les consommateurs restants
		tampon.decConsumers();
	}

	@Override
	public int nombreDeMessages()
	{
		return nombreMessages;
	}
}
