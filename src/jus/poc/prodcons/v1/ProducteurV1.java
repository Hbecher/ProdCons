package jus.poc.prodcons.v1;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.*;
import jus.poc.prodcons.message.MessageX;
import jus.poc.prodcons.print.Afficheur;

public class ProducteurV1 extends Acteur implements _Producteur
{
	private static final Aleatoire ALEATOIRE = new Aleatoire(DEFAULT_CONFIG.getProdTimeMean(), DEFAULT_CONFIG.getProdTimeDev());
	private final ProdConsV1 tampon;
	private int nombreMessages = Aleatoire.valeur(DEFAULT_CONFIG.getProdMessagesMean(), DEFAULT_CONFIG.getProdMessagesDev());

	public ProducteurV1(Observateur observateur, ProdConsV1 tampon) throws ControlException
	{
		super(Acteur.typeProducteur, observateur, DEFAULT_CONFIG.getProdTimeMean(), DEFAULT_CONFIG.getProdTimeDev());

		this.tampon = tampon;

		Afficheur.printNewProducer(this);
	}

	@Override
	public void run()
	{
		int id = 1; // numéro du message

		while(nombreMessages > 0)
		{
			try
			{
				int time = ALEATOIRE.next();

				// on s'endort pour simuler une création de message
				Thread.sleep(time);

				Message message = new MessageX(this, id, Long.toString(System.currentTimeMillis()));

				Afficheur.printProduction(this, message, time);

				// on met le message dans le buffer
				tampon.put(this, message);

				// on passe au message suivant
				nombreMessages--;
				id++;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		Afficheur.printEndProducer(this);

		// à la fin, on décrémente les producteurs restants
		tampon.decProducers();
	}

	@Override
	public int nombreDeMessages()
	{
		return nombreMessages;
	}
}
