package accg.objects.blocks;

import static accg.gui.toolkit.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import org.lwjgl.util.glu.Cylinder;

import accg.State;
import accg.objects.Block;
import accg.objects.Luggage;
import accg.utils.Utils;

/**
 * A block in which the luggage enters the scene.
 */
public class EnterBlock extends StraightConveyorBlock {
	
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
		
		glBegin(GL_QUAD_STRIP);
		{
			glVertex3f(-0.5f, 0.375f, 0.875f);
			glVertex3f(0.5f, 0.375f, 0.875f);
			glVertex3f(-0.5f, 0.375f, 1);
			glVertex3f(0.5f, 0.375f, 1);
			glVertex3f(-0.5f, -0.5f, 1);
			glVertex3f(0.5f, -0.5f, 1);
			glVertex3f(-0.5f, -0.5f, 0);
			glVertex3f(0.5f, -0.5f, 0);
			glVertex3f(-0.5f, 0.375f, 0);
			glVertex3f(0.5f, 0.375f, 0);
			glVertex3f(-0.5f, 0.375f, 0.125f);
			glVertex3f(0.5f, 0.375f, 0.125f);
		}
		glEnd();
		
		glPopMatrix();
	}

	@Override
	public Block clone() {
		return new EnterBlock(this);
	}
}
