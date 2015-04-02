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
	private static final int NUM_GATES = 5;
	private static final int NUM_BAYS = 5;
	private static BlockingQueue<Runway> runways = initializeRunways();
	private static Gate[] gateArray = initializeGates();
	private static CargoBay[] bayArray = initializeCargoBays();
	private static BlockingQueue<Airplane> arrivalsQueue = new PriorityBlockingQueue<>(ARRIVALS_QUEUE_CAPACITY);
	private static BlockingQueue<Airplane> landedQueue = new ArrayBlockingQueue<>(LANDED_QUEUE_CAPACITY);
	private static BlockingQueue<Airplane> departureQueue = new ArrayBlockingQueue<>(DEPARTURE_QUEUE_CAPACITY);
	
	
	
	public static BlockingQueue<Runway> getRunways() {
		return runways;
	}
	
	public static Gate[] getGateAvailable() {
		return gateArray;
	}
	
	public static CargoBay[] getBayAvailable() {
		return bayArray;
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
	
	private static Gate[] initializeGates() {
		Gate[] gateArray = new Gate[NUM_GATES];
		for (int i = 0; i < NUM_GATES; ++i) {
			gateArray[i] = new Gate();
		}
		return gateArray;
	}
	
	private static CargoBay[] initializeCargoBays() {
		CargoBay[] bayArray = new CargoBay[NUM_BAYS];
		for (int i = 0; i < NUM_BAYS; ++i) {
			bayArray[i] = new CargoBay();
		}
		return bayArray;
	}
}
