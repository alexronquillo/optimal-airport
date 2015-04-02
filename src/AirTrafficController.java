import java.util.concurrent.BlockingQueue;


public class AirTrafficController {
	static int rejectedPlanes = 0;
	
	public static void start() {
		while (true) {
			if (hasRunway()) {
				if (hasLandingVacancy()) {
					Airplane airplane = Airport.getArrivalsQueue().poll();
					System.out.println("ATC signals plane to land");
					signalLanding(airplane);
				} else if (hasPlanesAwaitingTakeoff()) {
					Airplane airplane = Airport.getDepartureQueue().poll(); 
					System.out.println("ATC signals plane to takeoff");
					signalTakeoff(airplane);
				}
			} 
		}
	}
	
	public static void addToLandedQueue(Airplane airplane) {
		Airport.getLandedQueue().offer(airplane);
		System.out.println("Airplane added to landed queue");
	}
	
	public static void addRunway(Runway runway) {
		Airport.getRunways().offer(runway);
	}
	
	private static boolean hasRunway() {
		return Airport.getRunways().size() > 0;
	}
	
	private static boolean hasLandingVacancy() {
		return Airport.getLandedQueue().remainingCapacity() > 0;
	}
	
	private static boolean hasArrivals() {
		return Airport.getArrivalsQueue().size() > 0;
	}
	
	private static boolean hasPlanesAwaitingTakeoff() {
		return Airport.getDepartureQueue().size() > 0;
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
