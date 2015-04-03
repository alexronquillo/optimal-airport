import java.util.Random;
import java.util.concurrent.BlockingQueue;


public class Arrivals implements Runnable{
	// Following values are constants we should edit
	private double meanInterArrivalTime = 2.0;
	private double runTime = 120.0;
	private final int PERCENTAGE_OF_PLANES_AS_PASSENGER = 75;
	
	// These values should never need editing
	private double elapsedTime = 0.0;
	private double startTime = 0.0;
	private Random generator = new Random();
	private int numberOfPlanes = 0;
	private final int NUMBER_OF_SIZES = Airplane.Size.values().length;
	private final int NUMBER_OF_PRIORITIES = Airplane.Priority.values().length;
	private boolean running = true;
	private BlockingQueue<Airplane> arrivalsQueue = null;
	
	public Arrivals() {
		this.arrivalsQueue = Airport.getArrivalsQueue();
	}
	
	@Override
	public void run() {		
		startTime = System.currentTimeMillis();
		
		while (running) {
			double timeElapsedTotal = (System.currentTimeMillis()-startTime)/1000;
			double arrivalTime = getEstimate(meanInterArrivalTime);

			if (timeElapsedTotal/1000 > runTime) {
				System.out.println("Time done");
				running = false;
			} else if ((timeElapsedTotal-elapsedTime) > arrivalTime) {
				boolean success = arrivalsQueue.offer(generatePlane());
				elapsedTime = timeElapsedTotal;
				
				if (!success) {
					rejectPlane();
				}
			}
		}
	}
	
	//makes a plane
	public Airplane generatePlane() {
		int salt = generator.nextInt(100);
		numberOfPlanes++;
		
		if (salt > PERCENTAGE_OF_PLANES_AS_PASSENGER){
			//cargo
			System.out.println("generates cargo");
			return new CargoPlane(getPlaneName(), getPriority(), getSize());
		}
		else {
			//passenger
			System.out.println("generates passenger");
			return new PassengerPlane(getPlaneName(), getPriority(), getSize());
		}
	}
	
	public void rejectPlane() {
		Airport.setRejectedPlanes(Airport.getRejectedPlanes() + 1);
	}
	
	public Airplane.Size getSize() {
		int index = generator.nextInt(NUMBER_OF_SIZES);
		return Airplane.Size.values()[index];
	}
	
	public Airplane.Priority getPriority() {
		int index = generator.nextInt(NUMBER_OF_PRIORITIES);
		return Airplane.Priority.values()[index];
	}
	
	public String getPlaneName() {
		numberOfPlanes++;
		return "Plane " + numberOfPlanes;
	}
	
	//get a values anywhere from 75% to 125% mean value
	public double getEstimate(double mean) {
		double salt = generator.nextDouble() + .75;
		
		return mean * salt;
	}

}
