package jus.poc.prodcons.v2;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.*;
import jus.poc.prodcons.message.MessageX;
import jus.poc.prodcons.print.Printer;

public class ProducteurV2 extends Acteur implements _Producteur
{
	private static final Aleatoire ALEATOIRE = new Aleatoire(DEFAULT_CONFIG.getProdTimeMean(), DEFAULT_CONFIG.getProdTimeDev());
	private final ProdConsV2 tampon;
	private int nombreMessages = Aleatoire.valeur(DEFAULT_CONFIG.getProdMessagesMean(), DEFAULT_CONFIG.getProdMessagesDev());

	public ProducteurV2(Observateur observateur, ProdConsV2 tampon) throws ControlException
	{
		super(Acteur.typeProducteur, observateur, DEFAULT_CONFIG.getProdTimeMean(), DEFAULT_CONFIG.getProdTimeDev());

		this.tampon = tampon;

		Printer.printNewProducer(this);
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

				Message message = new MessageX(this, id, Long.toString(System.currentTimeMillis()));

				Printer.printProduction(this, message, time);

				tampon.put(this, message);

				nombreMessages--;
				id++;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		Printer.printEndProducer(this);

		tampon.decProducers();
	}

	@Override
	public int nombreDeMessages()
	{
		return nombreMessages;
	}
}
