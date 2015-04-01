import java.util.List;

public class GroundMovementController {
	List<Gate> gates;
	List<CargoBay> bays;
	
	public GroundMovementController(List<Gate> gates, List<CargoBay> bays) {
		this.gates = gates;
		this.bays = bays;
	}
	
	public void start() {
		
	}
	
	private boolean gateAvailable() {
		return false;	
	}
	
	private boolean cargoBayAvailable() {
		return false;	
	}
	
	private Gate getGate() {
		return null;
	}
	
	private CargoBay getCargoBay() {
		return null;
	}
	
	private void setPassengerPlaneToGate() {
		
	}
	
	private void setCargoPlaneToGate() {
		
	}
	
	private boolean isNextPassengerPlane() {
		return false;
	}
	
	//private Airplane getNextPlane() {
		//return -1;
	//}
}
