
public class CargoPlaneServiceBehavior implements ServiceBehavior {	
	@Override
	public void service(Airplane airplane) throws InvalidAirplaneTypeException {
		if (airplane instanceof CargoPlane) {
			System.out.println("Start: Servicing a cargo plane");
			System.out.println("Finish: Servicing a cargo plane");
		} else {
			throw new InvalidAirplaneTypeException("Can only service a cargo plane");
		}
	}
}
