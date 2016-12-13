package jus.poc.prodcons.print;

import static jus.poc.prodcons.options.Config.DEFAULT_CONFIG;

import jus.poc.prodcons.Message;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

/**
 * Cette classe permet d'afficher à l'écran plusieurs états du système.<br />
 * Les impressions sont désactivables via l'entrée {@code impressions} de la configuration.
 */
public class Afficheur
{
	private static final boolean PRINTING_ENABLED = DEFAULT_CONFIG.isPrintingEnabled();
	private static final String LINE_SEPARATOR = System.lineSeparator();

	private static void println(String line)
	{
		System.out.println(line + LINE_SEPARATOR + "--------------------------------");
	}

	/**
	 * Affiche la création d'un producteur.
	 *
	 * @param producteur le producteur
	 */
	public static void printNewProducer(_Producteur producteur)
	{
		if(PRINTING_ENABLED)
		{
			String line = "Nouveau producteur :" + LINE_SEPARATOR +
					"- identifiant : n°" + producteur.identification() + LINE_SEPARATOR +
					"- Nombre de messages : " + producteur.nombreDeMessages();

			println(line);
		}
	}

	/**
	 * Affiche la création d'un consommateur.
	 *
	 * @param consommateur le consommateur
	 */
	public static void printNewConsumer(_Consommateur consommateur)
	{
		if(PRINTING_ENABLED)
		{
			String line = "Nouveau consommateur :" + LINE_SEPARATOR +
					"- identifiant : n°" + consommateur.identification();

			println(line);
		}
	}

	/**
	 * Affiche la fin d'un producteur.
	 *
	 * @param producteur le producteur
	 */
	public static void printEndProducer(_Producteur producteur)
	{
		if(PRINTING_ENABLED)
		{
			println("Producteur n°" + producteur.identification() + " terminé");
		}
	}

	/**
	 * Affiche la fin d'un consommateur.
	 *
	 * @param consommateur le consommateur
	 */
	public static void printEndConsumer(_Consommateur consommateur)
	{
		if(PRINTING_ENABLED)
		{
			println("Consommateur n°" + consommateur.identification() + " terminé");
		}
	}

	/**
	 * Affiche un dépôt de message dans le tampon.
	 *
	 * @param producteur le producteur qui dépose
	 * @param message    le message déposé
	 */
	public static void printPut(_Producteur producteur, Message message)
	{
		if(PRINTING_ENABLED)
		{
			String line = "Dépôt dans le tampon :" + LINE_SEPARATOR +
					"- Producteur n°" + producteur.identification() + LINE_SEPARATOR +
					"- Message : " + message;

			println(line);
		}
	}

	/**
	 * Affiche un retrait de message du tampon.
	 *
	 * @param consommateur le consommateur qui retire
	 * @param message      le message retiré
	 */
	public static void printGet(_Consommateur consommateur, Message message)
	{
		if(PRINTING_ENABLED)
		{
			String line = "Retrait du tampon :" + LINE_SEPARATOR +
					"- Consommateur n°" + consommateur.identification() + LINE_SEPARATOR +
					"- Message : " + message;

			println(line);
		}
	}

	/**
	 * Affiche l'état du tampon à un instant.<br />
	 * Chaque emplacement est matérialisé par 3 caractères : {@code RWM}.
	 * <ul>
	 * <li>{@code R} indique le pointeur de lecture</li>
	 * <li>{@code W} indique le pointeur d'écriture</li>
	 * <li>{@code M} indique la présence d'un message</li>
	 * </ul>
	 *
	 * @param buffer    le tampon
	 * @param nextRead  le pointeur de lecture suivante
	 * @param nextWrite le pointeur d'écriture suivante
	 */
	public static void printBuffer(Message[] buffer, int nextRead, int nextWrite)
	{
		if(PRINTING_ENABLED)
		{
			StringBuilder sb = new StringBuilder();

			sb.append(Thread.currentThread().getName()).append("\t[ ");

			for(int i = 0; i < buffer.length; i++)
			{
				sb.append(i == nextRead ? "R" : "-").append(i == nextWrite ? "W" : "-").append(buffer[i] == null ? "-" : "M").append(" ");
			}

			sb.append("]");

			println(sb.toString());
		}
	}

	/**
	 * Affiche la production d'un message.
	 *
	 * @param producteur le producteur qui a produit
	 * @param message    le message produit
	 * @param time       le temps mis en ms
	 */
	public static void printProduction(_Producteur producteur, Message message, int time)
	{
		if(PRINTING_ENABLED)
		{
			int msgs = producteur.nombreDeMessages() - 1;

			String line = "Production d'un message :" + LINE_SEPARATOR +
					"- Producteur n°" + producteur.identification() + LINE_SEPARATOR +
					"- Reste : " + msgs + " message" + (msgs > 1 ? "s" : "") + LINE_SEPARATOR +
					"- Temps : " + time + "ms" + LINE_SEPARATOR +
					"- Message : " + message;

			println(line);
		}
	}

	/**
	 * Affiche la consommation d'un message.
	 *
	 * @param consommateur le consommateur qui a consommé
	 * @param message      le message consommé
	 * @param time         le temps mis en ms
	 */
	public static void printConsumption(_Consommateur consommateur, Message message, int time)
	{
		if(PRINTING_ENABLED)
		{
			String line = "Consommation d'un message :" + LINE_SEPARATOR +
					"- Consommateur n°" + consommateur.identification() + LINE_SEPARATOR +
					"- Temps : " + time + "ms" + LINE_SEPARATOR +
					"- Message : " + message;

			println(line);
		}
	}
}
