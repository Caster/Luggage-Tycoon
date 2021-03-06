package accg.simulation;

/**
 * This enumeration enumerates types of RigidBody objects in the Simulation.
 */
public enum SimulationBodyType {
	/**
	 * The floor.
	 */
	FLOOR,
	/**
	 * One of the four walls.
	 */
	WALL,
	/**
	 * Some conveyor block.
	 */
	CONVEYOR_BLOCK,
	/**
	 * A block that "eats up" luggage.
	 */
	LEAVE_BLOCK,
	/**
	 * Some piece of luggage.
	 */
	LUGGAGE;
}
