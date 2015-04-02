
public class PassengerPlaneServiceBehavior implements ServiceBehavior {
	private int serviceTime = 1000;
	@Override
	public void service(Airplane airplane) throws InvalidAirplaneTypeException {
		try {
			if (airplane instanceof PassengerPlane) {
				System.out.println("Start: Servicing a passenger plane");
				Thread.sleep(serviceTime);
				System.out.println("Finish: Servicing a passenger plane");
			} else {
				throw new InvalidAirplaneTypeException("Can only service a passenger plane");
			}
		}
		catch (Exception e) {
			System.out.println(e.getClass().getName() + "----" + e.getMessage());
		}
	}
}
