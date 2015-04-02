import java.util.concurrent.ArrayBlockingQueue;


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
	
	public static void main(String[] args) {
		ArrayBlockingQueue<Runway> runways = getRunways();		
		ArrayBlockingQueue<Airplane> arrivalsQueue = new ArrayBlockingQueue<>(ARRIVALS_QUEUE_CAPACITY);
		ArrayBlockingQueue<Airplane> landedQueue = new ArrayBlockingQueue<>(LANDED_QUEUE_CAPACITY);
		ArrayBlockingQueue<Airplane> departureQueue = new ArrayBlockingQueue<>(DEPARTURE_QUEUE_CAPACITY);
		
		Airplane testPlane = new PassengerPlane("Test passenger plane1", Airplane.Priority.HIGH, Airplane.Size.LARGE);
		Airplane testPlane1 = new PassengerPlane("Test passenger plane2", Airplane.Priority.HIGH, Airplane.Size.MEDIUM);
		Airplane testPlane2 = new PassengerPlane("Test passenger plane3", Airplane.Priority.HIGH, Airplane.Size.SMALL);
		Airplane testPlane3 = new PassengerPlane("Test passenger plane4", Airplane.Priority.HIGH, Airplane.Size.LARGE);
		Airplane testPlane4 = new PassengerPlane("Test passenger plane5", Airplane.Priority.HIGH, Airplane.Size.SMALL);
		
		arrivalsQueue.add(testPlane);		
		arrivalsQueue.add(testPlane1);
		arrivalsQueue.add(testPlane2);
		arrivalsQueue.add(testPlane3);
		arrivalsQueue.add(testPlane4);
		
		AirTrafficController atc = new AirTrafficController(runways, arrivalsQueue, landedQueue, departureQueue);
		atc.start();
	}

	private static ArrayBlockingQueue<Runway> getRunways() {
		ArrayBlockingQueue<Runway> runways = new ArrayBlockingQueue<>(NUM_RUNWAYS);
		for (int i = 0; i < NUM_RUNWAYS; ++i) {
			runways.add(new Runway());
		}
		
		return runways;
	}
}
