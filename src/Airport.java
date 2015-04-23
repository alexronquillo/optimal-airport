import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public class Airport {
	public static final int ARRIVALS_QUEUE_CAPACITY = 100;
	private static final int LANDED_QUEUE_CAPACITY = 300;
	private static final int DEPARTURE_QUEUE_CAPACITY = 15;	
	private static final int NUMBER_OF_PLANES_PER_DAY = 300;
	private static final int NUM_RUNWAYS = 5;
	private static final int NUM_GATES = 207;
	private static final int NUM_BAYS = 28;	
	private static final int PERCENTAGE_OF_PLANES_AS_PASSENGER = 75;
	private static final int NUMBER_OF_SIZES = Airplane.Size.values().length;
	private static final int NUMBER_OF_PRIORITIES = Airplane.Priority.values().length;
	private static final String BAR = "--------------------------------------------------\n";
	private static final String DOUBLE_BAR = "==================================================\n";
	
	private static AirplaneArrivalsQueue arrivalsQueue = new AirplaneArrivalsQueue(ARRIVALS_QUEUE_CAPACITY);
	private static BlockingQueue<Airplane> landedQueue = new ArrayBlockingQueue<>(LANDED_QUEUE_CAPACITY);
	private static BlockingQueue<Airplane> departureQueue = new ArrayBlockingQueue<>(DEPARTURE_QUEUE_CAPACITY);
	private static BlockingQueue<Runway> runways = initializeRunways();
	private static Gate[] gates = initializeGates();
	private static CargoBay[] bays = initializeCargoBays();
	private static double runwayTotal = 0;
	private static int rejectedPlanes = 0;
	private static int numPassengerPlanes = 0;
	private static int numCargoPlanes = 0;
	private static int numLargePlanes = 0;
	private static int numMediumPlanes = 0;
	private static int numSmallPlanes = 0;
	private static int throughput = 0;

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
					if (!arrivalsQueue.offer(testPlane))
					{
						System.out.println("Plane Rejected");
						++rejectedPlanes;
					}
					else
					{
						++throughput;
					}
				}
				else
				{
					arrivalsTimer.cancel();
					while (arrivalsQueue.size() > 0 || landedQueue.size() > 0 || departureQueue.size() > 0 || !allGatesAvailable() || !allBaysAvailable());
					closingProcedures(getCurrentSimulationTime());
				}
			}
		}, 0, 1);

		Thread atcThread = new Thread(new AirTrafficController());
		atcThread.start();

		Thread gmcThread = new Thread(new GroundMovementController());
		gmcThread.start();
	}
	
	private static void closingProcedures(double simulationPeriod) {
		cumulativeSojournTime /= (numPassengerPlanes + numCargoPlanes);
		
		try {
			String statisticsOutput = "";
		
			statisticsOutput += DOUBLE_BAR;
            statisticsOutput += "Optimal Airport Simulation\n";
			statisticsOutput += "Simulation has completed. Results:\n";
			statisticsOutput += DOUBLE_BAR;
			statisticsOutput += getGateStatistics(simulationPeriod);
			statisticsOutput += getCargoBayStatistics(simulationPeriod);
			statisticsOutput += getRunwayStatistics(simulationPeriod);
			statisticsOutput += BAR;
			statisticsOutput += "Average Arrivals Queue Time: " + (totalArrivalsQueueTime / (numPassengerPlanes + numCargoPlanes)) + " ms\n";
			statisticsOutput += "Average Landed Queue Time: " + (totalGroundQueueTime / (numPassengerPlanes + numCargoPlanes)) + " ms\n";
			statisticsOutput += "Average Departure Queue Time: " + (totalDepartureQueueTime / (numPassengerPlanes + numCargoPlanes)) + " ms\n";
			statisticsOutput += "Average Sojourn Time: " + cumulativeSojournTime + " ms\n";
			statisticsOutput += BAR;
			statisticsOutput += "Total Simulation Time: " + simulationPeriod + " ms\n";
			statisticsOutput += "Throughput: " + throughput + "\n";
			statisticsOutput += "Rejected Planes: " + rejectedPlanes + "\n";
			statisticsOutput += BAR;
			statisticsOutput += "Number of Passenger Planes: " + numPassengerPlanes + "\n";
			statisticsOutput += "Number of Cargo Planes: " + numCargoPlanes + "\n";
			statisticsOutput += "Number of Small Planes: " + numSmallPlanes + "\n";
			statisticsOutput += "Number of Medium Planes: " + numMediumPlanes + "\n";
			statisticsOutput += "Number of Large Planes: " + numLargePlanes + "\n";
			statisticsOutput += BAR;
			
			FileWriter fileWriter = new FileWriter(new File("airport_stats.txt"));
			fileWriter.write(statisticsOutput);
			fileWriter.close();
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage() + "\n\nStack Trace: " + e.getStackTrace());
		}		
		
		System.exit(0);
	}

	private static String getRunwayStatistics(double simulationPeriod) {
		String runwayStats = "";
		while (runways.size() > 0)
		{
			runwayStats += "Runway Total Time in Use: " + runways.poll().getTimeInUse() + "\n";
		}
		double runwayUtil = (runwayTotal / NUM_RUNWAYS) / simulationPeriod;

		try
		{
			FileWriter fileWriter = new FileWriter(new File("runway_stats.txt"));
			fileWriter.write(runwayStats);
			fileWriter.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return "Average Runway Utilization: " + runwayUtil + "\n";
	}

	private static String getCargoBayStatistics(double simulationPeriod) {
		String cargoBayStats = "";
		double sum = 0;
		for (CargoBay b : bays) {
			cargoBayStats += b.getName() + " Total Time in Use: " + b.getTotalUsageTime() + "\n";
			sum += b.getTotalUsageTime();
		}
		double average = (sum /NUM_BAYS);
		double bayUtilization = (average / simulationPeriod);

		try
		{
			FileWriter fileWriter = new FileWriter(new File("cargobay_stats.txt"));
			fileWriter.write(cargoBayStats);
			fileWriter.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return "Average Bay Utilization: " + bayUtilization + "\n";
	}

	private static String getGateStatistics(double simulationPeriod) {
		String gateStats = "";
		double sum = 0;
		for (Gate g : gates) {
				gateStats += g.getName() + " Total Time in Use: " + g.getTotalUsageTime() + "\n";
				sum += g.getTotalUsageTime();
		}
		double average = (sum / NUM_GATES);
		double gateUtilization = (average / simulationPeriod);	

		try
		{
			FileWriter fileWriter = new FileWriter(new File("gate_stats.txt"));
			fileWriter.write(gateStats);
			fileWriter.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return "Average Gate Utilization: " + gateUtilization + "\n";
	}

	public static Airplane.Size getSize() {
		int index = new Random().nextInt(NUMBER_OF_SIZES);
		Airplane.Size size = Airplane.Size.values()[index];
		switch (size) {
			case LARGE:
				++numLargePlanes;
				break;
			case MEDIUM:
				++numMediumPlanes;
				break;
			case SMALL:
				++numSmallPlanes;
				break;
		}

		return size;
	}
	
	public static Airplane.Priority getPriority() {
		int index = new Random().nextInt(NUMBER_OF_PRIORITIES);
		return Airplane.Priority.values()[index];
	}
	
	public static Airplane generatePlane() {
		int salt = new Random().nextInt(100);

		Airplane newPlane = null;
		if (salt > PERCENTAGE_OF_PLANES_AS_PASSENGER) {					
			newPlane = new CargoPlane("Cargo Plane " + numCargoPlanes++, getPriority(), getSize());
		} else {
			newPlane = new PassengerPlane("Passenger Plane " + numPassengerPlanes++, getPriority(), getSize());
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
			gates[i] = new Gate("Gate " + i);
		}
		return gates;
	}
	
	private static CargoBay[] initializeCargoBays() {
		CargoBay[] bays = new CargoBay[NUM_BAYS];
		for (int i = 0; i < NUM_BAYS; ++i) {
			bays[i] = new CargoBay("CargoBay " + i);
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
