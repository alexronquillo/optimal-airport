import java.util.Random;


public class Arrivals implements Runnable{
	private double meanInterArrivalTime = 2.0;
	private Random generator = new Random();
	private double runTime = 120.0;
	private boolean running = true;
	private double elapsedTime = 0.0;
	private double startTime = 0.0;
	private final int PERCENTAGE_OF_PLANES_AS_PASSENGER = 75;
	private int numberOfPlanes = 0;
	private final int NUMBER_OF_SIZES = Airplane.Size.values().length;
	private final int NUMBER_OF_PRIORITIES = Airplane.Priority.values().length;
	
	public Arrivals(AirTrafficController atc, ){
		
	}
	
	public void run() {		
		startTime = System.currentTimeMillis();
		
		//main running loop
		while (running) {
			double timeElapsedTotal = (System.currentTimeMillis()-startTime)/1000;
			double arrivalTime = getEstimate(meanInterArrivalTime);

			//set running to false if over the time
			if (timeElapsedTotal/1000 > runTime){
				System.out.println("Time done");
				running = false;
				continue;
			}
			
			//make cargo and passenger planes at specific times
			if ((timeElapsedTotal-elapsedTime) > arrivalTime){
				generatePlane();
				elapsedTime = timeElapsedTotal;
			}
		}
	}
	
	//makes a plane
	public Airplane generatePlane() {
		Airplane plane = null;
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
	
	public void RejectedPlane() {
		
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
