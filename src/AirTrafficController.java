

public class AirTrafficController implements Runnable {	
	private static int landingPlanes = 0;

	@Override
	public void run() {
		while (true) {
			if (hasRunway()) {
				if ((Airport.getLandedQueue().remainingCapacity() - landingPlanes > 0) && hasArrivals()) {
					Airplane airplane = Airport.getArrivalsQueue().poll();
					System.out.println("ATC signals plane to land");			
					signalLanding(airplane);
					landingPlanes++;
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
}
