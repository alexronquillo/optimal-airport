public abstract class ServiceArea {
	private ServiceBehavior serviceBehavior;
	private volatile boolean available = true;
	private String name;
	private double startWait = 0;
	private double stopWait = 0;
	private double totalWait = 0;
	
	public ServiceArea(String name, ServiceBehavior serviceBehavior) {
		this.name = name;
		this.serviceBehavior = serviceBehavior;
		available = true;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isAvailable() {
		return available;
	}
	
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	public void service(Airplane airplane) {		
			new Thread(new Runnable(){
				public void run() {
					try {	
						stopWait();
						System.out.println(airplane.getName() + " starts receiving service at " + name + ". Time: " + Airport.getCurrentSimulationTime());
						serviceBehavior.service(airplane);
						System.out.println(airplane.getName() + " finishes receiving service at " + name + ". Time: " + Airport.getCurrentSimulationTime());
						sendAirplaneToDepartureQueue(airplane);
						airplane.startWait();				
						available = true;
						System.out.println(name + " is available. Time: " + Airport.getCurrentSimulationTime());
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
	
	public void startWait() {
		startWait = Airport.getCurrentSimulationTime();
	}
	
	public void stopWait(){
		stopWait = Airport.getCurrentSimulationTime();
		totalWait += stopWait - startWait;
	}
	
	public double getTotalWait() {
		return totalWait;
	}
	
	private void sendAirplaneToDepartureQueue(Airplane plane) {
		while (!Airport.getDepartureQueue().offer(plane));
		System.out.println(plane.getName() + " added to the departure queue.");
	}
}
