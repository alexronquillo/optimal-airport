import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

public abstract class ServiceArea implements Cloneable{
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
	
	public ServiceArea clone() {
		try{  
	        return (ServiceArea) super.clone();  
	    }catch(Exception e){ 
	        return null; 
	    }
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
			}.run();
	}
	
	public void setServiceBehavior(ServiceBehavior serviceBehavior) {
		this.serviceBehavior = serviceBehavior;
	}
	
	private void sendAirplaneToDepartureQueue(Airplane plane) {
		Airport.getDepartureQueue().add(plane);
		System.out.println("Done serving plane "+ plane.getName());
		
		//make sure gate can be used now in airport
		if (plane instanceof CargoPlane){
			Airport.releaseCargoBay();
		}
		else if (plane instanceof PassengerPlane) {
			Airport.releaseGate();
		}
		else {
			
		}
	}
}
