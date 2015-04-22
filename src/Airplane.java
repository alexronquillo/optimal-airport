import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Airplane implements Comparable<Airplane> {
	public static enum Priority { HIGH, MEDIUM, LOW }
	public static enum Size { SMALL, MEDIUM, LARGE }
	
	private Priority priority;
	private Size size;
	private String name;
	private Runway runway = null;
	private double startWait = 0;
	private double stopWait = 0;
	private double totalWait = 0;
	private double landingAndTakeoffTime = 0;
	public ArrayList<Double> waitTimes = new ArrayList<Double>();
	
	private long timeEnteredSystem = 0;
	private long timeExitedSystem = 0;
	
	public Airplane(String name, Priority priority, Size size, double landingAndTakeoffTime) {
		this.name = name;		
		this.priority = priority;
		this.size = size;
		this.landingAndTakeoffTime = landingAndTakeoffTime;
		this.timeEnteredSystem = System.currentTimeMillis();
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
	
	public void land() {
		System.out.println(name + " starts landing procedure. Time: " + Airport.getCurrentSimulationTime());
		runway = Airport.getRunways().poll();
		runway.startUse();

		final Timer landingTimer = new Timer();
		landingTimer.schedule(new TimerTask () {
			@Override
			public void run() {
				Airport.getRunways().offer(runway);
				runway.stopUse();
				Airport.getLandedQueue().offer(Airplane.this);
				System.out.println(name + " landed. Time: " + Airport.getCurrentSimulationTime());
				landingTimer.cancel();
			}
		}, getLandingDelay());
		this.startWait();

	}
	
	public void takeoff() {
		System.out.println(name + " starts takeoff procedure. Time: " + Airport.getCurrentSimulationTime());
		runway = Airport.getRunways().poll();
		runway.startUse();

		final Timer takeoffTimer = new Timer();
		takeoffTimer.schedule(new TimerTask () {
			@Override
			public void run() {
				Airport.getRunways().offer(runway);
				runway.stopUse();
				System.out.println(name + " took off. Time: " + Airport.getCurrentSimulationTime());
				System.out.println("Time in Arrivals Queue: " + waitTimes.get(0));
				System.out.println("Time in Landed Queue: " + waitTimes.get(1));
				System.out.println("Time in Departure Queue: " + waitTimes.get(2));
				takeoffTimer.cancel();
			}
		}, getTakeoffDelay());
		
		timeExitedSystem = System.currentTimeMillis();
		Airport.addMySTime(getSojournTime());
		System.out.println("total wait time of " + name + ": " + totalWait);
		System.out.println("Sojourn time of " + name + ": " + getSojournTime());
		
	}
	
	private double getSojournTime() {
		return (timeExitedSystem - timeEnteredSystem);
	}
	
	@Override
	public String toString() {
		String airplaneString = "";
		airplaneString += "Name: " + name + "\n";
		airplaneString += "Priority: " + priority + "\n";
		airplaneString += "Size: " + size + "\n";
		return airplaneString;
	}
	
	public void startWait() {
		startWait = Airport.getCurrentSimulationTime();
	}
	
	public void stopWait(){
		stopWait = Airport.getCurrentSimulationTime();
		totalWait += stopWait - startWait;
		waitTimes.add(stopWait - startWait);
	}
	
	public double getTotalWait() {
		return totalWait;
	}
	
	private long getTakeoffDelay() {
		long takeoffDelay = (long) (landingAndTakeoffTime);
		switch (size) {
			case SMALL:
				takeoffDelay = (long) (landingAndTakeoffTime * 1.1);
				break;
			case MEDIUM:
				takeoffDelay = (long)(landingAndTakeoffTime * 1.2);
				break;
			case LARGE:
				takeoffDelay = (long) (landingAndTakeoffTime * 1.3);
				break;
		}
		return takeoffDelay;
	}
	
	private long getLandingDelay() {
		long landingDelay = (long) (landingAndTakeoffTime);
		switch (size) {
			case SMALL:
				landingDelay = (long) (landingAndTakeoffTime * 1.1);
				break;
			case MEDIUM:
				landingDelay = (long) (landingAndTakeoffTime * 1.2);
				break;
			case LARGE:
				landingDelay = (long) (landingAndTakeoffTime * 1.3);
				break;
	}
		return landingDelay;
	}
	
	@Override
	public int compareTo(Airplane airplane) {
		return this.priority.compareTo(airplane.getPriority());
	}
	
}
