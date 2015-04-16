
public class CargoBay extends ServiceArea {
	public CargoBay(String name) {
		super(name, new CargoPlaneServiceBehavior());
	}
}
