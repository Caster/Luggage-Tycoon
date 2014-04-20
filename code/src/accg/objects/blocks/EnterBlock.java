package accg.objects.blocks;

import static accg.gui.toolkit.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.util.ArrayList;

import javax.vecmath.Vector3f;

import accg.State;
import accg.State.ProgramMode;
import accg.objects.Block;
import accg.objects.Luggage.LuggageColor;
import accg.objects.Orientation;

/**
 * A block in which the luggage enters the scene.
 */
public class EnterBlock extends FlatConveyorBlock {

	/**
	 * Default time in seconds between releasing pieces of luggage.
	 */
	public static final float DEFAULT_TIME_BETWEEN_LUGGAGE = 4;
	/**
	 * Series of points that define the hull around this block.
	 */
	public static final Vector3f[] HULL_POINTS = new Vector3f[] {
		// top front quad
		new Vector3f(-0.5f, 0.375f, 1),
		new Vector3f(0.5f, 0.375f, 1),
		new Vector3f(0.5f, 0.375f, 0.875f),
		new Vector3f(-0.5f, 0.375f, 0.875f),
		
		// top quad ("ceiling")
		new Vector3f(0.5f, 0.375f, 1),
		new Vector3f(-0.5f, 0.375f, 1),
		new Vector3f(-0.5f, -0.5f, 1),
		new Vector3f(0.5f, -0.5f, 1),
		
		// top quad of "bump" below conveyor sticking out
		new Vector3f(0.375f, 0.375f, 0.125f),
		new Vector3f(0.375f, 0.5f, 0.125f),
		new Vector3f(-0.375f, 0.5f, 0.125f),
		new Vector3f(-0.375f, 0.375f, 0.125f),
		
		// top left quad of "bump" below conveyor sticking out
		new Vector3f(-0.375f, 0.375f, 0.625f),
		new Vector3f(-0.375f, 0.5f, 0.625f),
		new Vector3f(-0.5f, 0.5f, 0.625f),
		new Vector3f(-0.5f, 0.375f, 0.625f),
		
		// top right quad of "bump" below conveyor sticking out
		new Vector3f(0.5f, 0.375f, 0.625f),
		new Vector3f(0.5f, 0.5f, 0.625f),
		new Vector3f(0.375f, 0.5f, 0.625f),
		new Vector3f(0.375f, 0.375f, 0.625f),
		
		// left outer wall of "bump" below conveyor sticking out
		new Vector3f(-0.5f, 0.375f, 0),
		new Vector3f(-0.5f, 0.375f, 0.625f),
		new Vector3f(-0.5f, 0.5f, 0.625f),
		new Vector3f(-0.5f, 0.5f, 0),
		
		// right outer wall of "bump" below conveyor sticking out
		new Vector3f(0.5f, 0.375f, 0),
		new Vector3f(0.5f, 0.5f, 0),
		new Vector3f(0.5f, 0.5f, 0.625f),
		new Vector3f(0.5f, 0.375f, 0.625f),
		
		// back outer quad
		new Vector3f(0.5f, -0.5f, 1),
		new Vector3f(-0.5f, -0.5f, 1),
		new Vector3f(-0.5f, -0.5f, 0),
		new Vector3f(0.5f, -0.5f, 0),
		
		// back inner quad
		new Vector3f(0.375f, -0.375f, 0.625f),
		new Vector3f(0.375f, -0.375f, 0.125f),
		new Vector3f(-0.375f, -0.375f, 0.125f),
		new Vector3f(-0.375f, -0.375f, 0.625f),
		
		// floor quad
		new Vector3f(-0.5f, -0.5f, 0),
		new Vector3f(0.5f, -0.5f, 0),
		new Vector3f(0.5f, 0.5f, 0),
		new Vector3f(-0.5f, 0.5f, 0),
		
		// front bottom quad
		new Vector3f(0.5f, 0.5f, 0),
		new Vector3f(-0.5f, 0.5f, 0),
		new Vector3f(-0.5f, 0.5f, 0.125f),
		new Vector3f(0.5f, 0.5f, 0.125f),
		
		// left outer wall
		new Vector3f(-0.5f, -0.5f, 0),
		new Vector3f(-0.5f, -0.5f, 1),
		new Vector3f(-0.5f, 0.375f, 1),
		new Vector3f(-0.5f, 0.375f, 0),
		
		// left inner wall
		new Vector3f(-0.375f, -0.375f, 0.625f),
		new Vector3f(-0.375f, -0.375f, 0.125f),
		new Vector3f(-0.375f, 0.5f, 0.125f),
		new Vector3f(-0.375f, 0.5f, 0.625f),
		
		// left front quad
		new Vector3f(-0.5f, 0.375f, 0.625f),
		new Vector3f(-0.5f, 0.375f, 0.875f),
		new Vector3f(-0.375f, 0.375f, 0.875f),
		new Vector3f(-0.375f, 0.375f, 0.625f),
		
		// left front quad ("bump")
		new Vector3f(-0.5f, 0.5f, 0),
		new Vector3f(-0.5f, 0.5f, 0.625f),
		new Vector3f(-0.375f, 0.5f, 0.625f),
		new Vector3f(-0.375f, 0.5f, 0),
		
		// right outer wall
		new Vector3f(0.5f, -0.5f, 1),
		new Vector3f(0.5f, -0.5f, 0),
		new Vector3f(0.5f, 0.375f, 0),
		new Vector3f(0.5f, 0.375f, 1),
		
		// right inner wall
		new Vector3f(0.375f, -0.375f, 0.125f),
		new Vector3f(0.375f, -0.375f, 0.625f),
		new Vector3f(0.375f, 0.5f, 0.625f),
		new Vector3f(0.375f, 0.5f, 0.125f),
		
		// right front quad
		new Vector3f(0.5f, 0.375f, 0.875f),
		new Vector3f(0.5f, 0.375f, 0.625f),
		new Vector3f(0.375f, 0.375f, 0.625f),
		new Vector3f(0.375f, 0.375f, 0.875f),
		
		// right front quad ("bump")
		new Vector3f(0.5f, 0.5f, 0.625f),
		new Vector3f(0.5f, 0.5f, 0),
		new Vector3f(0.375f, 0.5f, 0),
		new Vector3f(0.375f, 0.5f, 0.625f)
	};
	static {
		applyZFightingCorrection(HULL_POINTS);
	}
	/**
	 * Time it takes, in seconds, to open the shutter completely.
	 */
	public static final float SHUTTER_OPEN_TIME = 0.6f;
	/**
	 * Time the opening/closing of the shutter is shifted relative to adding
	 * a new piece of luggage. This means that the shutter will not be (fully)
	 * open when a new piece of luggage is added, but this amount of time later. 
	 */
	public static final float SHUTTER_OPEN_TIME_SHIFT = 0.4f;
	/**
	 * Time the shutter should stay open after reaching the open state.
	 */
	public static final float SHUTTER_STAY_OPEN_TIME = 0.1f;
	
