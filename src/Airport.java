import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;


/* A simple airport system consists of arriving airplanes, departing airplanes, 
 * and airplanes at gates and loading unloading areas (referred to in this 
 * documentation as bays). Airplanes approach the airport and communicate with 
 * the air traffic controller to determine when the plane can land. Once the 
 * landing conditions have been met, the airplane begins its landing procedure. 
 * After landing, the plane awaits an available gate or bay depending on the plane 
 * type. After a gate or bay becomes available, the plane will approach the gate 
 * and await passenger/cargo loading/unloading. After the plane has received its 
 * services at the gate/bay, the plane coordinates with the air traffic controller 
 * to approach the departure runway. When sufficient conditions have been met, 
 * the plane will enter the runway and takeoff.
 */

public class Airport {
	
	private static final int NUM_RUNWAYS = 2;
	private static final int ARRIVALS_QUEUE_CAPACITY = 100;
	private static final int LANDED_QUEUE_CAPACITY = 2;
	private static final int DEPARTURE_QUEUE_CAPACITY = 100;
	private static int rejectedPlanes = 0;
	private static BlockingQueue<Runway> runways = initializeRunways();
	private static BlockingQueue<Airplane> arrivalsQueue = new PriorityBlockingQueue<>(ARRIVALS_QUEUE_CAPACITY);
	private static BlockingQueue<Airplane> landedQueue = new ArrayBlockingQueue<>(LANDED_QUEUE_CAPACITY);
	private static BlockingQueue<Airplane> departureQueue = new ArrayBlockingQueue<>(DEPARTURE_QUEUE_CAPACITY);
	
	public static void main(String[] args) {
		AirTrafficController atc = new AirTrafficController();
		atc.run();
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
	
	public static BlockingQueue<Airplane> getArrivalsQueue() {
		return arrivalsQueue;
	}
	
	public static BlockingQueue<Airplane> getLandedQueue() {
		return landedQueue;
	}
	
	public static BlockingQueue<Airplane> getDepartureQueue() {
		return departureQueue;
	}

	private static BlockingQueue<Runway> initializeRunways() {
		BlockingQueue<Runway> runways = new ArrayBlockingQueue<>(NUM_RUNWAYS);
		for (int i = 0; i < NUM_RUNWAYS; ++i) {
			runways.add(new Runway());
		}
		
		return runways;
	}
}
