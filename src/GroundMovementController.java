

public class GroundMovementController implements Runnable {
	
	@Override
	public void run() {
		while(true) {
			if(nextIsPassengerPlane()) {
				while(!gateAvailable());
				Gate gate = getGate();
				PassengerPlane plane = (PassengerPlane)getNextPlane();
				setPassengerPlaneToGate(gate, plane);
			} else {
				while (!cargoBayAvailable());
				CargoBay bay = getCargoBay();
			    CargoPlane plane = (CargoPlane)getNextPlane();
				setCargoPlaneToCargoBay(bay, plane);
			}
		}
	}
	
	private static boolean gateAvailable() {
		return false;	
	}
	
	private static boolean cargoBayAvailable() {
		return false;	
	}
	
	private static Gate getGate() {
		return null;
	}
	
	private static CargoBay getCargoBay() {
		return null;
	}
	
	private static void setPassengerPlaneToGate(Gate gate, PassengerPlane plane) {
		gate.service(plane);
	}
	
	private static void setCargoPlaneToCargoBay(CargoBay bay, CargoPlane plane) {
		bay.service(plane);
	}
	
	private static boolean nextIsPassengerPlane() {
		return Airport.getLandedQueue().peek() instanceof PassengerPlane;	
	}

	
	
	private static Airplane getNextPlane() {
		return null;
	}
}
