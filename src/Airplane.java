import java.util.Timer;
import java.util.TimerTask;


public class Airplane {
	public static enum Priority { LOW, MEDIUM, HIGH }
	public static enum Size { SMALL, MEDIUM, LARGE }
	
	private Priority priority;
	private Size size;
	private String name;
	
	public Airplane(String name, Priority priority, Size size) {
		this.name = name;		
		this.priority = priority;
		this.size = size;
	}
	
	public Priority getPriority() {
		return priority;
	}
	
	public Size getSize() {
		return size;
	}
	
	public String getName() {
		return name;
	}
	
	public void land(AirTrafficController atc, Runway runway) {
		System.out.println(name + " begins landing procedure");
		final Timer landingTimer = new Timer();
		landingTimer.schedule(new TimerTask () {
			@Override
			public void run() {				
				releaseRunway(atc, runway);
				atc.addToLandedQueue(Airplane.this);
				System.out.println(name + " landed");				
				landingTimer.cancel();
			}
		}, getLandingDelay());
	}
	
	public void takeoff(AirTrafficController atc, Runway runway) {
		System.out.println(name + " begins takeoff procedure");
		final Timer takeoffTimer = new Timer();
		takeoffTimer.schedule(new TimerTask () {
			@Override
			public void run() {
				releaseRunway(atc, runway);
				System.out.println(name + " exited the system");
				takeoffTimer.cancel();
			}
		}, getTakeoffDelay());
	}
	
	@Override
	public String toString() {
		String airplaneString = "";
		airplaneString += "Name: " + name + "\n";
		airplaneString += "Priority: " + priority + "\n";
		airplaneString += "Size: " + size + "\n";
		return airplaneString;
	}
	
	private void releaseRunway(AirTrafficController atc, Runway runway) { 
		atc.addRunway(runway);
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
