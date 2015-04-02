import java.util.concurrent.atomic.AtomicInteger;

public class AirTrafficController implements Runnable {	
	private static AtomicInteger landingPlanes = new AtomicInteger(0);
	
	@Override
	public void run() {
		while (true) {
			if (hasRunway()) {
				if ((Airport.getLandedQueue().remainingCapacity() - landingPlanes.get() > 0) && hasArrivals()) {
					Airplane airplane = Airport.getArrivalsQueue().poll();
					System.out.println("ATC signals plane to land");			
					signalLanding(airplane);
					landingPlanes.set(landingPlanes.get() + 1);
				} else if (hasPlanesAwaitingTakeoff()) {
					Airplane airplane = Airport.getDepartureQueue().poll(); 
					System.out.println("ATC signals plane to takeoff");
					signalTakeoff(airplane);
				}
			} 
		}
	}
	
	public void addToLandedQueue(Airplane airplane) {
		Airport.getLandedQueue().offer(airplane);
		System.out.println("Airplane added to landed queue");
	}
	
	public void addRunway(Runway runway) {
		Airport.getRunways().offer(runway);
	}
	
	private boolean hasRunway() {
		return Airport.getRunways().size() > 0;
	}
	
	private boolean hasArrivals() {
		return Airport.getArrivalsQueue().size() > 0;
	}
	
	private boolean hasPlanesAwaitingTakeoff() {
		return Airport.getDepartureQueue().size() > 0;
	}
	
	private void signalLanding(Airplane airplane) {
		airplane.land();
	}
	
	private void signalTakeoff(Airplane airplane) {
		airplane.takeoff();
	}
}
