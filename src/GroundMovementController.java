public class GroundMovementController implements Runnable {

	@Override
	public void run() {
		try {
			while (true) {
				if (!Airport.getLandedQueue().isEmpty()) {
					if (nextIsPassengerPlane()) {
						while (!gateAvailable());
						Gate gate = getGate();
						PassengerPlane plane = (PassengerPlane) getNextPlane();
						plane.stopWait();
						setPassengerPlaneToGate(gate, plane);
					} else {
						while (!cargoBayAvailable());
						CargoBay bay = getCargoBay();
						CargoPlane plane = (CargoPlane) getNextPlane();
						plane.stopWait(); 
						setCargoPlaneToCargoBay(bay, plane);
					}
				}
			}
		} catch (NullServiceAreaException e) {
			e.printStackTrace();
		}
	}

	private static boolean gateAvailable() {
		for (Gate gate : Airport.getGates()) {
			if (gate.isAvailable()) {
				return true;
			}
		}
		return false;
	}

	private static boolean cargoBayAvailable() {
		for (CargoBay bay : Airport.getBays()) {
			if (bay.isAvailable()) {
				return true;
			}
		}
		return false;
	}

	private static Gate getGate() throws NullServiceAreaException {
		for (Gate gate : Airport.getGates()) {
			if (gate.isAvailable()) {
				gate.setAvailable(false);
				return gate;
			}
		}
		throw new NullServiceAreaException("Ground Movement receives null gate");
	}

	private static CargoBay getCargoBay() throws NullServiceAreaException {
		for (CargoBay bay : Airport.getBays()) {
			if (bay.isAvailable()) {
				bay.setAvailable(false);
				return bay;
			}
		}
		throw new NullServiceAreaException("Ground Movement receives null bay");
	}

	private static void setPassengerPlaneToGate(Gate gate, PassengerPlane plane) {
		System.out.println("Ground Movement Controller assigns " + plane.getName() + " to " + gate.getName() + ". Time: " + Airport.getCurrentSimulationTime());
		gate.service(plane);
	}

	private static void setCargoPlaneToCargoBay(CargoBay bay, CargoPlane plane) {
		System.out.println("Ground Movement Controller assigns " + plane.getName() + " to " + bay.getName() + ". Time: " + Airport.getCurrentSimulationTime());
		bay.service(plane);
	}

	private static boolean nextIsPassengerPlane() {
		return Airport.getLandedQueue().peek() instanceof PassengerPlane;
	}

	private static Airplane getNextPlane() {
		return Airport.getLandedQueue().poll();
	}
}
