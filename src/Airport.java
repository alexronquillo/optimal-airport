import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Airport {
	// Following values are constants we should edit
	public static final double SIMULATION_PERIOD = .5;
	public static final int ARRIVALS_QUEUE_CAPACITY = 550;
	private static final double ARRIVAL_PERIOD = .25;
	private static final int NUM_RUNWAYS = 2;
	private static final int NUM_GATES = 35; //207;
	private static final int NUM_BAYS = 30; //28;	
	private static final int LANDED_QUEUE_CAPACITY = 100;
	private static final int DEPARTURE_QUEUE_CAPACITY = 15;	
	
	private static double elapsedTime = 0.0;
	private static double startTime = System.currentTimeMillis();	
	private static double runwayTotal = 0;
	private static double cumulativeSojournTime = 0;
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
		Thread arrivalsThread = new Thread(new Arrivals(ARRIVAL_PERIOD));
		arrivalsThread.start();
		
		Thread atcThread = new Thread(airTrafficController);
		atcThread.start();
		
		Thread gmcThread = new Thread(groundMovementController);
		gmcThread.start();	
		
		boolean running = true;
		while (running) {
			elapsedTime = (System.currentTimeMillis()-startTime)/1000;
			if (elapsedTime > SIMULATION_PERIOD) {
				System.out.println("Simulation has completed execution.");
				running = false;
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
		double runwayUtil = (runwayTotal / NUM_RUNWAYS) / SIMULATION_PERIOD;
		
		//get average gate utilization
	    average = 0;
		for (Gate g : gates) {
		     	average += g.getTotalWait();
		}
		average /= NUM_GATES;
		double gateUtilization = average / SIMULATION_PERIOD;
		
		//get average bay utilization
		average = 0;
		for (CargoBay b : bays) {
	     	average += b.getTotalWait();
		}
		average /= NUM_BAYS;
		double bayUtilization = average / SIMULATION_PERIOD;
		
		//get rejected planes
		rejectedPlanes = arrivalsQueue.size() + getRejectedPlanes();
		
		try {
			String statisticsOutput = "=================================================\n"+
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
                    "=================================================";
			
			FileWriter fileWriter = new FileWriter(new File("airport_stats.txt"));
			fileWriter.write(statisticsOutput);
			fileWriter.close();
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage() + "\n\nStack Trace: " + e.getStackTrace());
		}		
		
		System.exit(0);
	}

	public static double getCurrentSimulationTime() {
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
			gates[i] = new Gate("Gate " + i, SIMULATION_PERIOD);
		}
		return gates;
	}
	
	private static CargoBay[] initializeCargoBays() {
		CargoBay[] bays = new CargoBay[NUM_BAYS];
		for (int i = 0; i < NUM_BAYS; ++i) {
			bays[i] = new CargoBay("CargoBay " + i, SIMULATION_PERIOD);
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
	
	public static void addPlaneTimes(ArrayList<Double> list) {
		totalArrivalsQueueTime += list.get(0);
		totalGroundQueueTime += list.get(1);
		totalDepartureQueueTime += list.get(2);
	}
}
