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
public class EnterBlock extends Block {
	
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
		
		// TODO draw scaffolding here, but it is ugly to duplicate this code from
		// ConveyorBlock
		// so make some API for that and use it in both cases

		glPushMatrix();
		glTranslated(x, y, z / 4.0);
		glRotated(-orientation.angle, 0, 0, 1);
		
		if (Utils.hasTimePassed(s, 1.0, 0)) {
			Luggage newLuggage = new Luggage(x, y, z / 4f + 2f);
			s.world.luggage.addObject(newLuggage);
			s.simulation.addLuggageToPhysicsEngine(newLuggage);
		}
		
		glBegin(GL_QUADS);
		{
			glVertex3f(x - 0.5f, y - 0.5f, z / 4f);
			glVertex3f(x - 0.5f, y + 0.5f, z / 4f);
			glVertex3f(x + 0.5f, y + 0.5f, z / 4f);
			glVertex3f(x + 0.5f, y - 0.5f, z / 4f);
		}
		glEnd();
		
		glPopMatrix();
	}
	
	@Override
	public int getHeight() {
		return 2;
	}

	@Override
	public Block clone() {
		return new EnterBlock(this);
	}
}
