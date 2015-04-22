import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class AirplaneArrivalsQueue
{
	private BlockingQueue<Airplane> arrivals = new PriorityBlockingQueue<>();
	private volatile int capacity;
	private volatile int size;

	public AirplaneArrivalsQueue(int capacity)
	{
		this.capacity = capacity;
		size = 0;
	}

	public boolean offer(Airplane plane)
	{
		if (remainingCapacity() == 0)
		{
			return false;
		}
		else if (remainingCapacity() > 0)
		{
			arrivals.offer(plane);
			++size;
			return true;
		}
		else
		{
			System.out.println("Arrivals Capacity Overflow");
			return false;
		}
	}

	public Airplane poll()
	{
		if (size <= 0)
		{
			return null;
		}
		else
		{
			--size;
			return arrivals.poll();	
		}
	}

	public int getCapacity()
	{
		return capacity;
	}

	public int size()
	{
		return size;
	}

	public int remainingCapacity()
	{
		return capacity - size;
	}
}
