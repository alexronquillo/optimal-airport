import java.util.Timer;
import java.util.TimerTask;


public class ServiceArea {
	private ServiceBehavior serviceBehavior;
	private boolean available;
	
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
		try {			
			serviceBehavior.service(airplane);
		} catch (InvalidAirplaneTypeException e) {
			System.out.println("InvalidAirplaneTypeException: " + e.getMessage());
		} catch (NullPointerException e) {
			System.out.println("NullPointerException: serviceBehavior is null");
		}
	}
	
	public void setServiceBehavior(ServiceBehavior serviceBehavior) {
		this.serviceBehavior = serviceBehavior;
	}
	
	private void sendAirplaneToDepartureQueue() {
		
	}
}
