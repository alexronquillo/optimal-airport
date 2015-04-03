
public class Gate extends ServiceArea {
	public Gate() {
		super(new PassengerPlaneServiceBehavior());
	}

	@Override
	protected void cleanupServiceArea() throws InterruptedException {
		Thread.sleep(1000);
	}
}
