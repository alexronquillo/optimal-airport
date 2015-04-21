import java.util.Random;
import java.util.concurrent.BlockingQueue;

import javax.swing.JOptionPane;


public class Arrivals implements Runnable {
	// Following values are constants we should edit
	private double arrivalPeriod;
	private double landingAndTakingOffFactor = 0.006944444;
	private final int PERCENTAGE_OF_PLANES_AS_PASSENGER = 75;
	private final int averageNumberOfFlightsPerDay = 2400;
	private final int NUMBER_OF_SIZES = Airplane.Size.values().length;
	private final int NUMBER_OF_PRIORITIES = Airplane.Priority.values().length;
	
	// These values should never need editing
	private int maxSizeOfArrivals = 0;
	private double meanInterArrivalTime;
	private static double elapsedTime = 0.0;
	private static double startTime = 0.0;
	private Random generator = new Random();
	private int numberOfPlanes = 0;
	private boolean running = true;
	private BlockingQueue<Airplane> arrivalsQueue = null;
	
	public Arrivals(double arrivalPeriod) {
		this.arrivalPeriod = arrivalPeriod;
		this.arrivalsQueue = Airport.getArrivalsQueue();
		meanInterArrivalTime = arrivalPeriod / averageNumberOfFlightsPerDay;
		maxSizeOfArrivals = Airport.ARRIVALS_QUEUE_CAPACITY;
	}
	
	@Override
	public void run() {		
		startTime = System.currentTimeMillis();
		
		while (running) {
			double timeElapsedTotal = (System.currentTimeMillis()-startTime)/1000;
			double arrivalTime = getEstimate(meanInterArrivalTime);
			if (timeElapsedTotal > arrivalPeriod) {
				running = false;
			} else if ((timeElapsedTotal-elapsedTime) > arrivalTime) {
				Airplane plane = generatePlane();
				plane.startWait();
				boolean success = false;
				if (maxSizeOfArrivals >= arrivalsQueue.size()){
					success = arrivalsQueue.offer(plane);
				}
				elapsedTime = timeElapsedTotal;
				if (!success) {
					rejectPlane(plane);
				}
			}
		}
		System.out.println("Arrivals has stopped generating new planes.");

	}
	
	//makes a plane
	public Airplane generatePlane() {
		int salt = generator.nextInt(100);
		String name = getPlaneName();	
		Airplane.Priority priority = getPriority();
		Airplane.Size size = getSize();

		if (salt > PERCENTAGE_OF_PLANES_AS_PASSENGER){					
			System.out.println(name + " with " + priority + " priority " +  "and " + size + " size " + "arrives. Time: " + Airport.getCurrentSimulationTime());
			return new CargoPlane(name, priority, size, Airport.getCurrentSimulationTime() * landingAndTakingOffFactor);
		} else {
			System.out.println(name + " with " + priority + " priority " +  "and " + size + " size " + "arrives. Time: " + Airport.getCurrentSimulationTime());
			return new PassengerPlane(name, priority, size, Airport.getCurrentSimulationTime() * landingAndTakingOffFactor);
		}
	}
	
	public void rejectPlane(Airplane plane ) {
		System.out.println(plane.getName() + " rejected.");
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
		int max = 25;
		int min = 0;
		double salt = .88 + ((double)generator.nextInt((max-min)+1) + min) / 100;
		//JOptionPane.showMessageDialog(null, mean * salt);
		return mean * salt;
	}
}
