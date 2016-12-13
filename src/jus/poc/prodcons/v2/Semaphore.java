package jus.poc.prodcons.v2;

public class Semaphore
{
	private final int permits;
	private int c;

	public Semaphore(int permits)
	{
		this.permits = c = permits;
	}

	public synchronized void acquire() throws InterruptedException
	{
		c--;

		if(c < 0)
		{
			wait();
		}
	}

	public synchronized void release()
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

	public synchronized int count()
	{
		return c;
	}
}
