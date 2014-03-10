package accg.objects.blocks;

import static accg.utils.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import org.lwjgl.util.glu.Cylinder;

import accg.State;
import accg.objects.Block;
import accg.utils.Utils;

/**
 * Generic super class of all conveyor blocks. This class is overridden by the
 * block classes.
 * 
 * A {@link ConveyorBlock} manages the drawing of conveyor belts and the simulation,
 * based on abstract methods that indicate the exact shape of the conveyor belt.
 * 
 * ConveyorBlocks use the following coordinate system: they are centered around
 * the origin (0, 0), and the conveyor belt moves towards the positive y direction.
 */
public abstract class ConveyorBlock extends Block {
	
	/**
	 * Type of this conveyor block.
	 */
	public enum ConveyorBlockType {
		ASCENDING, BEND_LEFT, BEND_RIGHT, DESCENDING, STRAIGHT
	};
	
	/**
	 * Creates a new ConveyorBlock on the specified position.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @param orientation The orientation of this block.
	 */
	public ConveyorBlock(int x, int y, int z, Orientation orientation,
			ConveyorBlockType type) {
		super(x, y, z, orientation);
		this.type = type;
	}
	
	/**
	 * Return the type of conveyor block. This is useful when determining its
	 * shape for example and may spare an instanceof operation.
	 * 
	 * @return The type of conveyor block.
	 */
	public ConveyorBlockType getConveyorBlockType() {
		return type;
	}
	
	/**
	 * Return the angular velocity that should be applied to objects in contact
	 * with this conveyor belt. Used in the simulation.
	 * 
	 * @return The angular velocity of this ConveyorBlock.
	 */
	public abstract Vector3f getAngularVelocity();
	
	/**
	 * Return the linear velocity that should be applied to objects in contact
	 * with this conveyor belt. Used in the simulation.
	 * 
	 * @return The linear velocity of this ConveyorBlock.
	 */
	public abstract Vector3f getLinearVelocity();
	
	/**
	 * Returns a list of the coordinates on the left side of the top part
	 * of the conveyor belt.
	 * 
	 * @return An ArrayList of the coordinates.
	 */
	public abstract ArrayList<Vector3f> getTopCoordinatesLeft();
	
	/**
	 * Returns a list of the coordinates on the right side of the top part
	 * of the conveyor belt.
	 * 
	 * @return An ArrayList of the coordinates.
	 */
	public abstract ArrayList<Vector3f> getTopCoordinatesRight();
	
	/**
	 * Returns a list of the texture coordinates for the top part of the conveyor belt.
	 * These should correspond to the coordinates given by the methods
	 * {@link #getTopCoordinatesLeft()} and {@link #getTopCoordinatesRight()}.
	 * 
	 * @return An ArrayList of the texture coordinates.
	 */
	public abstract ArrayList<Double> getTopTextureCoordinates();
	
	/**
	 * Returns a list of the coordinates on the left side of the bottom part
	 * of the conveyor belt.
	 * 
	 * @return An ArrayList of the coordinates.
	 */
	public abstract ArrayList<Vector3f> getBottomCoordinatesLeft();
	
	/**
	 * Returns a list of the coordinates on the right side of the bottom part
	 * of the conveyor belt.
	 * 
	 * @return An ArrayList of the coordinates.
	 */
	public abstract ArrayList<Vector3f> getBottomCoordinatesRight();
	
	/**
	 * Returns a list of the texture coordinates for the bottom part of the conveyor belt.
	 * These should correspond to the coordinates given by the methods
	 * {@link #getBottomCoordinatesLeft()} and {@link #getBottomCoordinatesRight()}.
	 * 
	 * @return An ArrayList of the texture coordinates.
	 */
	public abstract ArrayList<Double> getBottomTextureCoordinates();
	
	@Override
	public void draw(State s) {
		
		drawScaffolding(s);

		glPushMatrix();
		glTranslated(x, y, z / 4.0);
		glRotated(-orientation.angle, 0, 0, 1);
		
		// TODO draw the axes
		
		glEnable(GL_TEXTURE_2D);
		s.textures.conveyor.bind();
		
		glBegin(GL_QUAD_STRIP);
		{			
			ArrayList<Vector3f> lefts = getTopCoordinatesLeft();
			ArrayList<Vector3f> rights = getTopCoordinatesRight();
			ArrayList<Double> texs = getTopTextureCoordinates();
			
			assert lefts.size() == rights.size() && lefts.size() == texs.size();
			
			putPoints(lefts, rights, texs, s);
		}
		glEnd();
		
		glBegin(GL_QUAD_STRIP);
		{
			ArrayList<Vector3f> lefts = getBottomCoordinatesLeft();
			ArrayList<Vector3f> rights = getBottomCoordinatesRight();
			ArrayList<Double> texs = getBottomTextureCoordinates();
			
			assert lefts.size() == rights.size() && lefts.size() == texs.size();
			
			putPoints(lefts, rights, texs, s);
		}
		glEnd();
		
		glDisable(GL_TEXTURE_2D);
		
		glPopMatrix();
	}
	
	@Override
	public void setPosition(Vector3f position) {
		this.x = (int) position.x;
		this.y = (int) position.y;
		this.z = (int) position.z;
	}
	
