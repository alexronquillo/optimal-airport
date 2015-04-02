
public class CargoPlaneServiceBehavior implements ServiceBehavior {	
	int serviceTime = 3000;
	@Override
	public void service(Airplane airplane) throws InvalidAirplaneTypeException, InterruptedException {
		try {
			if (airplane instanceof CargoPlane) {
				System.out.println("Start: Servicing a cargo plane");
				Thread.sleep(serviceTime);
				System.out.println("Finish: Servicing a cargo plane");
			} else {
				throw new InvalidAirplaneTypeException("Can only service a cargo plane");
			}
		}
		catch (InterruptedException e) {
			System.out.println(e.getClass().getName() + "----" + e.getMessage());
		}
		
	}

}