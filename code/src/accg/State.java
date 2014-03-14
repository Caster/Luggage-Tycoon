package accg;

import org.newdawn.slick.Font;

import accg.objects.Floor;
import accg.objects.ShadowBlock;
import accg.objects.World;
import accg.simulation.Simulation;

/**
 * The state of the program. This for example stores the frame number.
 */
public class State {
	
	/**
	 * Enumeration of the modes in which the program can be.
	 */
	public enum ProgramMode {
		
		/**
		 * In this mode, the user can place and remove blocks.
		 */
		BUILDING_MODE,
		
		/**
		 * In this mode, luggage movement is simulated over the blocks.
		 */
		SIMULATION_MODE;
	}
	
	/**
	 * Mode in which the program can be.
	 * 
	 * At the moment, this is either the building mode or the simulation
	 * mode, also see {@link State.ProgramMode}.
	 */
	public ProgramMode programMode = ProgramMode.BUILDING_MODE;
	
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
	 * The simulation time in seconds when the last frame was rendered.
	 * 
	 * May of course not be initialized, during the rendering of the first
	 * frame.
	 */
	public float prevTime;
	
	/**
	 * The current simulation time in seconds.
	 */
	public float time;
	
	/**
	 * The time at which the simulation started.
	 */
	public float startTime;
	
	/**
	 * The {@link Textures} object that contains the textures for the
	 * application.
	 */
	public Textures textures;
	
	/**
	 * The world object that contains all objects to draw (except for the
	 * floor).
	 */
	public World world;
	
	/**
	 * The floor to draw.
	 */
	public Floor floor;
	
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
	
	/**
	 * This boolean indicates whether we are in the first or second rendering
	 * pass. The first rendering pass, transparent objects are not rendered
	 * (then this is <code>false</code>). In the second rendering pass, they
	 * are rendered (then this is <code>true</code>). Then, they are combined
	 * to make the objects really transparent.
	 */
	public boolean drawTransparentObjects;

	/**
	 * This boolean indicates whether we are drawing shadows or not.
	 */
	public boolean drawingShadows;
	
	/**
	 * If not null, indicates that this object should be drawn where the mouse
	 * hovers the scene (using ray projection/intersection et cetera).
	 */
	public ShadowBlock shadowBlock;
}
