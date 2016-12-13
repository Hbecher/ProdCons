package jus.poc.prodcons.v4;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.*;
import jus.poc.prodcons.message.MessageTTL;
import jus.poc.prodcons.print.Afficheur;
import jus.poc.prodcons.v2.Semaphore;

public class ProducteurV4 extends Acteur implements _Producteur
{
	private static final Aleatoire ALEATOIRE = new Aleatoire(DEFAULT_CONFIG.getProdTimeMean(), DEFAULT_CONFIG.getProdTimeDev());
	private final ProdConsV4 tampon;
	private int nombreMessages = Aleatoire.valeur(DEFAULT_CONFIG.getProdMessagesMean(), DEFAULT_CONFIG.getProdMessagesDev());
	private final Semaphore wait = new Semaphore(0);

	public ProducteurV4(Observateur observateur, ProdConsV4 tampon) throws ControlException
	{
		super(Acteur.typeProducteur, observateur, DEFAULT_CONFIG.getProdTimeMean(), DEFAULT_CONFIG.getProdTimeDev());

		this.tampon = tampon;

		observateur.newProducteur(this);

		Afficheur.printNewProducer(this);
	}

	@Override
	public void run()
	{
		int id = 1;

		while(nombreMessages > 0)
		{
			try
			{
				int time = ALEATOIRE.next();

				Thread.sleep(time);

				MessageTTL message = new MessageTTL(this, id, Long.toString(System.currentTimeMillis()));

				observateur.productionMessage(this, message, time);

				Afficheur.printProduction(this, message, time);

				tampon.put(this, message);
				// après avoir publié le message, on attend qu'il soit intégralement consommé
				wait.acquire();

				nombreMessages--;
				id++;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		Afficheur.printEndProducer(this);

		tampon.decProducers();
	}

	@Override
	public int nombreDeMessages()
	{
		return nombreMessages;
	}

	/**
	 * Réveille le producteur en attente, typiquement lorsque tous les exemplaires du MessageTTL qu'il a publié ont été consommés.
	 */
	public void wakeup()
	{
		wait.release();
	}
}
