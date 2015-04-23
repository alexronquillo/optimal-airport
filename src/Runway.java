
public class Runway {
	private double startUse = 0;
	private double stopUse = 0;
	private double timeInUse = 0;
	
	
	public void startUse() {
		startUse = Airport.getCurrentSimulationTime();
	}
	
	public void stopUse(){
		stopUse = Airport.getCurrentSimulationTime();

		double totalUsageTime = stopUse - startUse;
		timeInUse += totalUsageTime;
		Airport.addRunwayTotal(totalUsageTime);
	}

	public double getTimeInUse() {
		return timeInUse;
	}
}
