
public class Runway {
	private double startUse = 0;
	private double stopUse = 0;
	private double timeInUse = 0;
	
	
	public void startUse() {
		startUse = Airport.getElapsedTime();
	}
	
	public void stopUse(){
		stopUse = Airport.getElapsedTime();
		Airport.addRunwayTotal(stopUse - startUse);
	}
}
