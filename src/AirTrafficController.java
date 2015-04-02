import java.util.concurrent.BlockingQueue;


public class AirTrafficController {
	static int rejectedPlanes = 0;
	
	public static void start() {
		while (true) {
			if (hasRunway()) {
				if (hasLandingVacancy()) {
					Airplane airplane = Airport.arrivalsQueue.poll();
					System.out.println("ATC signals plane to land");
					signalLanding(airplane);
				} else if (hasPlanesAwaitingTakeoff()) {
					Airplane airplane = Airport.departureQueue.poll(); 
					System.out.println("ATC signals plane to takeoff");
					signalTakeoff(airplane);
				}
			} 
		}
	}
	
	public static void addToLandedQueue(Airplane airplane) {
		Airport.landedQueue.offer(airplane);
		System.out.println("Airplane added to landed queue");
	}
	
	public static void addRunway(Runway runway) {
		Airport.runways.offer(runway);
	}
	
	private static boolean hasRunway() {
		return Airport.runways.size() > 0;
	}
	
	private static boolean hasLandingVacancy() {
		return Airport.landedQueue.remainingCapacity() > 0;
	}
	
	private static boolean hasArrivals() {
		return Airport.arrivalsQueue.size() > 0;
	}
	
	private static boolean hasPlanesAwaitingTakeoff() {
		return Airport.departureQueue.size() > 0;
	}
	
	private static void signalLanding(Airplane airplane) {
		airplane.land();
	}
	
	private static void signalTakeoff(Airplane airplane) {
		airplane.takeoff();
	}
	
	public static void rejectedPlane() {
		rejectedPlanes++;
	}
}
