
public class CargoBay extends ServiceArea {
	public CargoBay(String name, double simTime) {
		super(name, new CargoPlaneServiceBehavior());
	}
}
