import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ServiceArea {
	private ServiceBehavior serviceBehavior;
	private AtomicBoolean available = new AtomicBoolean(true);
	private String name;
	
	public ServiceArea(String name, ServiceBehavior serviceBehavior) {
		this.name = name;
		this.serviceBehavior = serviceBehavior;
		available.set(true);
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
						available.set(false);
						System.out.println(airplane.getName() + " starts receiving service at " + name + ". Time: " + Arrivals.getElapsedTime());
						serviceBehavior.service(airplane);
						System.out.println(airplane.getName() + " finishes receiving service at " + name + ". Time: " + Arrivals.getElapsedTime());
						sendAirplaneToDepartureQueue(airplane);
						System.out.println(name + " starts cleaning procedure. Time: " + Arrivals.getElapsedTime());
						cleanupServiceArea();
						System.out.println(name + " finishes cleaning procedure. Time: " + Arrivals.getElapsedTime());						
						available.set(true);
						System.out.println(name + " is available. Time: " + Arrivals.getElapsedTime());
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
}
