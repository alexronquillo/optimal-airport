import java.util.Random;
import java.util.concurrent.BlockingQueue;

import javax.swing.JOptionPane;


public class Arrivals implements Runnable{
	// Following values are constants we should edit
	private double runTime;
	private double simTime;
	private double landingAndTakingOffFactor = 0.006944444;
	private final int PERCENTAGE_OF_PLANES_AS_PASSENGER = 75;
	private final int averageNumberOfFlightsPerDay = 2400;
	private final int NUMBER_OF_SIZES = Airplane.Size.values().length;
	private final int NUMBER_OF_PRIORITIES = Airplane.Priority.values().length;
	
	// These values should never need editing
	private double meanInterArrivalTime = runTime / averageNumberOfFlightsPerDay;
	private static double elapsedTime = 0.0;
	private static double startTime = 0.0;
	private Random generator = new Random();
	private int numberOfPlanes = 0;
	private boolean running = true;
	private BlockingQueue<Airplane> arrivalsQueue = null;
	
	public Arrivals(double runTime, double simTime) {
		this.runTime = runTime;
		this.simTime = simTime;
		this.arrivalsQueue = Airport.getArrivalsQueue();
		meanInterArrivalTime = runTime / averageNumberOfFlightsPerDay;
	}
	
	@Override
	public void run() {		
		startTime = System.currentTimeMillis();
		
		while (running) {
			double timeElapsedTotal = (System.currentTimeMillis()-startTime)/1000;
			double arrivalTime = getEstimate(meanInterArrivalTime);
			if (timeElapsedTotal > runTime) {
				running = false;
				continue;
			} else if ((timeElapsedTotal-elapsedTime) > arrivalTime) {
				Airplane plane = generatePlane();
				plane.startWait();
				boolean success = arrivalsQueue.offer(plane);
				elapsedTime = timeElapsedTotal;
				if (!success) {
					rejectPlane();
				}
			}
		}
		System.out.println("Arrivals has stopped generating new planes.");

	}
	
	//makes a plane
	public Airplane generatePlane() {
		int salt = generator.nextInt(100);
		String name = getPlaneName();	
		
		if (salt > PERCENTAGE_OF_PLANES_AS_PASSENGER){					
			System.out.println(name + " with " + getPriority() + " priority " +  "and " + getSize() + " size " + "arrives. Time: " + Airport.getSimulationTime());
			return new CargoPlane(name, getPriority(), getSize(), simTime * landingAndTakingOffFactor);
		} else {
			System.out.println(name + " with " + getPriority() + " priority " +  "and " + getSize() + " size " + "arrives. Time: " + Airport.getSimulationTime());
			return new PassengerPlane(name, getPriority(), getSize(), simTime * landingAndTakingOffFactor);
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
