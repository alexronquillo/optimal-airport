
public class PassengerPlaneServiceBehavior implements ServiceBehavior {
	@Override
	public void service(Airplane airplane) throws InterruptedException, InvalidAirplaneTypeException {	
		if (airplane instanceof PassengerPlane) {			
			Thread.sleep(getServiceTime(airplane));			
		} else {
			throw new InvalidAirplaneTypeException("Can only service a passenger plane");
		}
	}
	
	@Override
	public long getServiceTime(Airplane airplane) {	
		switch (airplane.getSize()) {
			case SMALL:
				return 10;
			case MEDIUM:
				return 15;
			case LARGE:
				return 20;
			default:
				return 20;
		}
	}
}
