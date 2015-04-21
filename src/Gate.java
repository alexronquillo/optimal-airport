
public class Gate extends ServiceArea {
	public Gate(String name, double simTime) {
		super(name, new PassengerPlaneServiceBehavior());
	}

}
