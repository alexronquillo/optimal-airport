import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

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
	
	public Airplane(String name, Priority priority, Size size, double landingAndTakeoffTime) {
		this.name = name;		
		this.priority = priority;
		this.size = size;
		this.landingAndTakeoffTime = landingAndTakeoffTime;
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
		System.out.println(name + " starts landing procedure. Time: " + Airport.getSimulationTime());
		runway = Airport.getRunways().poll();
		final Timer landingTimer = new Timer();
		landingTimer.schedule(new TimerTask () {
			@Override
			public void run() {
				Airport.getRunways().offer(runway);
				Airport.getLandedQueue().offer(Airplane.this);
				Airport.getAirTrafficController().signalLanded();
				System.out.println(name + " landed. Time: " + Airport.getSimulationTime());
				landingTimer.cancel();
			}
		}, getLandingDelay());
		this.startWait();

	}
	
	public void takeoff() {
		System.out.println(name + " starts takeoff procedure. Time: " + Airport.getSimulationTime());
		runway = Airport.getRunways().poll();
		final Timer takeoffTimer = new Timer();
		takeoffTimer.schedule(new TimerTask () {
			@Override
			public void run() {
				Airport.getRunways().offer(runway);
				System.out.println(name + " took off. Time: " + Airport.getSimulationTime());
				takeoffTimer.cancel();
			}
		}, getTakeoffDelay());
		
		System.out.println("total wait time of " + name + ": " + totalWait);
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
		startWait = Airport.getElapsedTime();
	}
	
	public void stopWait(){
		stopWait = Airport.getElapsedTime();
		totalWait += stopWait - startWait;
	}
	
	public double getTotalWait() {
		return totalWait;
	}
	
	private long getTakeoffDelay() {
		long takeoffDelay = (long) landingAndTakeoffTime * 1000;
		switch (size) {
			case SMALL:
				takeoffDelay = (long) landingAndTakeoffTime * 1000;
				break;
			case MEDIUM:
				takeoffDelay = (long)(landingAndTakeoffTime * 1.1 * 1000);
				break;
			case LARGE:
				takeoffDelay = (long) (landingAndTakeoffTime * 1.2 * 1000);
				break;
		}
		return takeoffDelay;
	}
	
	private long getLandingDelay() {
		long landingDelay = (long) landingAndTakeoffTime * 1000;
		switch (size) {
			case SMALL:
				landingDelay = (long) landingAndTakeoffTime * 1000;
				break;
			case MEDIUM:
				landingDelay = (long) (landingAndTakeoffTime * 1.1 * 1000);
				break;
			case LARGE:
				landingDelay = (long) (landingAndTakeoffTime * 1.2 * 1000);
				break;
	}
		return landingDelay;
	}
	
	@Override
	public int compareTo(Airplane airplane) {
		return this.priority.compareTo(airplane.getPriority());
	}
	
}
