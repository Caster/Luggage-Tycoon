package accg.objects.blocks;

import static accg.utils.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import accg.State;
import accg.objects.Block;
import accg.objects.Luggage;

/**
 * Generic super class of all conveyor blocks. This class is overridden by the
 * block classes.
 * 
 * A {@link ConveyorBlock} manages the drawing of conveyor belts and the simulation
 * <b>(not yet of course)</b>, based on abstract methods that indicate the exact
 * shape of the conveyor belt.
 * 
 * ConveyorBlocks use the following coordinate system: they are centered around
 * the origin (0, 0), and the conveyor belt moves towards the positive y direction.
 */
public abstract class ConveyorBlock extends Block {
	
	/**
	 * Creates a new ConveyorBlock on the specified position.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @param orientation The orientation of this block.
	 */
	public ConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation);
	}
	
	/**
	 * Returns a list of the coordinates on the left side of the top part
	 * of the conveyor belt.
	 * 
	 * @return An ArrayList of the coordinates.
	 */
	protected abstract ArrayList<Vector3f> getTopCoordinatesLeft();
	
	/**
	 * Returns a list of the coordinates on the right side of the top part
	 * of the conveyor belt.
	 * 
	 * @return An ArrayList of the coordinates.
	 */
	protected abstract ArrayList<Vector3f> getTopCoordinatesRight();
	
	/**
	 * Returns a list of the texture coordinates for the top part of the conveyor belt.
	 * These should correspond to the coordinates given by the methods
	 * {@link #getTopCoordinatesLeft()} and {@link #getTopCoordinatesRight()}.
	 * 
	 * @return An ArrayList of the texture coordinates.
	 */
	protected abstract ArrayList<Double> getTopTextureCoordinates();
	
	/**
	 * Returns a list of the coordinates on the left side of the bottom part
	 * of the conveyor belt.
	 * 
	 * @return An ArrayList of the coordinates.
	 */
	protected abstract ArrayList<Vector3f> getBottomCoordinatesLeft();
	
	/**
	 * Returns a list of the coordinates on the right side of the bottom part
	 * of the conveyor belt.
	 * 
	 * @return An ArrayList of the coordinates.
	 */
	protected abstract ArrayList<Vector3f> getBottomCoordinatesRight();
	
	/**
	 * Returns a list of the texture coordinates for the bottom part of the conveyor belt.
	 * These should correspond to the coordinates given by the methods
	 * {@link #getBottomCoordinatesLeft()} and {@link #getBottomCoordinatesRight()}.
	 * 
	 * @return An ArrayList of the texture coordinates.
	 */
	protected abstract ArrayList<Double> getBottomTextureCoordinates();
	
	/**
	 * Increases the position of the luggage.
	 * 
	 * This method may do two things:
	 * 
	 *  * update the position of the luggage;
	 *  * "drop" the luggage (set {@link Luggage#supportingBlock} to <code>null</code>).
	 * 
	 * It may be assumed that the luggage is indeed on {@link Luggage#supportingBlock}.
	 * 
	 * This method is used for the simulation.
	 * 
	 * @param l The luggage item, which also contains data like the current
	 * position on this conveyor belt.
	 * @param step The step size in seconds.
	 */
	public abstract void furtherPosition(Luggage l, double step);
	
	/**
	 * Checks whether this conveyor belt is able to take the given luggage.
	 * 
	 * We say that a conveyor belt can take luggage if the luggage is in such a position
	 * that it "seems to be approximately" on the conveyor belt. In that situation,
	 * the luggage can be moved exactly to the conveyor belt for the next simulation
	 * step (with the method {@link #takeLuggage(Luggage)}).
	 * 
	 * @param l The luggage to check.
	 * @return Whether the luggage can be taken (<code>true</code>) or not
	 * (<code>false</code>).
	 */
	public abstract boolean canTakeLuggage(Luggage l);
	
	/**
	 * Takes the luggage. That is, puts the luggage exactly on the conveyor belt.
	 * This should only be done when {@link #canTakeLuggage(Luggage)} is <code>true</code>,
	 * else the luggage could move a lot between two simulation steps, causing a
	 * "jump".
	 * 
	 * @param l The luggage to take.
	 */
	public abstract void takeLuggage(Luggage l);
	
	@Override
	public void draw(State s) {
		
		glPushMatrix();
		glTranslated(x, y, z / 4.0);
		glRotated(-orientation.angle, 0, 0, 1);
		
		// TODO draw the axes
		
		glColor3d(1, 1, 1);
		glEnable(GL_TEXTURE_2D);
		s.textures.conveyor.bind();
		
		glBegin(GL_QUAD_STRIP);
		{
			glNormal3d(0, 0, 1); // TODO calculate proper normals
			
			ArrayList<Vector3f> lefts = getTopCoordinatesLeft();
			ArrayList<Vector3f> rights = getTopCoordinatesRight();
			ArrayList<Double> texs = getTopTextureCoordinates();
			
			assert lefts.size() == rights.size() && lefts.size() == texs.size();
			
			for (int i = 0; i < lefts.size(); i++) {
				glTexCoord2d(texs.get(i) - s.time, 0);
				glVertex3f(lefts.get(i));
				
				glTexCoord2d(texs.get(i) - s.time, 1);
				glVertex3f(rights.get(i));
			}
		}
		glEnd();
		
		glBegin(GL_QUAD_STRIP);
		{
			glNormal3d(0, 0, 1); // TODO calculate proper normals
			
			ArrayList<Vector3f> lefts = getBottomCoordinatesLeft();
			ArrayList<Vector3f> rights = getBottomCoordinatesRight();
			ArrayList<Double> texs = getBottomTextureCoordinates();
			
			assert lefts.size() == rights.size() && lefts.size() == texs.size();
			
			for (int i = 0; i < lefts.size(); i++) {
				glTexCoord2d(texs.get(i) - s.time, 0);
				glVertex3f(lefts.get(i));
				
				glTexCoord2d(texs.get(i) - s.time, 1);
				glVertex3f(rights.get(i));
			}
		}
		glEnd();
		
		glDisable(GL_TEXTURE_2D);
		
		glPopMatrix();
	}
}
