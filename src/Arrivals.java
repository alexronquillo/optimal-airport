import java.util.Random;
import java.util.concurrent.BlockingQueue;

import javax.swing.JOptionPane;


public class Arrivals implements Runnable {
	private final int PERCENTAGE_OF_PLANES_AS_PASSENGER = 75;
	private final int AVERAGE_NUMBER_OF_FLIGHTS_PER_DAY = 2400;
	private final int NUMBER_OF_SIZES = Airplane.Size.values().length;
	private final int NUMBER_OF_PRIORITIES = Airplane.Priority.values().length;
	private final double LANDING_FACTOR = 0.006944444;

	private Random generator = new Random();
	private int maxSizeOfArrivals = 0;
	private int numberOfPlanes = 0;
	private double meanInterArrivalTime;
	
	public Arrivals() {
		meanInterArrivalTime = Airport.ARRIVAL_PERIOD / AVERAGE_NUMBER_OF_FLIGHTS_PER_DAY;
		maxSizeOfArrivals = Airport.ARRIVALS_QUEUE_CAPACITY;
	}
	
	@Override
	public void run() {		
		BlockingQueue<Airplane> arrivalsQueue = Airport.getArrivalsQueue();
		double totalElapsedTime = 0;
		double startTime = System.currentTimeMillis();
		double elapsedTimeSincePlaneGenerated = 0;

		if (arrivalsQueue != null) {
			while ((totalElapsedTime = (System.currentTimeMillis() - startTime) / 1000) <= Airport.ARRIVAL_PERIOD) {
				double arrivalTime = getArrivalTime();

				if ((totalElapsedTime-elapsedTimeSincePlaneGenerated) > arrivalTime) {
					Airplane plane = generatePlane();
					plane.startWait();

					boolean success = false;
					if (maxSizeOfArrivals >= arrivalsQueue.size()) {
						success = arrivalsQueue.offer(plane);
					}
					elapsedTimeSincePlaneGenerated = totalElapsedTime;
					if (!success) {
						rejectPlane(plane);
					}
				}
			}

			System.out.println("Arrivals has stopped generating new planes.");
		} else {
			System.out.println("Error: Arrivals queue is null");
		}
	}
	
	public Airplane generatePlane() {
		int salt = generator.nextInt(100);
		String name = getNextPlaneName();	
		Airplane.Priority priority = getRandomPriority();
		Airplane.Size size = getRandomSize();

		if (salt > PERCENTAGE_OF_PLANES_AS_PASSENGER) {					
			System.out.println(name + " with " + priority + " priority " +  "and " + size + " size " + "arrives. Time: " + Airport.getCurrentSimulationTime());
			return new CargoPlane(name, priority, size, Airport.getCurrentSimulationTime() * LANDING_FACTOR);
		} else {
			System.out.println(name + " with " + priority + " priority " +  "and " + size + " size " + "arrives. Time: " + Airport.getCurrentSimulationTime());
			return new PassengerPlane(name, priority, size, Airport.getCurrentSimulationTime() * LANDING_FACTOR);
		}
	}
	
	public void rejectPlane(Airplane plane ) {
		System.out.println(plane.getName() + " rejected.");
		Airport.setRejectedPlanes(Airport.getRejectedPlanes() + 1);
	}
	
	public Airplane.Size getRandomSize() {
		int index = generator.nextInt(NUMBER_OF_SIZES);
		return Airplane.Size.values()[index];
	}
	
	public Airplane.Priority getRandomPriority() {
		int index = generator.nextInt(NUMBER_OF_PRIORITIES);
		return Airplane.Priority.values()[index];
	}
	
	public String getNextPlaneName() {
		numberOfPlanes++;
		return "Plane " + numberOfPlanes;
	}
	
	public double getArrivalTime() {
		int max = 25;
		int min = 0;

		// Get a value anywhere from 75% to 125% of the mean value
		double salt = .88 + ((double)generator.nextInt((max-min)+1) + min) / 100;

		return meanInterArrivalTime * salt;
	}
}
