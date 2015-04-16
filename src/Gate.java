
public class Gate extends ServiceArea {
	public Gate(String name, double simTime) {
		super(name, new PassengerPlaneServiceBehavior(), simTime);
	}

	@Override
	protected void cleanupServiceArea() throws InterruptedException {
		//Thread.sleep(1000);
	}
}
