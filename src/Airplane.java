import java.util.Timer;
import java.util.TimerTask;


public class Airplane {
	public static enum Size { SMALL, MEDIUM, LARGE }
	
	private int priority;
	private Size size;
	private String name;
	
	public Airplane(String name, int priority, Size size) {
		this.name = name;		
		this.priority = priority;
		this.size = size;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public Size getSize() {
		return size;
	}
	
	public String getName() {
		return name;
	}
	
	public void land(AirTrafficController atc, Runway runway) {
		final Timer landingTimer = new Timer();
		landingTimer.schedule(new TimerTask () {
			@Override
			public void run() {
				releaseRunway(atc, runway);
				System.out.println("Landed");
				landingTimer.cancel();
			}
		}, getLandingDelay());
	}
	
	public void takeoff(AirTrafficController atc, Runway runway) {
		final Timer takeoffTimer = new Timer();
		takeoffTimer.schedule(new TimerTask () {
			@Override
			public void run() {
				releaseRunway(atc, runway);
				System.out.println("Took off");
				takeoffTimer.cancel();
			}
		}, getTakeoffDelay());
	}
	
	@Override
	public String toString() {
		String sizeString = "";
		switch (size) {
			case SMALL:
				sizeString = "Small";
				break;
			case MEDIUM:
				sizeString = "Medium";
				break;
			case LARGE:
				sizeString = "Large";
				break;
		}
		
		String airplaneString = "";
		airplaneString += "Name: " + name + "\n";
		airplaneString += "Priority: " + priority + "\n";
		airplaneString += "Size: " + sizeString + "\n";
		return airplaneString;
	}
	
	private void releaseRunway(AirTrafficController atc, Runway runway) { 
		atc.addRunway(runway);
		System.out.println("Runway released");
	}
	
	private long getTakeoffDelay() {
		long takeoffDelay = 1000;
		switch (size) {
			case SMALL:
				takeoffDelay = 1000;
				break;
			case MEDIUM:
				takeoffDelay = 2000;
				break;
			case LARGE:
				takeoffDelay = 3000;
				break;
		}
		return takeoffDelay;
	}
	
	private long getLandingDelay() {
		long landingDelay = 1000;
		switch (size) {
			case SMALL:
				landingDelay = 1000;
				break;
			case MEDIUM:
				landingDelay = 2000;
				break;
			case LARGE:
				landingDelay = 3000;
				break;
	}
		return landingDelay;
	}
}
