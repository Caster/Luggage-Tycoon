package accg.objects.blocks;

import static accg.gui.toolkit.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.util.ArrayList;

import javax.vecmath.Vector3f;

import accg.State;
import accg.objects.Block;
import accg.objects.Luggage.LuggageColor;
import accg.objects.Orientation;

/**
 * A block in which the luggage exits the scene (hopefully).
 */
public class LeaveBlock extends FlatConveyorBlock {
	
	/**
	 * Color of the hull surrounding the straight conveyor block.
	 */
	public static final Color HULL_COLOR = new Color(186, 189, 182);
	
	/**
	 * Series of points that define the hull around this block.
	 */
	public static final Vector3f[] HULL_POINTS = new Vector3f[] {
		new Vector3f(-0.5f, -0.375f, 1),
		new Vector3f(-0.5f, -0.375f, 0.875f),
		new Vector3f(0.5f, -0.375f, 0.875f),
		new Vector3f(0.5f, -0.375f, 1),
		
		new Vector3f(0.5f, -0.375f, 1),
		new Vector3f(0.5f, 0.5f, 1),
		new Vector3f(-0.5f, 0.5f, 1),
		new Vector3f(-0.5f, -0.375f, 1),
		
		new Vector3f(0.5f, 0.5f, 1),
		new Vector3f(0.5f, 0.5f, 0),
		new Vector3f(-0.5f, 0.5f, 0),
		new Vector3f(-0.5f, 0.5f, 1),
		
		new Vector3f(-0.5f, 0.5f, 0),
		new Vector3f(-0.5f, -0.375f, 0),
		new Vector3f(0.5f, -0.375f, 0),
		new Vector3f(0.5f, 0.5f, 0),
		
		new Vector3f(0.5f, -0.375f, 0),
		new Vector3f(0.5f, -0.375f, 0.125f),
		new Vector3f(-0.5f, -0.375f, 0.125f),
		new Vector3f(-0.5f, -0.375f, 0),
		
		new Vector3f(-0.5f, 0.5f, 0),
		new Vector3f(-0.5f, -0.375f, 0),
		new Vector3f(-0.5f, -0.375f, 1),
		new Vector3f(-0.5f, 0.5f, 1),
		
		new Vector3f(-0.5f, -0.375f, 0.125f),
		new Vector3f(-0.375f, -0.375f, 0.125f),
		new Vector3f(-0.375f, -0.375f, 0.875f),
		new Vector3f(-0.5f, -0.375f, 0.875f),
		
		new Vector3f(0.5f, 0.5f, 1),
		new Vector3f(0.5f, -0.375f, 1),
		new Vector3f(0.5f, -0.375f, 0),
		new Vector3f(0.5f, 0.5f, 0),
		
		new Vector3f(0.5f, -0.375f, 0.875f),
		new Vector3f(0.375f, -0.375f, 0.875f),
		new Vector3f(0.375f, -0.375f, 0.125f),
		new Vector3f(0.5f, -0.375f, 0.125f)
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
	 * Colors of luggage that are accepted by this block.
	 */
	private ArrayList<LuggageColor> acceptColors;
	
	/**
	 * Creates a new LeaveBlock on the specified position.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @param orientation The orientation of this block.
	 */
	public LeaveBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation);
		
		this.type = ConveyorBlockType.ENTER;
		this.acceptColors = null;
	}
	
	/**
	 * Creates a new LeaveBlock on the specified position.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @param orientation The orientation of this block.
	 * @param deletable If this block may be deleted by a user or not.
	 */
	public LeaveBlock(int x, int y, int z, Orientation orientation,
			boolean deletable) {
		super(x, y, z, orientation, deletable);
		
		this.type = ConveyorBlockType.ENTER;
	}
	
	/**
	 * Creates a new LeaveBlock by copying an existing one.
	 * @param block The block to copy from.
	 */
	public LeaveBlock(LeaveBlock block) {
		this(block.x, block.y, block.z, block.orientation);
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
		s.textures.shutterExit.bind();
		glBegin(GL_QUADS);
		{
			glNormal3f(0, -1, 0);
			// completely open is at 0.85, not 0.875, therefore not 1 but 0.947
			glTexCoord2f(1, 0.947f - sof);
			glVertex3f(-0.375f, -0.375f, 0.875f);
			glTexCoord2f(0, 0.947f - sof);
			glVertex3f(0.375f, -0.375f, 0.875f);
			glTexCoord2f(0, 1);
			glVertex3f(0.375f, -0.375f, 0.85f - sof * 0.45f);
			glTexCoord2f(1, 1);
			glVertex3f(-0.375f, -0.375f, 0.85f - sof * 0.45f);
		}
		glEnd();
		glDisable(GL_TEXTURE_2D);
		
		glPopMatrix();
	}

	@Override
	public Block clone() {
		return new LeaveBlock(this);
	}
	
	/**
	 * Returns a list of colors that are accepted by this block.
	 * @return A list of colors that are accepted by this block.
	 *         If any color is accepted, {@code null} is returned.
	 */
	public ArrayList<LuggageColor> getAcceptColors() {
		return acceptColors;
	}
	
	@Override
	public ConveyorBlockType getConveyorBlockType() {
		return ConveyorBlockType.LEAVE;
	}
	
	/**
	 * Change the list of colors that is accepted by this block.
	 * @param acceptColors The new list of accepted colors, or {@code null} if
	 *            any block may be accepted.
	 */
	public void setAcceptColors(ArrayList<LuggageColor> acceptColors) {
		this.acceptColors = acceptColors;
	}
	
	/**
	 * Given a State to retrieve the current time from, return how far open
	 * the shutter should be at this time, given the {@link #timeBetweenLuggage}.
	 * 
	 * @return How far open the shutter should be. 0 means completely open,
	 *         1 means completely closed.
	 */
	private float getShutterOpenFactor(State s) {
		/*if (s.programMode != ProgramMode.SIMULATION_MODE) {
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
		return 1 - Math.max((SHUTTER_OPEN_TIME - timeMod) / SHUTTER_OPEN_TIME, 0);*/
		
		return 1;
	}
}