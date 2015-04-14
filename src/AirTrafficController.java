import java.util.concurrent.atomic.AtomicInteger;

public class AirTrafficController implements Runnable {	
	private static AtomicInteger landingPlanes = new AtomicInteger(0);
	
	@Override
	public void run() {
		while (true) {
			if (airportHasRunway()) {
				if (airplaneCanLand()) {
					Airplane airplane = Airport.getArrivalsQueue().poll();
					System.out.println("Air Traffic Controller signals " + airplane.getName() + " to land. Time: " + Airport.getSimulationTime());	
					airplane.stopWait();
					signalLanding(airplane);					
					landingPlanes.set(landingPlanes.get() + 1);
				} else if (hasPlanesAwaitingTakeoff()) {
					Airplane airplane = Airport.getDepartureQueue().poll(); 
					airplane.stopWait();
					System.out.println("Air Traffic Controller signals " + airplane.getName() + " to takeoff. Time: " + Airport.getSimulationTime());
					signalTakeoff(airplane);
				}
			} 
		}
	}
	
	public void signalLanded() {
		landingPlanes.set(landingPlanes.get() - 1);
	}
	
	private boolean airplaneCanLand() {		
		return (Airport.getLandedQueue().remainingCapacity() - landingPlanes.get() > 0) && hasArrivals();
	}
	
	private boolean airportHasRunway() {
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
