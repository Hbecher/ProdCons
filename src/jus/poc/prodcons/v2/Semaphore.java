package jus.poc.prodcons.v2;

public class Semaphore
{
	/** Le nombre de threads pouvant initialement passer le sémaphore */
	private final int permits;
	/** Le compteur du sémaphore */
	private int c;

	public Semaphore(int permits)
	{
		this.permits = c = permits;
	}

	public synchronized void acquire() throws InterruptedException
	{
		// on décrémente le compteur
		c--;

		if(c < 0)
		{
			// s'il est négatif, c'est-à-dire qu'il n'y a plus de ressources disponibles
			// et éventuellement des threas en attente, on se bloque
			wait();
		}
	}

	public synchronized void release()
	{
		// on incrémente le compteur
		c++;

		if(c <= 0)
		{
			// s'il reste des threads en attente, on en réveille un
			notify();
		}
	}

	public int permits()
	{
		return permits;
	}

	public synchronized int count()
	{
		return c;
	}
}
