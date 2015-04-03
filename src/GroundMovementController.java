import java.util.List;

public class GroundMovementController implements Runnable {
	
	public void start() {
		run();
	}
	
	public void run(){
		try {
			while (true) {
				Airplane plane = null;
				
				if (Airport.getLandedQueue().size() > 0){
					plane = Airport.getLandedQueue().poll();
					System.out.println(plane + " being serviced");
					
					if (plane instanceof PassengerPlane && gateAvailable()){
						setPassengerPlaneToGate(plane);
					}
					else if (plane instanceof CargoPlane && cargoBayAvailable()) {
						setCargoPlaneToGate(plane);
					}
				}
				else {
					continue;
				}
			}
		}
		catch (Exception e){
			System.out.println(e.getClass().getName() + "-----" + e.getMessage());
		}
	}
	
	private static boolean gateAvailable() {
		if (Airport.getNumberOfOpenGates() > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private static boolean cargoBayAvailable() {
		if (Airport.getNumberOfOpenBays() > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private static Gate getGate() {
		Gate gate = null;
		try {
			gate = Airport.returnGate();
			if (gate == null) {
				throw new Exception("gate is null for som reason");
			}
		}
		catch (Exception e) {
			System.out.println(e.getClass().getName() + "-----" + e.getMessage());
		}
		
		return gate;
	}
	
	private static CargoBay getCargoBay() {
		CargoBay bay = null;
		try {
			bay = Airport.returnCargoBay();
			if (bay == null) {
				throw new Exception("gate is null for som reason");
			}
		}
		catch (Exception e) {
			System.out.println(e.getClass().getName() + "-----" + e.getMessage());
		}
		
		return bay;
	}
	
	private static void setPassengerPlaneToGate(Airplane plane) {
		System.out.println("giving gate to " + plane.getName());
		Gate gate = getGate();
		gate.service(plane);
		System.out.println("Landed Queue size: " + Airport.getLandedQueue().size());
		System.out.println("gates available: " + Airport.getNumberOfOpenGates());
	}
	
	private static void setCargoPlaneToGate(Airplane plane) {
		System.out.println("giving bay to" + plane.getName());
		CargoBay bay = getCargoBay();
		bay.service(plane);
		System.out.println("Landed Queue size: " + Airport.getLandedQueue().size());
		System.out.println("bays available: " + Airport.getNumberOfOpenBays());
	}
	
	private static boolean isNextPassengerPlane() {
		return false;
	}
	
	//private static Airplane getNextPlane() {
		//return -1;
	//}
}
