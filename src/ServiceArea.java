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
			new Runnable(){
				public void run() {
					try {	
						available.set(false);
						serviceBehavior.service(airplane);
						sendAirplaneToDepartureQueue(airplane);
						cleanupServiceArea();
						available.set(true);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.run();
	}
	
	public void setServiceBehavior(ServiceBehavior serviceBehavior) {
		this.serviceBehavior = serviceBehavior;
	}
	
	private void sendAirplaneToDepartureQueue(Airplane plane) {
		while (!Airport.getDepartureQueue().offer(plane));
		System.out.println("Airplane added to the departure queue.");
	}
	
	protected abstract void cleanupServiceArea() throws InterruptedException;
}
