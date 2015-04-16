
public class CargoPlaneServiceBehavior implements ServiceBehavior {	
	@Override
	public void service(Airplane airplane, double serviceTime) throws InvalidAirplaneTypeException, InterruptedException {
		if (airplane instanceof CargoPlane) {
			Thread.sleep(getServiceTime(airplane, serviceTime));
		} else {
			throw new InvalidAirplaneTypeException("Can only service a cargo plane");
		}
	}
	
	@Override
	public long getServiceTime(Airplane airplane, double serviceTime) {
		switch (airplane.getSize()) {
			case SMALL:
				return (long)(serviceTime * 1.2);
			case MEDIUM:
				return (long)(serviceTime * 1.5);				
			case LARGE:
				return (long)(serviceTime * 1.8);
			default:
				return (long)(serviceTime * 2);
		}
	}

}