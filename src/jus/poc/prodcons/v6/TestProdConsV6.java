package jus.poc.prodcons.v6;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;

public class TestProdConsV6 extends Simulateur
{
	private static final int PRODUCERS = DEFAULT_CONFIG.getProducers(), CONSUMERS = DEFAULT_CONFIG.getConsumers(), BUFFER_SIZE = DEFAULT_CONFIG.getBufferSize();
	private final ProducteurV6[] producteurs = new ProducteurV6[PRODUCERS];
	private final ConsommateurV6[] consommateurs = new ConsommateurV6[CONSUMERS];
	private final Verificateur verificateur = new Verificateur();
	private final ProdConsV6 tampon;

	public TestProdConsV6(Observateur observateur)
	{
		super(observateur);

		tampon = new ProdConsV6(observateur, verificateur, PRODUCERS, CONSUMERS, BUFFER_SIZE);
	}

	public static void main(String[] args)
	{
		new TestProdConsV6(new Observateur()).start();
	}

	@Override
	protected final void run() throws Exception
	{
		observateur.init(PRODUCERS, CONSUMERS, BUFFER_SIZE);

		for(int i = 0; i < PRODUCERS; i++)
		{
			ProducteurV6 producteur = new ProducteurV6(observateur, tampon);
			producteurs[i] = producteur;
			producteur.start();
		}

		for(int i = 0; i < CONSUMERS; i++)
		{
			ConsommateurV6 consommateur = new ConsommateurV6(observateur, tampon);
			consommateurs[i] = consommateur;
			consommateur.start();
		}

		for(int i = 0; i < PRODUCERS; i++)
		{
			ProducteurV6 producteur = producteurs[i];

			producteur.join();
		}

		for(int i = 0; i < CONSUMERS; i++)
		{
			ConsommateurV6 consommateur = consommateurs[i];

			consommateur.join();
		}

		for(int i = 0; i < PRODUCERS; i++)
		{
			verificateur.endProducer(producteurs[i]);
		}

		verificateur.end(tampon);
	}
}
