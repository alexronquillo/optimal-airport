
public class Gate extends ServiceArea {
	public Gate(String name) {
		super(name, new PassengerPlaneServiceBehavior());
	}

}
