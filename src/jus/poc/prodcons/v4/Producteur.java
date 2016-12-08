package jus.poc.prodcons.v4;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import java.util.concurrent.atomic.AtomicInteger;

import jus.poc.prodcons.*;
import jus.poc.prodcons.common.MessageX;

public class Producteur extends Acteur implements _Producteur
{
	private static final Aleatoire ALEATOIRE = new Aleatoire(DEFAULT_CONFIG.getProdTimeMean(), DEFAULT_CONFIG.getProdTimeDev()), MSG_ALEATOIRE = new Aleatoire(DEFAULT_CONFIG.getConsMessagesMean(), DEFAULT_CONFIG.getConsMessagesDev());
	private static final AtomicInteger i = new AtomicInteger();
	private final Tampon tampon;
	private int nombreMessages = Aleatoire.valeur(DEFAULT_CONFIG.getProdMessagesMean(), DEFAULT_CONFIG.getProdMessagesDev());

	public Producteur(Observateur observateur, Tampon tampon) throws ControlException
	{
		super(Acteur.typeProducteur, observateur, DEFAULT_CONFIG.getProdTimeMean(), DEFAULT_CONFIG.getProdTimeDev());

		this.tampon = tampon;

		observateur.newProducteur(this);
	}

	public static int producteursRestants()
	{
		return i.get();
	}

	@Override
	public void run()
	{
		i.addAndGet(1);

		int m = 0;

		while(nombreMessages > 0)
		{
			Message message = new MessageX(this, ++m);
			int msgs = MSG_ALEATOIRE.next();

			while(msgs > 0)
			{
				int sleep = ALEATOIRE.next();

				try
				{
					tampon.put(this, message);

					try
					{
						Thread.sleep(sleep);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}

					observateur.productionMessage(this, message, sleep);

					System.out.println(identification() + " -> " + message);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}

				msgs--;
			}

			nombreMessages--;
		}

		i.addAndGet(-1);

		System.out.println(getName() + " terminé");
	}

	@Override
	public int nombreDeMessages()
	{
		return nombreMessages;
	}
}
