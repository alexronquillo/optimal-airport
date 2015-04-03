import java.util.Stack;
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
	private static final int NUMBER_OF_GATES = 10;
	private static final int NUMBER_OF_BAYS = 5;
	private static BlockingQueue<Runway> runways = initializeRunways();
	private static BlockingQueue<Airplane> arrivalsQueue = new PriorityBlockingQueue<>(ARRIVALS_QUEUE_CAPACITY);
	private static BlockingQueue<Airplane> landedQueue = new ArrayBlockingQueue<>(LANDED_QUEUE_CAPACITY);
	private static BlockingQueue<Airplane> departureQueue = new ArrayBlockingQueue<>(DEPARTURE_QUEUE_CAPACITY);
	
	private static Gate[] gates = new Gate[NUMBER_OF_GATES];
	private static CargoBay[] bays = new CargoBay[NUMBER_OF_BAYS];
	
	public static void main(String[] args) {
		//instantiate all new gates and bays
		for (int i = 0; i < gates.length; i++) {
			gates[i] = new Gate();
		}
		
		for (int i = 0; i < bays.length; i++) {
			bays[i] = new CargoBay();
		}
		
		AirTrafficController.start();
		new GroundMovementController().start();
	}
	
	public static int getNumberOfOpenGates() {
		int value = 0;
		
		for (int i = 0; i < gates.length; i++) {
			if (gates[i] != null) {
				value++;
			}
		}
		return value;
	}
	
	public static int getNumberOfOpenBays() {
		int value = 0;
		
		for (int i = 0; i < bays.length; i++) {
			if (bays[i] != null) {
				value++;
			}
		}
		
		return value;
	}
	
	public static Gate returnGate() {
		for (int i = 0; i < gates.length; i++) {
			if (gates[i] != null) {
				Gate gate = (Gate)gates[i].clone();
				gates[i] = null;
				return gate;
			}
		}
		return null;
	}
	
	public static void releaseGate() {
		for (int i = 0; i < gates.length; i++) {
			if (gates[i] == null) {
				System.out.println("Gate returned to system.");
				gates[i] = new Gate();
				return;
			}
		}
	}
	
	public static void releaseCargoBay() {
		for (int i = 0; i < bays.length; i++) {
			if (bays[i] == null) {
				System.out.println("CargoBay returned to system.");
				bays[i] = new CargoBay();
				return;
			}
		}
	}
	
	public static CargoBay returnCargoBay() {
		for (int i = 0; i < bays.length; i++) {
			if (bays[i] != null) {
				CargoBay bay = (CargoBay)bays[i].clone();
				bays[i] = null;
				return bay;
			}
		}
		return null;
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
