import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JOptionPane;

public class AirTrafficController implements Runnable {	
	private AtomicInteger landingPlanes = new AtomicInteger(0);
	private double airplaneWaitTimeTotal = 0;
	private int numberOfPlanes = 0;
	private AtomicBoolean nextAttemptIsLanding = new AtomicBoolean(true);
	
	@Override
	public void run() {
		while (true) {
			if (airportHasRunway()) {
				if (nextAttemptIsLanding.get()) {
					// Attempt landing
					if (airplaneCanLand()) {
						Airplane airplane = Airport.getArrivalsQueue().poll();
						System.out.println("Air Traffic Controller signals " + airplane.getName() + " to land. Time: " + Airport.getSimulationTime());	
						airplane.stopWait();
						signalLanding(airplane);					
						landingPlanes.set(landingPlanes.get() + 1);
					}	
					
					nextAttemptIsLanding.set(false);
				} else {
					// Attempt departure
					if (hasPlanesAwaitingTakeoff()) {
						Airplane airplane = Airport.getDepartureQueue().poll(); 
						airplane.stopWait();
						Airport.addPlaneTimes(airplane.waitTimes);
						System.out.println("Air Traffic Controller signals " + airplane.getName() + " to takeoff. Time: " + Airport.getSimulationTime());
						signalTakeoff(airplane);
					}
					
					nextAttemptIsLanding.set(true);
				}
			} 
		}
	}
	
	public int getNumberOfPlanes()
	{
		return numberOfPlanes;
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
		airplaneWaitTimeTotal += airplane.getTotalWait();
		numberOfPlanes++;
		airplane.takeoff();
	}	
	
	public double getAverageWaitTime() {
		return airplaneWaitTimeTotal / numberOfPlanes;
	}
}
