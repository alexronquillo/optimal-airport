
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
		double serviceTimeRatio = 0.0555555;
		double serviceTime = Airport.SIMULATION_PERIOD * serviceTimeRatio;
	
		switch (airplane.getSize()) {
			case SMALL:
				return (long)(serviceTime * .8);
			case MEDIUM:
				return (long) serviceTime;				
			case LARGE:
				return (long)(serviceTime * 1.2);
			default:
				return (long)(serviceTime * 1.5);
		}
	}
}
