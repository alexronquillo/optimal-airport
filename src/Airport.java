import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import javax.swing.JOptionPane;

public class Airport {
	// Following values are constants we should edit
	// private final int PERCENTAGE_OF_PLANES_AS_PASSENGER = 75;
	// private final int averageNumberOfFlightsPerDay = 2400;
	private static double simulationPeriod = 10;
	private static double arrivalPeriod = 5;	
	private static double elapsedTime = 0.0;
	private static double startTime = System.currentTimeMillis();
	
	private static double runwayTotal = 0;
	private static double cumulativeSojournTime = 0;
	private static final int NUM_RUNWAYS = 2;
	private static final int NUM_GATES = 10;
	private static final int NUM_BAYS = 10;
	private static final int ARRIVALS_QUEUE_CAPACITY = 150;
	private static final int LANDED_QUEUE_CAPACITY = 6;
	private static final int DEPARTURE_QUEUE_CAPACITY = 6;
	private static int rejectedPlanes = 0;
	private static BlockingQueue<Runway> runways = initializeRunways();
	private static Gate[] gates = initializeGates();
	private static CargoBay[] bays = initializeCargoBays();
	private static BlockingQueue<Airplane> arrivalsQueue = new PriorityBlockingQueue<>(ARRIVALS_QUEUE_CAPACITY);
	private static BlockingQueue<Airplane> landedQueue = new ArrayBlockingQueue<>(LANDED_QUEUE_CAPACITY);
	private static BlockingQueue<Airplane> departureQueue = new ArrayBlockingQueue<>(DEPARTURE_QUEUE_CAPACITY);
	private static AirTrafficController airTrafficController = new AirTrafficController();
	private static GroundMovementController groundMovementController = new GroundMovementController();

	private static double totalArrivalsQueueTime = 0;
	private static double totalGroundQueueTime = 0;
	private static double totalDepartureQueueTime = 0;
	
	public static void main(String[] args) {
		AirportThread arrivalsThread = new AirportThread(new Arrivals(arrivalPeriod));
		arrivalsThread.start();
		
		AirportThread atcThread = new AirportThread(airTrafficController);
		atcThread.start();
		
		AirportThread gmcThread = new AirportThread(groundMovementController);
		gmcThread.start();	
		
		boolean running = true;
		while (running) {
			elapsedTime = (System.currentTimeMillis()-startTime)/1000;
			if (elapsedTime > simulationPeriod) {
				System.out.println("Simulation has completed execution.");
				running = false;
				arrivalsThread.terminate();
				atcThread.terminate();
				gmcThread.terminate();
				continue;
			}
		}
		closingProcedures();
	}

	//do closing things
	private static void closingProcedures() {
		double average = 0;
		
		//get average sojourn time
		cumulativeSojournTime /= airTrafficController.getNumberOfPlanes();
		
		
		//get average runway utilization
		double runwayUtil = (runwayTotal / NUM_RUNWAYS) / simulationPeriod;
		
		//get average gate utilization
	    average = 0;
		for (Gate g : gates) {
		     	average += g.getTotalWait();
		}
		average /= NUM_GATES;
		double gateUtilization = 1 - (average / simulationPeriod);
		
		//get average bay utilization
		average = 0;
		for (CargoBay b : bays) {
	     	average += b.getTotalWait();
		}
		average /= NUM_BAYS;
		double bayUtilization = 1- (average / simulationPeriod);
		
		//get rejected planes
		rejectedPlanes = arrivalsQueue.size() + getRejectedPlanes();
		
		//output all these things
		System.out.println("=================================================\n"+
	                       "Optimal Airport Simulation\n" + 
				           "Simulation has completed. Results Follow:\n" +
	                       "Average Gate Utilization: " + gateUtilization + "\n" + 
	                       "Average Bay Utilization: " + bayUtilization + "\n" +
	                       "Average Arrivals Queue Time: " + (totalArrivalsQueueTime / airTrafficController.getNumberOfPlanes()) + "\n" +
	                       "Average Landed Queue Time: " + (totalGroundQueueTime / airTrafficController.getNumberOfPlanes()) + "\n" +
	                       "Average Departure Queue Time: " + (totalDepartureQueueTime / airTrafficController.getNumberOfPlanes()) + "\n" +
	                       "Rejected planes: " + rejectedPlanes + "\n" + 
	                       "Planes Serviced: " + airTrafficController.getNumberOfPlanes() + "\n" +
	                       "Average Sojourn Time: " + cumulativeSojournTime + "\n" +
	                       "Average Runway Utilization: " + runwayUtil + "\n" +
                           "=================================================");
		
		System.exit(0);
	}

	public static double getSimulationTime() {
		return (double)(System.currentTimeMillis() - startTime) / 1000;
	}
	
	public static int getRejectedPlanes() {
		return rejectedPlanes;
	}
	
	public static void setRejectedPlanes(int rejectedPlanes) {
		Airport.rejectedPlanes = rejectedPlanes;
	}
	
	public static BlockingQueue<Runway> getRunways() {
		return runways;
	}
	
	public static Gate[] getGates() {
		return gates;
	}
	
	public static CargoBay[] getBays() {
		return bays;
	}
	
	public static AirTrafficController getAirTrafficController() {
		return airTrafficController;
	}
	
	public static GroundMovementController getGroundMovementController() {
		return groundMovementController;
	}
	
	public static BlockingQueue<Airplane> getArrivalsQueue() {
		return arrivalsQueue;
	}
	
	public static BlockingQueue<Airplane> getLandedQueue() {
		return landedQueue;
	}
	
	public static BlockingQueue<Airplane> getDepartureQueue() {
		return departureQueue;
	}
	
	public static boolean departureQueueFull() {
		return departureQueue.remainingCapacity() == 0;
	}

	private static BlockingQueue<Runway> initializeRunways() {
		BlockingQueue<Runway> runways = new ArrayBlockingQueue<>(NUM_RUNWAYS);
		for (int i = 0; i < NUM_RUNWAYS; ++i) {
			runways.add(new Runway("Runway " + (i + 1)));
		}
		
		return runways;
	}
	
	private static Gate[] initializeGates() {
		Gate[] gates = new Gate[NUM_GATES];
		for (int i = 0; i < NUM_GATES; ++i) {
			gates[i] = new Gate("Gate " + i, simulationPeriod);
		}
		return gates;
	}
	
	private static CargoBay[] initializeCargoBays() {
		CargoBay[] bays = new CargoBay[NUM_BAYS];
		for (int i = 0; i < NUM_BAYS; ++i) {
			bays[i] = new CargoBay("CargoBay " + i, simulationPeriod);
		}
		return bays;
	}
	
	public static double getElapsedTime(){
		return elapsedTime;	
	}

	public static double getStartTime(){
		return startTime;
	}
	public static void addMySTime(double time) {
		cumulativeSojournTime += time;
	}
	
	public static void addRunwayTotal(double d) {
		runwayTotal += d;
	}
	
	public double getSimTime () {
		return simulationPeriod;
	}
	
	public static int getArrivalsMaxSize() {
		return ARRIVALS_QUEUE_CAPACITY;
	}
	
	public static void addPlaneTimes(ArrayList<Double> list) {
		totalArrivalsQueueTime += list.get(0);
		totalGroundQueueTime += list.get(1);
		totalDepartureQueueTime += list.get(2);
	}
}
