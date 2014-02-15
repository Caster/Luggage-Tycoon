package accg;

import accg.objects.World;

/**
 * The state of the program. This for example stores the frame number.
 */
public class State {
	
	/**
	 * The current frame number.
	 */
	public int frame;
	
	/**
	 * The current simulation time in seconds.
	 */
	public double time;
	
	/**
	 * The time at which the simulation started.
	 */
	public double startTime;
	
	/**
	 * The {@link Textures} object that contains the textures for the
	 * application.
	 */
	public Textures textures;
	
	/**
	 * The world object that contains all objects to draw.
	 */
	public World world;
	
	/**
	 * The simulation.
	 */
	public Simulation simulation;
}
