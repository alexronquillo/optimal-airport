
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
				return 1000L;
			case MEDIUM:
				return 2000L;				
			case LARGE:
				return 3000L;
			default:
				return 4000L;
		}
	}
}
