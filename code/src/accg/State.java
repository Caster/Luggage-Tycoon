package accg;

import org.newdawn.slick.Font;

import accg.objects.World;

/**
 * The state of the program. This for example stores the frame number.
 */
public class State {
	
	/**
	 * Length of the field, in number of grid squares.
	 * 
	 * <p>This is the number of squares along the X-axis.</p>
	 */
	public int fieldLength = 20;
	
	/**
	 * Width of the field, in number of grid squares.
	 * 
	 * <p>This is the number of squares along the Y-axis.</p>
	 */
	public int fieldWidth = 16;
	
	/**
	 * Height of the field, in number of grid squares.
	 * 
	 * <p>This is the allowed height to build.</p>
	 */
	public int fieldHeight = 20;
	
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
	
	/**
	 * Font that is used in all menus.
	 */
	public Font fontMenu;
	
	/**
	 * This factor is used to multiply camera movements with. It can be
	 * used to make the program more or less sensitive to mouse interaction.
	 */
	public float mouseSensitivityFactor = 1.0f;
}
