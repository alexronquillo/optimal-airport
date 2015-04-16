import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Airport {
	// Following values are constants we should edit
	private static double simTime = 10.0;
	private static double runTime = 5.0;
	private final int PERCENTAGE_OF_PLANES_AS_PASSENGER = 75;
	private final int averageNumberOfFlightsPerDay = 2400;
	private static double elapsedTime = 0.0;
	private static double startTime = System.currentTimeMillis();
	
	private static final int NUM_RUNWAYS = 2;
	private static final int NUM_GATES = 5;
	private static final int NUM_BAYS = 5;
	private static final int ARRIVALS_QUEUE_CAPACITY = 2;
	private static final int LANDED_QUEUE_CAPACITY = 2;
	private static final int DEPARTURE_QUEUE_CAPACITY = 1;
	private static int rejectedPlanes = 0;
	private static BlockingQueue<Runway> runways = initializeRunways();
	private static Gate[] gates = initializeGates();
	private static CargoBay[] bays = initializeCargoBays();
	private static BlockingQueue<Airplane> arrivalsQueue = new PriorityBlockingQueue<>(ARRIVALS_QUEUE_CAPACITY);
	private static BlockingQueue<Airplane> landedQueue = new ArrayBlockingQueue<>(LANDED_QUEUE_CAPACITY);
	private static BlockingQueue<Airplane> departureQueue = new ArrayBlockingQueue<>(DEPARTURE_QUEUE_CAPACITY);
	private static AirTrafficController airTrafficController = new AirTrafficController();
	private static GroundMovementController groundMovementController = new GroundMovementController();

	public static void main(String[] args) {
		Thread arrivalsThread = new Thread(new Arrivals(runTime, simTime));
		arrivalsThread.start();
		
		Thread atcThread = new Thread(airTrafficController);
		atcThread.start();
		
		Thread gmcThread = new Thread(groundMovementController);
		gmcThread.start();		
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
			runways.add(new Runway());
		}
		
		return runways;
	}
	
	private static Gate[] initializeGates() {
		Gate[] gates = new Gate[NUM_GATES];
		for (int i = 0; i < NUM_GATES; ++i) {
			gates[i] = new Gate("Gate " + i, simTime);
		}
		return gates;
	}
	
	private static CargoBay[] initializeCargoBays() {
		CargoBay[] bays = new CargoBay[NUM_BAYS];
		for (int i = 0; i < NUM_BAYS; ++i) {
			bays[i] = new CargoBay("CargoBay " + i, simTime);
		}
		return bays;
	}
	
	public static double getElapsedTime(){
		return elapsedTime;	
	}

	public static double getStartTime(){
		return startTime;
	}
}
