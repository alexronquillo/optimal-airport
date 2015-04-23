import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

import javax.swing.JOptionPane;

public class Airport {
	// Following values are constants we should edit
	public static final int ARRIVALS_QUEUE_CAPACITY = 550;
<<<<<<< HEAD
	private static final int NUMBER_OF_PLANES_PER_DAY = 2400;
=======
	private static final int NUMBER_OF_PLANES_PER_DAY = 10;
	private static final double ARRIVAL_PERIOD = 500;
>>>>>>> e16880c7800ea2fa78d93b70316c09c00caed120
	private static final int NUM_RUNWAYS = 5;
	private static final int NUM_GATES = 207;
	private static final int NUM_BAYS = 28;	
	private static final int LANDED_QUEUE_CAPACITY = 300;
	private static final int DEPARTURE_QUEUE_CAPACITY = 15;	
	private static final int PERCENTAGE_OF_PLANES_AS_PASSENGER = 75;
	private static final int NUMBER_OF_SIZES = Airplane.Size.values().length;
	private static final int NUMBER_OF_PRIORITIES = Airplane.Priority.values().length;
	private static double LANDING_AND_TAKEOFF_FACTOR = 0.006944444;
	
	private static AirplaneArrivalsQueue arrivalsQueue = new AirplaneArrivalsQueue(ARRIVALS_QUEUE_CAPACITY);
	private static BlockingQueue<Airplane> landedQueue = new ArrayBlockingQueue<>(LANDED_QUEUE_CAPACITY);
	private static BlockingQueue<Airplane> departureQueue = new ArrayBlockingQueue<>(DEPARTURE_QUEUE_CAPACITY);
	private static BlockingQueue<Runway> runways = initializeRunways();
	private static Gate[] gates = initializeGates();
	private static CargoBay[] bays = initializeCargoBays();
	private static double runwayTotal = 0;
	private static double totalSimulationPeriod = 0;
	private volatile static int rejectedPlanes = 0;
	private volatile static int numPassengerPlanes = 0;
	private volatile static int numCargoPlanes = 0;

	private static double startTime = System.currentTimeMillis();	
	private static double cumulativeSojournTime = 0;
	private static double totalArrivalsQueueTime = 0;
	private static double totalGroundQueueTime = 0;
	private static double totalDepartureQueueTime = 0;
		
	public static void main(String[] args) 
	{
		final Timer arrivalsTimer = new Timer();
		arrivalsTimer.schedule(new TimerTask() { 
			@Override
			public void run()
			{
				if (numPassengerPlanes + numCargoPlanes < NUMBER_OF_PLANES_PER_DAY)
				{
					Airplane testPlane = generatePlane();
					++totalPlaneAttempts;
					if (!arrivalsQueue.offer(testPlane))
					{
						System.out.println("Plane Rejected");
						++rejectedPlanes;
					}
				}
				else
				{
					arrivalsTimer.cancel();
					while (arrivalsQueue.size() > 0 || landedQueue.size() > 0 || departureQueue.size() > 0 || !allGatesAvailable() || !allBaysAvailable());
					closingProcedures();
				}
			}
		}, 0, 1);

		Thread atcThread = new Thread(new AirTrafficController());
		atcThread.start();

		Thread gmcThread = new Thread(new GroundMovementController());
		gmcThread.start();
	}
	
	private static int getAttempts() {
		return totalPlaneAttempts;
	}

	//do closing things
	private static void closingProcedures() {
		double average = 0;
		
		//get average sojourn time
		cumulativeSojournTime /= (numPassengerPlanes + numCargoPlanes);
		
		
		//get average runway utilization
		double runwayUtil = (runwayTotal / NUM_RUNWAYS) / getCurrentSimulationTime();
		
		//get average gate utilization
	    average = 0;
		for (Gate g : gates) {
		     	average += g.getTotalWait();
		}
		average /= NUM_GATES;
		double gateUtilization = average / getCurrentSimulationTime();
		
		//get average bay utilization
		average = 0;
		for (CargoBay b : bays) {
	     	average += b.getTotalWait();
		}
		average /= NUM_BAYS;
		double bayUtilization = average / getCurrentSimulationTime();
		
		try {
			String statisticsOutput = "=================================================\n"+
                    "Optimal Airport Simulation\n" + 
			           "Simulation has completed. Results Follow:\n" +
                    "Average Gate Utilization: " + gateUtilization + "\n" + 
                    "Average Bay Utilization: " + bayUtilization + "\n" +
                    "Average Arrivals Queue Time: " + (totalArrivalsQueueTime / (numPassengerPlanes + numCargoPlanes)) + "\n" +
                    "Average Landed Queue Time: " + (totalGroundQueueTime / (numPassengerPlanes + numCargoPlanes)) + "\n" +
                    "Average Departure Queue Time: " + (totalDepartureQueueTime / (numPassengerPlanes + numCargoPlanes)) + "\n" +
                    "Rejected planes: " + rejectedPlanes + "\n" + 
                    "Planes Serviced: " + airTrafficController.getNumberOfPlanes() + "\n" +
					"Number of Passenger Planes: " + numPassengerPlanes + "\n" +
					"Number of Cargo Planes: " + numCargoPlanes + "\n" +
                    "Average Sojourn Time: " + cumulativeSojournTime + "\n" +
                    "Average Runway Utilization: " + runwayUtil + "\n" +
                    "Sim Time: " + getCurrentSimulationTime() + "\n" +

                    "=================================================";
			
			FileWriter fileWriter = new FileWriter(new File("airport_stats.txt"));
			fileWriter.write(statisticsOutput);
			fileWriter.close();
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage() + "\n\nStack Trace: " + e.getStackTrace());
		}		
		
		System.exit(0);
	}

	public static Airplane.Size getSize() {
		int index = new Random().nextInt(NUMBER_OF_SIZES);
		return Airplane.Size.values()[index];
	}
	
	public static Airplane.Priority getPriority() {
		int index = new Random().nextInt(NUMBER_OF_PRIORITIES);
		return Airplane.Priority.values()[index];
	}
	
	public static Airplane generatePlane() {
		int salt = new Random().nextInt(100);

		Airplane newPlane = null;
		if (salt > PERCENTAGE_OF_PLANES_AS_PASSENGER) {					
			newPlane = new CargoPlane("Cargo Plane " + numCargoPlanes++, getPriority(), getSize(), SIMULATION_PERIOD * LANDING_AND_TAKEOFF_FACTOR);
		} else {
			newPlane = new PassengerPlane("Passenger Plane " + numPassengerPlanes++, getPriority(), getSize(), SIMULATION_PERIOD * LANDING_AND_TAKEOFF_FACTOR);
		}

		System.out.println(newPlane.getName() + " with " + 
							newPlane.getPriority() + " priority " +  
							"and " + newPlane.getSize() + " size " + 
							"arrives. Time: " + getCurrentSimulationTime());
		return newPlane;
	}

	public static boolean allGatesAvailable()
	{
		for (Gate gate : gates)
		{
			if (!gate.isAvailable())
			{
				return false;
			}
		}
		return true;
	}

	public static boolean allBaysAvailable()
	{
		for (CargoBay bay : bays)
		{
			if (!bay.isAvailable())
			{
				return false;
			}
		}
		return true;
	}

	public static double getCurrentSimulationTime() {
		return (double)(System.currentTimeMillis() - startTime);
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
	
	public static AirplaneArrivalsQueue getArrivalsQueue() {
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