	/**
	 * Draws the scaffolding below the block.
	 * @param s The state object.
	 */
	private void drawScaffolding(State s) {
		
		drawPole(-0.5, -0.5);
		drawPole(-0.5, 0.5);
		drawPole(0.5, -0.5);
		drawPole(0.5, 0.5);
	}
	
	private void drawPole(double dx, double dy) {

		glPushMatrix();
		glTranslated(x + dx, y + dy, 0);

		Cylinder c = new Cylinder();
		c.draw(0.05f, 0.02f, 0.25f, 16, 1);
		glPushMatrix();
		glTranslated(0, 0, 0.25f);
		c.draw(0.02f, 0.02f, z / 4.0f, 16, 1);
		glPopMatrix();
		
		glPopMatrix();
	}

	/**
	 * Add coordinates to the given list that form a bend in the XY plane.
	 * 
	 * @param list The list to which points should be added.
	 * @param radStart Start in radians of bend.
	 * @param radEnd End in radians of bend.
	 * @paramzxCoord Z-coordinate of all added points.
	 * @param xOffset Offset for all generated X-coordinates.
	 * @param yOffset Offset for all generated Y-coordinates.
	 * @param rScale Scale factor for the radius of the bend.
	 */
	protected void addBendXY(ArrayList<Vector3f> list, double radStart,
			double radEnd, float zCoord, float xOffset, float yOffset, double rScale) {
		for (double rad = radStart; (radStart < radEnd ? rad < radEnd : rad > radEnd);
				rad += Math.signum(radEnd - radStart) * RAD_STEP) {
			list.add(new Vector3f((float) (rScale * Math.cos(rad)) + xOffset,
					(float) (rScale * Math.sin(rad)) + yOffset, zCoord));
		}
		list.add(new Vector3f((float) (rScale * Math.cos(radEnd)) + xOffset,
				(float) (rScale * Math.sin(radEnd)) + yOffset, zCoord));
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
	 * {@link #addBendYZ(ArrayList, double, double, float, float, float, double)} or
	 * {@link #addBendXY(ArrayList, double, double, float, float, float, double)}.
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
	protected double addBendTextureCoordinates(ArrayList<Double> list,
			double radStart, double radEnd, double rScale, double coordStart) {
		double coord = coordStart;
		// we map 6 texture coordinates to a full circle with a radius of 0.125,
		// so we can calculate the part we map to the bend we are going to texture now
		double coordSum = Math.abs(radStart - radEnd) / (2 * Math.PI) * (rScale / 0.125) * 6;
		int numSteps = (int) Math.ceil(Math.abs(radStart - radEnd) / RAD_STEP);
		for (double rad = radStart; (radStart < radEnd ? rad < radEnd : rad > radEnd);
				rad += Math.signum(radEnd - radStart) * RAD_STEP) {
			list.add(Double.valueOf(coord));
			coord += coordSum / numSteps;
		}
		list.add(Double.valueOf(coord));
		return coord;
	}
	
	/**
	 * Put points of one side of a conveyor belt in OpenGL direct mode.
	 * All three given lists should have the same length.
	 * 
	 * @param lefts Left side of part to add.
	 * @param rights Right side of part to add.
	 * @param texs Texture coordinates.
	 * @param s State, time is read from this to shift texture.
	 */
	private void putPoints(ArrayList<Vector3f> lefts, ArrayList<Vector3f> rights,
			ArrayList<Double> texs, State s) {
		for (int i = 0; i < lefts.size(); i++) {
			Vector3f normal1 = new Vector3f();
			if (i < lefts.size() - 1) {
				Vector3f normal1h = new Vector3f();
				Vector3f normal1v = new Vector3f();
				if (!lefts.get(i).epsilonEquals(lefts.get(i + 1), Utils.EPSILON)) {
					normal1h.sub(lefts.get(i), lefts.get(i + 1));
					normal1v.sub(lefts.get(i), rights.get(i));
				} else {
					normal1h.sub(rights.get(i + 1), rights.get(i));
					normal1v.sub(rights.get(i), lefts.get(i));
				}
				normal1.cross(normal1v, normal1h);
			}
			
			Vector3f normal2 = new Vector3f();
			if (i > 1) {
				Vector3f normal2h = new Vector3f();
				Vector3f normal2v = new Vector3f();
				if (!lefts.get(i).epsilonEquals(lefts.get(i - 1), Utils.EPSILON)) {
					normal2h.sub(lefts.get(i), lefts.get(i - 1));
					normal2v.sub(lefts.get(i), rights.get(i));
				} else {
					normal2h.sub(rights.get(i - 1), rights.get(i));
					normal2v.sub(rights.get(i), lefts.get(i));
				}
				normal2.cross(normal2h, normal2v);
			}
			Vector3f average = new Vector3f();
			average.add(normal1, normal2);
			average.normalize();
			glNormal3f(average);
			
			glTexCoord2d(texs.get(i) - 8 * s.time, 0);
			glVertex3f(lefts.get(i));
			
			glTexCoord2d(texs.get(i) - 8 * s.time, 1);
			glVertex3f(rights.get(i));
		}
	}
	
	/** Step that is used when generating bends. */
	protected static final double RAD_STEP = Math.PI / 20;
	/** Type of this block. */
	protected ConveyorBlockType type;
}
