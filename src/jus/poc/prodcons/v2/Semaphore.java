package jus.poc.prodcons.v2;

public class Semaphore
{
	private final int permits;
	private int c;

	public Semaphore(int permits)
	{
		this.permits = permits;
		c = permits;
	}

	public synchronized void P() throws InterruptedException
	{
		c--;

		if(c < 0)
		{
			wait();
		}
	}

	public synchronized void V()
	{
		c++;

		if(c <= 0)
		{
			notify();
		}
	}

	public int permits()
	{
		return permits;
	}

	public int count()
	{
		return c;
	}
}
