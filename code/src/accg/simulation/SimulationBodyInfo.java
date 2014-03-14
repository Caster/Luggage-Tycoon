package accg.simulation;

/**
 * This class is a small wrapper that can have a user pointer (anything, basically)
 * and a type. It is meant to be attached to a body in the simulation and be used
 * to get the type of body (object that it represents) and possibly a pointer to the
 * object that is drawn in the program.
 */
public class SimulationBodyInfo {

	/**
	 * Construct a new information object with given contents.
	 * @param userPointer Pointer to anything, may be {@code null}.
	 * @param bodyType Type of body.
	 */
	public SimulationBodyInfo(Object userPointer, SimulationBodyType bodyType) {
		this.bodyType = bodyType;
		this.userPointer = userPointer;
	}
	
	/**
	 * Return the type of body as set at construction time.
	 * @return The type of body.
	 */
	public SimulationBodyType getBodyType() {
		return bodyType;
	}
	
	/**
	 * Return the object that was set at construction time, may be {@code null}.
	 * @return The object set at construction time.
	 */
	public Object getUserPointer() {
		return userPointer;
	}
	
	/**
	 * Type of the body.
	 */
	private SimulationBodyType bodyType;
	/**
	 * Pointer to whatever the user wants. May be {@code null}.
	 */
	private Object userPointer;
}
