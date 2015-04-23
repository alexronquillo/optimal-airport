
public class AirTrafficController implements Runnable {	
	private double airplaneWaitTimeTotal = 0;
	private int numberOfPlanes = 0;
	private volatile boolean nextAttemptIsLanding = true;
	
	@Override
	public void run() {
		while (true) {
			if (airportHasRunway()) {
				if (nextAttemptIsLanding) {
					if (airplaneCanLand()) {
						Airplane airplane;
						if ((airplane = Airport.getArrivalsQueue().poll()) != null)
						{
							System.out.println("Air Traffic Controller signals " + airplane.getName() + " to land. Time: " + Airport.getCurrentSimulationTime());	
							airplane.stopWait();
							signalLanding(airplane);					
						}
					}	
					
					nextAttemptIsLanding = false;
				} else {					
					if (hasPlanesAwaitingTakeoff()) {
						Airplane airplane = Airport.getDepartureQueue().poll(); 
						airplane.stopWait();
						Airport.addPlaneTimes(airplane.waitTimes);
						System.out.println("Air Traffic Controller signals " + airplane.getName() + " to takeoff. Time: " + Airport.getCurrentSimulationTime());
						signalTakeoff(airplane);
					}
					
					nextAttemptIsLanding = true;
				}
			}
		}
	}
	
	public int getNumberOfPlanes()
	{
		return numberOfPlanes;
	}
	
	public double getAverageWaitTime() {
		return airplaneWaitTimeTotal / numberOfPlanes;
	}
	
	private boolean airplaneCanLand() {		
		return Airport.getLandedQueue().remainingCapacity() > 0 && hasArrivals();
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
}
