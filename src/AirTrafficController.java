import java.util.List;


public class AirTrafficController {
	List<Runway> runways;
	
	public AirTrafficController(List<Runway> runways) {
		this.runways = runways;
	}
	
	public void addRunway(Runway runway) {
		System.out.println("Runway added");
		runways.add(runway);
	}
}
