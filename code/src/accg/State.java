package accg;

import java.util.prefs.Preferences;

import org.newdawn.slick.Font;

import accg.camera.Camera;
import accg.gui.MainGUI;
import accg.objects.Floor;
import accg.objects.ShadowBlock;
import accg.objects.World;
import accg.simulation.Simulation;

/**
 * The state of the program. This for example stores the frame number.
 */
public class State {
	
	/**
	 * Default menu alignment (index in enumeration).
	 */
	public static final int DEF_MENU_ALIGNMENT = 1;
	
	/**
	 * Default menu position (index in enumeration).
	 */
	public static final int DEF_MENU_POSITION = 0;
	
	/**
	 * Default menu presentation (index in enumeration).
	 */
	public static final int DEF_MENU_PRESENTATION = 1;
	
	/**
	 * Default menu presentation (index in enumeration).
	 */
	public static final float DEF_MOUSE_SENSITIVITY = 0.5f;
	
	/**
	 * Enumeration of the modes in which the program can be.
	 */
	public enum ProgramMode {

		/**
		 * In this mode, no world is shown yet, and the user can choose one
		 * (that is, a level, a sand box or a saved file) to load.
		 */
		START_MODE,
		
		/**
		 * In this mode, the user can change the camera position, and choose another
		 * mode to work further.
		 */
		NORMAL_MODE,
		
		/**
		 * In this mode, a GUI is shown for placing and removing blocks.
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
	public ProgramMode programMode = ProgramMode.START_MODE;
	
	/**
	 * Camera used in the program.
	 */
	public Camera camera;
	
	/**
	 * Whether the user is removing blocks at the moment. This only makes sense
	 * in the {@link ProgramMode#BUILDING_MODE}.
	 * 
	 * If <code>true</code>, the user cannot build blocks; instead, blocks will
	 * be removed when the user clicks on them. If <code>false</code>, the user
	 * is able to build blocks instead.
	 */
	public boolean removingBlocks = false;
	
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
	 * Name of the currently loaded level. May be {@code null}.
	 */
	public String levelName;
	
	/**
	 * Number of the currently loaded level. May be -1 if not applicable.
	 */
	public int levelNumber;
	
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
	 * Position of the conveyor belts. This is separated from {@link #time},
	 * since the belts should not move when not in the simulation mode.
	 */
	public float beltPosition;
	
	/**
	 * Speed of the conveyor belts, as fraction of their full speed. This is
	 * the same as one minus the opacity of the arrows that are shown when the
	 * belts are not rotating.
	 */
	public float beltSpeed;
	
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
	 * This boolean indicates whether we are drawing shadows or not.
	 */
	public boolean drawingShadows;
	
	/**
	 * This boolean indicates whether we are in the invisible luggage drawing
	 * pass or not.
	 */
	public boolean drawingInvisibleLuggage;
	
	/**
	 * If not null, indicates that this object should be drawn where the mouse
	 * hovers the scene (using ray projection/intersection et cetera).
	 */
	public ShadowBlock shadowBlock;
	
	/**
	 * The GUI object.
	 */
	public MainGUI gui;
	
	/**
	 * Preferences object. Used to store user preferences persistently.
	 */
	public Preferences prefs;
	
	/**
	 * If the escape key has been pressed.
	 */
	public boolean escPressed = false;
	
	/**
	 * If a level is currently loaded that is built in. It may also be the case
	 * that no level is loaded at all when this value is false.
	 */
	public boolean loadedBuiltinLevel = false;
}
