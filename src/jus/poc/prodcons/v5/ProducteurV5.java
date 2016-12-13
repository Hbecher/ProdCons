package jus.poc.prodcons.v5;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.*;
import jus.poc.prodcons.message.MessageX;

public class ProducteurV5 extends Acteur implements _Producteur
{
	private static final Aleatoire ALEATOIRE = new Aleatoire(DEFAULT_CONFIG.getProdTimeMean(), DEFAULT_CONFIG.getProdTimeDev());
	private final ProdConsV5 tampon;
	private int nombreMessages = Aleatoire.valeur(DEFAULT_CONFIG.getProdMessagesMean(), DEFAULT_CONFIG.getProdMessagesDev());

	public ProducteurV5(Observateur observateur, ProdConsV5 tampon) throws ControlException
	{
		super(Acteur.typeProducteur, observateur, DEFAULT_CONFIG.getProdTimeMean(), DEFAULT_CONFIG.getProdTimeDev());

		this.tampon = tampon;

		observateur.newProducteur(this);
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

				Message message = new MessageX(this, id);

				observateur.productionMessage(this, message, time);
				tampon.put(this, message);

				System.out.println(identification() + " -> " + message);

				nombreMessages--;
				id++;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		System.out.println("Producteur n°" + identification() + " terminé");

		tampon.decProducers();
	}

	@Override
	public int nombreDeMessages()
	{
		return nombreMessages;
	}
}
