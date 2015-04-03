
public class CargoBay extends ServiceArea {
	public CargoBay() {
		super(new CargoPlaneServiceBehavior());
	}

	@Override
	protected void cleanupServiceArea() throws InterruptedException {
		Thread.sleep(2000);
	}
}
