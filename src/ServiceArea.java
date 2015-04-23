public abstract class ServiceArea {
	private ServiceBehavior serviceBehavior;
	private volatile boolean available = true;
	private String name;
	private double startUse = 0;
	private double stopUse = 0;
	private double totalUsageTime = 0;
	
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
						startUse = Airport.getCurrentSimulationTime();
						System.out.println(airplane.getName() + " starts receiving service at " + name + ". Time: " + startUse);

						serviceBehavior.service(airplane);

						stopUse = Airport.getCurrentSimulationTime();
						System.out.println(airplane.getName() + " finishes receiving service at " + name + ". Time: " + stopUse);

						sendAirplaneToDepartureQueue(airplane);
						airplane.startWait();				
						totalUsageTime += (stopUse - startUse);
						available = true;
						System.out.println(name + " is available. Time: " + Airport.getCurrentSimulationTime());
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
	
	public double getTotalUsageTime() {
		return totalUsageTime;
	}
	
	private void sendAirplaneToDepartureQueue(Airplane plane) {
		while (!Airport.getDepartureQueue().offer(plane));
		System.out.println(plane.getName() + " added to the departure queue.");
	}
}
