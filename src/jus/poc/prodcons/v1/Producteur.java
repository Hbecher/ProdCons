package jus.poc.prodcons.v1;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import java.util.concurrent.atomic.AtomicInteger;

import jus.poc.prodcons.*;

public class Producteur extends Acteur implements _Producteur
{
	private static final AtomicInteger i = new AtomicInteger();
	private final Tampon tampon;
	private final Aleatoire aleatoire;
	private int nombreMessages = Aleatoire.valeur(DEFAULT_CONFIG.getProdMessagesMean(), DEFAULT_CONFIG.getProdMessagesDev());

	public Producteur(Observateur observateur, Tampon tampon) throws ControlException
	{
		super(Acteur.typeProducteur, observateur, DEFAULT_CONFIG.getProdTimeMean(), DEFAULT_CONFIG.getProdTimeDev());

		this.tampon = tampon;
		aleatoire = new Aleatoire(DEFAULT_CONFIG.getProdTimeMean(), DEFAULT_CONFIG.getProdTimeDev());
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

		while(nombreDeMessages() > 0)
		{
			nombreMessages--;

			try
			{
				Message message = new MessageX(this, ++m);
				tampon.put(this, message);

				System.out.println(identification() + " -> " + message);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			try
			{
				Thread.sleep(aleatoire.next());
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		i.addAndGet(-1);
	}

	@Override
	public int nombreDeMessages()
	{
		return nombreMessages;
	}
}
