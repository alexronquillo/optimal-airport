import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ServiceArea {
	private ServiceBehavior serviceBehavior;
	private AtomicBoolean available = new AtomicBoolean(true);
	private int sleepTime = 2000;
	
	public ServiceArea(ServiceBehavior serviceBehavior) {
		this.serviceBehavior = serviceBehavior;
		available.set(true);
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
						Thread.sleep(sleepTime);
						available.set(true);
					}
					catch (Exception e) {
						System.out.println(e.getClass().getName() +"----" + e.getMessage());
					}
				}
			}.run();
	}
	
	public void setServiceBehavior(ServiceBehavior serviceBehavior) {
		this.serviceBehavior = serviceBehavior;
	}
	
	private void sendAirplaneToDepartureQueue(Airplane plane) {
		
	}
}
