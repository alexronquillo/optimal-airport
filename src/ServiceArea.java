import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

public abstract class ServiceArea {
	private ServiceBehavior serviceBehavior;
	private boolean available;
	private int sleepTime = 2000;
	
	public ServiceArea(ServiceBehavior serviceBehavior) {
		this.serviceBehavior = serviceBehavior;
		available = true;
	}
	
	public boolean isAvailable() {
		return available;
	}
	
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	public void service(Airplane airplane) {		
			new Runnable(){
				public void run() {
					try {	
						setAvailable(false);
						serviceBehavior.service(airplane);
						sendAirplaneToDepartureQueue(airplane);
						Thread.sleep(sleepTime);
						setAvailable(true);
					}
					catch (Exception e) {
						System.out.println(e.getClass().getName() +"----" + e.getMessage());
					}
				}
			};
	}
	
	public void setServiceBehavior(ServiceBehavior serviceBehavior) {
		this.serviceBehavior = serviceBehavior;
	}
	
	private void sendAirplaneToDepartureQueue(Airplane plane) {
		
	}
}