	/**
	 * The time between two luggage items.
	 */
	public float timeBetweenLuggage;
	
	/**
	 * A list of luggage colors from which can be chosen when generating luggage.
	 * In case this is {@code null}, all colors can be used.
	 */
	protected ArrayList<LuggageColor> luggageColors;
	/**
	 * Number of pieces of luggage that should enter the scene in total.
	 */
	protected int luggageNum;
	/**
	 * Number of pieces of luggage that were generated in total.
	 */
	protected int generatedLuggage;
	
	/**
	 * Creates a new EnterBlock on the specified position.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @param orientation The orientation of this block.
	 * @param timeBetweenLuggage The time between two luggage items.
	 */
	public EnterBlock(int x, int y, int z, Orientation orientation,
			float timeBetweenLuggage) {
		super(x, y, z, orientation);
		init(timeBetweenLuggage);
	}
	
	/**
	 * Creates a new EnterBlock on the specified position.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @param orientation The orientation of this block.
	 * @param timeBetweenLuggage The time between two luggage items.
	 * @param deletable If this block may be deleted by a user or not.
	 */
	public EnterBlock(int x, int y, int z, Orientation orientation,
			float timeBetweenLuggage, boolean deletable) {
		super(x, y, z, orientation, deletable);
		init(timeBetweenLuggage);
		
	}
	
	/**
	 * Creates a new EnterBlock by copying an existing one.
	 * @param block The block to copy from.
	 */
	public EnterBlock(EnterBlock block) {
		this(block.x, block.y, block.z, block.orientation,
				block.timeBetweenLuggage);
	}

	/**
	 * Initialisation method that is called from all constructors.
	 */
	private void init(float timeBetweenLuggage) {
		this.type = ConveyorBlockType.ENTER;
		this.timeBetweenLuggage = timeBetweenLuggage;
		this.generatedLuggage = 0;
		this.luggageColors = null;
		this.luggageNum = -1;
	}
	
