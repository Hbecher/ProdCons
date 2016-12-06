package jus.poc.prodcons.v1;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;

public class TestProdCons extends Simulateur
{
	private final ProdCons tampon;
	private final Producteur[] producers = new Producteur[DEFAULT_CONFIG.getProducers()];
	private final Consommateur[] consumers = new Consommateur[DEFAULT_CONFIG.getConsumers()];

	public TestProdCons(Observateur observateur)
	{
		super(observateur);

		tampon = new ProdCons(observateur, DEFAULT_CONFIG.getBufferSize());
	}

	public static void main(String[] args)
	{
		new TestProdCons(new Observateur()).start();
	}

	@Override
	protected void run() throws Exception
	{
		for(int i = 0; i < producers.length; i++)
		{
			Producteur producteur = new Producteur(observateur, tampon);
			producers[i] = producteur;
			producteur.start();
		}

		for(int i = 0; i < consumers.length; i++)
		{
			Consommateur consommateur = new Consommateur(observateur, tampon);
			consumers[i] = consommateur;
			consommateur.start();
		}
	}
}
