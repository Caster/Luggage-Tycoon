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
	
	/**
	 * Add coordinates to the given list that form a bend in the YZ plane.
	 * 
	 * @param list The list to which points should be added.
	 * @param radStart Start in radians of bend.
	 * @param radEnd End in radians of bend.
	 * @param xCoord X-coordinate of all added points.
	 * @param yOffset Offset for all generated Y-coordinates.
	 * @param zOffset Offset for all generated Z-coordinates.
	 * @param rScale Scale factor for the radius of the bend.
	 */
	protected void addBendYZ(ArrayList<Vector3f> list, double radStart,
			double radEnd, float xCoord, float yOffset, float zOffset, double rScale) {
		for (double rad = radStart; (radStart < radEnd ? rad < radEnd : rad > radEnd);
				rad += Math.signum(radEnd - radStart) * RAD_STEP) {
			list.add(new Vector3f(xCoord, (float) (rScale * Math.cos(rad)) + yOffset,
					(float) (rScale * Math.sin(rad)) + zOffset));
		}
		list.add(new Vector3f(xCoord, (float) (rScale * Math.cos(radEnd)) + yOffset,
				(float) (rScale * Math.sin(radEnd)) + zOffset));
	}
	
	/**
	 * Add texture coordinates to the given list of texture coordinates that would
	 * match a bend created with
	 * {@link #addBendYZ(ArrayList, double, double, float, float, float, double)}.
	 * Note that it is used that a distance of 0.75 maps to a distance of 2 in
	 * texture coordinates.
	 * 
	 * @param list List of texture coordinates to add to.
	 * @param radStart Start in radians of bend.
	 * @param radEnd End in radians of bend.
	 * @param rScale Scale factor for the radius of the bend.
	 * @param coordStart Offset where coordinates should start.
	 * @return Last added texture coordinate.
	 */
	protected double addBendYZTextureCoordinates(ArrayList<Double> list,
			double radStart, double radEnd, double rScale, double coordStart) {
		double coord = coordStart;
		for (double rad = radStart; (radStart < radEnd ? rad < radEnd : rad > radEnd);
				rad += Math.signum(radEnd - radStart) * RAD_STEP) {
			list.add(Double.valueOf(coord));
			// calculate the distance to the next point
			Vector3f curr = new Vector3f(0, (float) (rScale * Math.cos(rad)),
					(float) (rScale * Math.sin(rad)));
			double nextRad = (radStart < radEnd ? Math.min(rad + RAD_STEP, radStart) :
				Math.max(rad - RAD_STEP, radEnd));
			Vector3f next = new Vector3f(0, (float) (rScale * Math.cos(nextRad)),
					(float) (rScale * Math.sin(nextRad)));
			Vector3f.sub(next, curr, next);
			coord += next.length() / 0.375;
		}
		list.add(Double.valueOf(coord));
		return coord;
	}
	
	/** Step that is used when generating bends. */
	protected static final double RAD_STEP = Math.PI / 20;
}
