
public class CargoBay extends ServiceArea {
	public CargoBay(String name, double simTime) {
		super(name, new CargoPlaneServiceBehavior(), simTime);
	}

	@Override
	protected void cleanupServiceArea() throws InterruptedException {
		//Thread.sleep(2000);
	}
}
