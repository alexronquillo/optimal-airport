

public class CargoPlaneServiceBehavior implements ServiceBehavior {	
	@Override
	public void service(Airplane airplane) throws InvalidAirplaneTypeException, InterruptedException {
		if (airplane instanceof CargoPlane) {
			Thread.sleep(getServiceTime(airplane));
		} else {
			throw new InvalidAirplaneTypeException("Can only service a cargo plane");
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
