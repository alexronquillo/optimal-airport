
public class Gate extends ServiceArea {
	public Gate(String name) {
		super(name, new PassengerPlaneServiceBehavior());
	}

	@Override
	protected void cleanupServiceArea() throws InterruptedException {
		Thread.sleep(1000);
	}
}
