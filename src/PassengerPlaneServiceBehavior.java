
public class PassengerPlaneServiceBehavior implements ServiceBehavior {
	@Override
	public void service(Airplane airplane) throws InvalidAirplaneTypeException {
		if (airplane instanceof PassengerPlane) {
			System.out.println("Start: Servicing a passenger plane");
			System.out.println("Finish: Servicing a passenger plane");
		} else {
			throw new InvalidAirplaneTypeException("Can only service a passenger plane");
		}
	}
}
