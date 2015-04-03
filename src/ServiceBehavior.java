
public interface ServiceBehavior {
	public void service(Airplane airplane) throws InvalidAirplaneTypeException, InterruptedException;	
	public long getServiceTime(Airplane airplane);
}
