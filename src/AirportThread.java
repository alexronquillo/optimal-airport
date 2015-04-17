
public class AirportThread extends Thread {
	AirportRunnable runnable;
	
	public AirportThread(AirportRunnable runnable) {
		super(runnable);
		this.runnable = runnable;
	}
	
	public void terminate() {
		runnable.terminate();
	}
}
