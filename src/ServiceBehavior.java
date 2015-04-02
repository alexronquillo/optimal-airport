
public interface ServiceBehavior {
	public void service(Airplane airplane) throws InvalidAirplaneTypeException, InterruptedException;
}
