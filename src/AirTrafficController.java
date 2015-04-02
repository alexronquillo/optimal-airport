import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;


public class AirTrafficController {
	ArrayBlockingQueue<Runway> runways;
	ArrayBlockingQueue<Airplane> arrivalsQueue;
	ArrayBlockingQueue<Airplane> landedQueue;
	ArrayBlockingQueue<Airplane> departureQueue;
	
	public AirTrafficController(ArrayBlockingQueue<Runway> runways, ArrayBlockingQueue<Airplane> arrivalsQueue, ArrayBlockingQueue<Airplane> landedQueue, ArrayBlockingQueue<Airplane> departureQueue) {
		this.runways = runways;
		this.arrivalsQueue = arrivalsQueue;
		this.landedQueue = landedQueue;
		this.departureQueue = departureQueue;
	}
	
	public void start() {
		while (true) {
			if (hasRunway()) {
				if (hasLandingVacancy() && hasArrivals()) {
					Airplane airplane = arrivalsQueue.poll();
					System.out.println("ATC signals plane to land");
					signalLanding(airplane);
				} else if (hasPlanesAwaitingTakeoff()) {
					Airplane airplane = departureQueue.poll(); 
					System.out.println("ATC signals plane to takeoff");
					signalTakeoff(airplane);
				}
			} 
		}
	}
	
	public void addToLandedQueue(Airplane airplane) {
		landedQueue.offer(airplane);
		System.out.println("Airplane added to landed queue");
	}
	
	public void addRunway(Runway runway) {
		runways.offer(runway);
	}
	
	private boolean hasRunway() {
		return runways.size() > 0;
	}
	
	private boolean hasLandingVacancy() {
		return landedQueue.remainingCapacity() > 0;
	}
	
	private boolean hasArrivals() {
		return arrivalsQueue.size() > 0;
	}
	
	private boolean hasPlanesAwaitingTakeoff() {
		return departureQueue.size() > 0;
	}
	
	private void signalLanding(Airplane airplane) {
		airplane.land(this, runways.poll());
	}
	
	private void signalTakeoff(Airplane airplane) {
		airplane.takeoff(this, runways.poll());
	}
}
