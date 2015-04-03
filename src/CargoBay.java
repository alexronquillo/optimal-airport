
public class CargoBay extends ServiceArea {
	public CargoBay(String name) {
		super(name, new CargoPlaneServiceBehavior());
	}

	@Override
	protected void cleanupServiceArea() throws InterruptedException {
		Thread.sleep(2000);
	}
}
