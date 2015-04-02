
public class InvalidAirplaneTypeException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidAirplaneTypeException() {
		super("Invalid airplane type");
	}
	
	public InvalidAirplaneTypeException(String message) {
		super(message);
	}
}
