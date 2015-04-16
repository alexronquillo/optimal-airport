import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ServiceArea {
	private ServiceBehavior serviceBehavior;
	private AtomicBoolean available = new AtomicBoolean(true);
	private String name;
	private double startWait = 0;
	private double stopWait = 0;
	private double totalWait = 0;
	private double serviceTimeRatio = 0.0555555;
	private double standardServiceTime = 0;
	
	public ServiceArea(String name, ServiceBehavior serviceBehavior, double simTime) {
		this.name = name;
		this.serviceBehavior = serviceBehavior;
		available.set(true);
		standardServiceTime = simTime * serviceTimeRatio;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isAvailable() {
		return available.get();
	}
	
	public void setAvailable(boolean available) {
		this.available.set(available);
	}
	
	public void service(Airplane airplane) {		
			new Thread(new Runnable(){
				public void run() {
					try {	
						stopWait();
						System.out.println(airplane.getName() + " starts receiving service at " + name + ". Time: " + Airport.getSimulationTime());
						serviceBehavior.service(airplane, standardServiceTime);
						System.out.println(airplane.getName() + " finishes receiving service at " + name + ". Time: " + Airport.getSimulationTime());
						sendAirplaneToDepartureQueue(airplane);
						airplane.startWait();
						System.out.println(name + " starts cleaning procedure. Time: " + Airport.getSimulationTime());
						cleanupServiceArea();
						System.out.println(name + " finishes cleaning procedure. Time: " + Airport.getSimulationTime());						
						available.set(true);
						System.out.println(name + " is available. Time: " + Airport.getSimulationTime());
						startWait();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
	}
	
	public void setServiceBehavior(ServiceBehavior serviceBehavior) {
		this.serviceBehavior = serviceBehavior;
	}
	
	private void sendAirplaneToDepartureQueue(Airplane plane) {
		while (!Airport.getDepartureQueue().offer(plane));
		System.out.println(plane.getName() + " added to the departure queue.");
	}
	
	protected abstract void cleanupServiceArea() throws InterruptedException;
	
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
}
