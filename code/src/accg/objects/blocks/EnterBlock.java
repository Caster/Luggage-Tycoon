package accg.objects.blocks;

import static accg.gui.toolkit.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import javax.vecmath.Vector3f;

import accg.State;
import accg.objects.Block;

/**
 * A block in which the luggage enters the scene.
 */
public class EnterBlock extends StraightConveyorBlock {

	/**
	 * Default time in seconds between releasing pieces of luggage.
	 */
	public static final double DEFAULT_TIME_BETWEEN_LUGGAGE = 4;
	/**
	 * Colour of the hull surrounding the straight conveyor block.
	 */
	public static final Color HULL_COLOR = new Color(186, 189, 182);
	
	/**
	 * Series of points that define the hull around this block.
	 */
	private static Vector3f[] hullPoints = new Vector3f[] {
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
	 * The time between two luggage items.
	 */
	public double timeBetweenLuggage;
	
	/**
	 * Creates a new EnterBlock on the specified position.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @param orientation The orientation of this block.
	 * @param timeBetweenLuggage The time between two luggage items.
	 */
	public EnterBlock(int x, int y, int z, Orientation orientation, double timeBetweenLuggage) {
		super(x, y, z, orientation);
		
		this.timeBetweenLuggage = timeBetweenLuggage;
	}
	
	/**
	 * Creates a new EnterBlock by copying an existing one.
	 * @param block The block to copy from.
	 */
	public EnterBlock(EnterBlock block) {
		this(block.x, block.y, block.z, block.orientation, block.timeBetweenLuggage);
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
			drawQuadsAndNormals(hullPoints);
		}
		glEnd();
		glColor4f(Color.WHITE);
		
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
}
