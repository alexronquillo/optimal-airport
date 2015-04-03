
public class CargoPlaneServiceBehavior implements ServiceBehavior {	
	@Override
	public void service(Airplane airplane) throws InvalidAirplaneTypeException, InterruptedException {
		if (airplane instanceof CargoPlane) {
			System.out.println("Start: Servicing a cargo plane");
			Thread.sleep(getServiceTime(airplane));
			System.out.println("Finish: Servicing a cargo plane");
		} else {
			throw new InvalidAirplaneTypeException("Can only service a cargo plane");
		}
	}
	
	@Override
	public long getServiceTime(Airplane airplane) {
		switch (airplane.getSize()) {
			case SMALL:
				return 2000L;
			case MEDIUM:
				return 3000L;				
			case LARGE:
				return 4000L;
			default:
				return 5000L;
		}
	}

}