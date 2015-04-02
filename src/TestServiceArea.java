public class TestServiceArea {
	
	// Need to discuss what happens if wrong type plane it sent to wrong type of ServiceArea
	
	public static void main(String[] args) {
		Airplane passengerPlane = new PassengerPlane("Test Plane 1", Airplane.Priority.HIGH, Airplane.Size.LARGE);
		ServiceArea gate1 = new Gate();
		System.out.println("Gate 1 is available: " + gate1.isAvailable());
		gate1.setAvailable(false);
		System.out.println("Gate 1 is available: " + gate1.isAvailable());
		gate1.service(passengerPlane);
		System.out.println("Gate 1 is available: " + gate1.isAvailable());
		
		Airplane cargoPlane = new CargoPlane("Test Plane 1", Airplane.Priority.HIGH, Airplane.Size.LARGE);
		ServiceArea bay1 = new CargoBay();
		System.out.println("Bay 1 is available: " + bay1.isAvailable());
		bay1.setAvailable(false);
		System.out.println("Bay 1 is available: " + bay1.isAvailable());
		bay1.service(cargoPlane);
		System.out.println("Bay 1 is available: " + bay1.isAvailable());

	}
}
