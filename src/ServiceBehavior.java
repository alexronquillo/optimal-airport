
public interface ServiceBehavior {
	public void service(Airplane airplane, double serviceTime) throws InvalidAirplaneTypeException, InterruptedException;	
	public long getServiceTime(Airplane airplane, double serviceTime);
}
