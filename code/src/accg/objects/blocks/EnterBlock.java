package accg.objects.blocks;

import static accg.gui.toolkit.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import javax.vecmath.Vector3f;

import accg.State;
import accg.State.ProgramMode;
import accg.objects.Block;

/**
 * A block in which the luggage enters the scene.
 */
public class EnterBlock extends StraightConveyorBlock {

	/**
	 * Default time in seconds between releasing pieces of luggage.
	 */
	public static final float DEFAULT_TIME_BETWEEN_LUGGAGE = 4;
	/**
	 * Color of the hull surrounding the straight conveyor block.
	 */
	public static final Color HULL_COLOR = new Color(186, 189, 182);
	/**
	 * Series of points that define the hull around this block.
	 */
	public static final Vector3f[] HULL_POINTS = new Vector3f[] {
		new Vector3f(-0.5f, 0.375f, 1),
		new Vector3f(0.5f, 0.375f, 1),
		new Vector3f(0.5f, 0.375f, 0.875f),
		new Vector3f(-0.5f, 0.375f, 0.875f),
		
		new Vector3f(0.5f, 0.375f, 1),
		new Vector3f(-0.5f, 0.375f, 1),
		new Vector3f(-0.5f, -0.5f, 1),
		new Vector3f(0.5f, -0.5f, 1),
		
		new Vector3f(0.5f, -0.5f, 1),
		new Vector3f(-0.5f, -0.5f, 1),
		new Vector3f(-0.5f, -0.5f, 0),
		new Vector3f(0.5f, -0.5f, 0),
		
		new Vector3f(-0.5f, -0.5f, 0),
		new Vector3f(0.5f, -0.5f, 0),
		new Vector3f(0.5f, 0.375f, 0),
		new Vector3f(-0.5f, 0.375f, 0),
		
		new Vector3f(0.5f, 0.375f, 0),
		new Vector3f(-0.5f, 0.375f, 0),
		new Vector3f(-0.5f, 0.375f, 0.125f),
		new Vector3f(0.5f, 0.375f, 0.125f),
		
		new Vector3f(-0.5f, -0.5f, 0),
		new Vector3f(-0.5f, -0.5f, 1),
		new Vector3f(-0.5f, 0.375f, 1),
		new Vector3f(-0.5f, 0.375f, 0),
		
		new Vector3f(-0.5f, 0.375f, 0.125f),
		new Vector3f(-0.5f, 0.375f, 0.875f),
		new Vector3f(-0.375f, 0.375f, 0.875f),
		new Vector3f(-0.375f, 0.375f, 0.125f),
		
		new Vector3f(0.5f, -0.5f, 1),
		new Vector3f(0.5f, -0.5f, 0),
		new Vector3f(0.5f, 0.375f, 0),
		new Vector3f(0.5f, 0.375f, 1),
		
		new Vector3f(0.5f, 0.375f, 0.875f),
		new Vector3f(0.5f, 0.375f, 0.125f),
		new Vector3f(0.375f, 0.375f, 0.125f),
		new Vector3f(0.375f, 0.375f, 0.875f)
	};
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
		
		this.timeBetweenLuggage = timeBetweenLuggage;
	}
	
	/**
	 * Creates a new EnterBlock by copying an existing one.
	 * @param block The block to copy from.
	 */
	public EnterBlock(EnterBlock block) {
		this(block.x, block.y, block.z, block.orientation,
				block.timeBetweenLuggage);
	}

	@Override
	public void draw(State s) {
		
		super.draw(s);

		glPushMatrix();
		glTranslated(x, y, z / 4.0);
		glRotated(-orientation.angle, 0, 0, 1);
		
		glColor4f(HULL_COLOR);
		glBegin(GL_QUADS);
		{
			drawQuadsAndNormals(HULL_POINTS);
		}
		glEnd();
		
		float sof = getShutterOpenFactor(s);
		glColor4f(Color.WHITE);
		glEnable(GL_TEXTURE_2D);
		s.textures.shutter.bind();
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
	public ConveyorBlockType getConveyorBlockType() {
		return ConveyorBlockType.ENTER;
	}
	
	/**
	 * Given a State to retrieve the current time from, return how far open
	 * the shutter should be at this time, given the {@link #timeBetweenLuggage}.
	 * 
	 * @return How far open the shutter should be. 0 means completely open,
	 *         1 means completely closed.
	 */
	private float getShutterOpenFactor(State s) {
		if (s.programMode != ProgramMode.SIMULATION_MODE) {
			return 1;
		}
		
		float timeMod = ((s.time % timeBetweenLuggage) + 2 * timeBetweenLuggage -
				SHUTTER_OPEN_TIME_SHIFT) % timeBetweenLuggage;
		
		if (timeMod <= SHUTTER_STAY_OPEN_TIME) {
			return 0;
		}
		
		if (timeMod <= timeBetweenLuggage / 2 + SHUTTER_STAY_OPEN_TIME) {
			timeMod -= SHUTTER_STAY_OPEN_TIME;
			return 1 - Math.max((SHUTTER_OPEN_TIME - timeMod) / SHUTTER_OPEN_TIME, 0);
		}
		
		timeMod = timeBetweenLuggage - timeMod;
		return 1 - Math.max((SHUTTER_OPEN_TIME - timeMod) / SHUTTER_OPEN_TIME, 0);
	}
}