	@Override
	public void draw(State s) {
		
		super.draw(s);

		glPushMatrix();
		glTranslated(x, y, z / 4.0);
		glRotated(-orientation.angle, 0, 0, 1);
		
		float sof = getShutterOpenFactor(s);
		glColor4f(Color.WHITE);
		glEnable(GL_TEXTURE_2D);
		s.textures.shutterEnter.bind();
		glBegin(GL_QUADS);
		{
			glNormal3f(0, 1, 0);
			// completely open is at 0.85, not 0.875, therefore not 1 but 0.947
			glTexCoord2f(1, 0.947f - sof);
			glVertex3f(0.375f, 0.375f, 0.875f);
			glTexCoord2f(0, 0.947f - sof);
			glVertex3f(-0.375f, 0.375f, 0.875f);
			glTexCoord2f(0, 1);
			glVertex3f(-0.375f, 0.375f, 0.85f - sof * 0.45f);
			glTexCoord2f(1, 1);
			glVertex3f(0.375f, 0.375f, 0.85f - sof * 0.45f);
		}
		glEnd();
		glDisable(GL_TEXTURE_2D);
		
		glPopMatrix();
	}

	@Override
	public Block clone() {
		return new EnterBlock(this);
	}
	
	@Override
	public String getBlockID() {
		return "eb";
	}
	
	@Override
	public ConveyorBlockType getConveyorBlockType() {
		return ConveyorBlockType.ENTER;
	}
	
	/**
	 * Returns the number of pieces of luggage that has been generated so far.
	 * @return The number of pieces of luggage that has been generated so far.
	 */
	public int getGeneratedLuggageNum() {
		return generatedLuggage;
	}
	
	@Override
	public int getHeight() {
		return 4;
	}
	
	@Override
	public Vector3f[] getHullPoints() {
		return HULL_POINTS;
	}
	
	/**
	 * Returns the list of colors from which can be chosen when generating
	 * luggage at this block.
	 * @return The list of colors, or {@code null} if any color is okay.
	 */
	public ArrayList<LuggageColor> getLuggageColors() {
		return luggageColors;
	}
	
	/**
	 * Returns the maximum number of pieces of luggage that can be generated at
	 * this block.
	 * @return The number of pieces of luggage that can be generated.
	 */
	public int getLuggageNum() {
		return luggageNum;
	}
	
	/**
	 * Increment the generated luggage count.
	 */
	public void incrementGeneratedLuggageNum() {
		this.generatedLuggage++;
	}
	
	/**
	 * Reset the count of generated luggage to zero. Can be used at the start of
	 * a simulation for example.
	 */
	public void resetGeneratedLuggageNum() {
		this.generatedLuggage = 0;
	}
	
	/**
	 * Changes the colors of luggage that can be generated at this block.
	 * @param luggageColors Colors to choose from.
	 */
	public void setLuggageColors(ArrayList<LuggageColor> luggageColors) {
		this.luggageColors = luggageColors;
	}
	
	/**
	 * Changes the number of items that can be generated at this block.
	 * @param luggageNum Number of items that can be generated.
	 */
	public void setLuggageNum(int luggageNum) {
		this.luggageNum = luggageNum;
	}
	
	/**
	 * Given a State to retrieve the current time from, return how far open
	 * the shutter should be at this time, given the {@link #timeBetweenLuggage}.
	 * 
	 * @return How far open the shutter should be. 0 means completely open,
	 *         1 means completely closed.
	 */
	private float getShutterOpenFactor(State s) {
		if (s.programMode != ProgramMode.SIMULATION_MODE ||
				(generatedLuggage > luggageNum && luggageNum >= 0)) {
			return 1;
		}
		
		float timeMod = ((s.time % timeBetweenLuggage) + 2 * timeBetweenLuggage -
				SHUTTER_OPEN_TIME_SHIFT) % timeBetweenLuggage;
		
		if (timeMod <= SHUTTER_STAY_OPEN_TIME) {
			return 0;
		}
		
		if (timeMod <= timeBetweenLuggage / 2 + SHUTTER_STAY_OPEN_TIME) {
			timeMod -= SHUTTER_STAY_OPEN_TIME;
			float result = 1 - Math.max((SHUTTER_OPEN_TIME - timeMod) / SHUTTER_OPEN_TIME, 0);
			if (result > 0.97f && generatedLuggage == luggageNum) {
				// shutter does not need to open anymore
				generatedLuggage++;
			}
			return result;
		}
		
		timeMod = timeBetweenLuggage - timeMod;
		return 1 - Math.max((SHUTTER_OPEN_TIME - timeMod) / SHUTTER_OPEN_TIME, 0);
	}
	
	@Override
	public void drawArrow(State s) {
		// do not draw an arrow for EnterBlocks
	}
}
