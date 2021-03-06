package accg.objects.blocks;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import accg.objects.Block;
import accg.objects.Orientation;

/**
 * A DescendingConveyorBlock is much like an {@link AscendingConveyorBlock}, only it
 * brings luggage a level lower instead of a level higher.
 */
public class DescendingConveyorBlock extends ConveyorBlock {

	/**
	 * Series of points that define the hull around this block.
	 */
	public static final Vector3f[] HULL_POINTS = new Vector3f[AscendingConveyorBlock.HULL_POINTS.length];
	/**
	 * Series of points that define the roof hull around this block.
	 */
	public static final Vector3f[] HULL_ROOF_POINTS = new Vector3f[AscendingConveyorBlock.HULL_ROOF_POINTS.length];
	static {
		for (int i = 0; i < HULL_POINTS.length; i++) {
			HULL_POINTS[i] = new Vector3f(-AscendingConveyorBlock.HULL_POINTS[i].x,
					-AscendingConveyorBlock.HULL_POINTS[i].y, AscendingConveyorBlock.HULL_POINTS[i].z);
		}
		for (int i = 0; i < HULL_ROOF_POINTS.length; i++) {
			HULL_ROOF_POINTS[i] = new Vector3f(-AscendingConveyorBlock.HULL_ROOF_POINTS[i].x,
					-AscendingConveyorBlock.HULL_ROOF_POINTS[i].y, AscendingConveyorBlock.HULL_ROOF_POINTS[i].z);
		}
	}
	
	/**
	 * Construct a new conveyor block at given position and with given orientation.
	 * The constructed block will be deletable.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @param orientation The orientation.
	 */
	public DescendingConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation, ConveyorBlockType.DESCENDING);
	}
	
	/**
	 * Construct a new conveyor block at given position and with given orientation.
	 * The constructed block will be deletable depending on the parameter.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @param orientation The orientation.
	 * @param deletable If the block can be deleted or not.
	 */
	public DescendingConveyorBlock(int x, int y, int z, Orientation orientation, boolean deletable) {
		super(x, y, z, orientation, deletable, ConveyorBlockType.DESCENDING);
	}

	@Override
	public int getHeight() {
		return super.getHeight() + 1;
	}
	
	@Override
	protected void drawArrowShape() {
		glNormal3f(0, 0, 1);
		glVertex3f(-0.12f, -0.3f, 1.22f);
		glVertex3f(-0.12f, 0.1f, 1.08f);
		glVertex3f(0.12f, 0.1f, 1.08f);
		glVertex3f(0.12f, -0.3f, 1.22f);
		
		glVertex3f(0, 0.1f, 1.08f);
		glVertex3f(0.25f, 0.1f, 1.08f);
		glVertex3f(0, 0.3f, 1.01f);
		glVertex3f(-0.25f, 0.1f, 1.08f);
	}
	
	@Override
	public Block clone() {
		return new DescendingConveyorBlock(x, y, z, orientation);
	}
	
	@Override
	public String getBlockID() {
		return "cd";
	}
	
	@Override
	public Vector3f[] getHullPoints() {
		return HULL_POINTS;
	}
	
	@Override
	public Vector3f[] getHullRoofPoints() {
		return HULL_ROOF_POINTS;
	}
	
	@Override
	public ArrayList<Vector3f> getTopCoordinatesLeft(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		if (neighbor1 == null) {
			addBendYZ(lefts, Math.PI, Math.PI / 2 - Math.atan2(1, 3), -0.375f,
					-0.375f, 0.5f, 0.125);
		} else {
			lefts.add(new Vector3f(-0.375f, -0.5f, 0.625f));
			addBendYZ(lefts, Math.PI / 2, Math.PI / 2 - Math.atan2(1, 3),
					-0.375f, -0.375f, 0.5f, 0.125);
		}
		if (neighbor2 == null) {
			addBendYZ(lefts, Math.PI / 2 - Math.atan2(1, 3), 0, -0.375f, 0.375f,
					0.25f, 0.125);
		} else {
			addBendYZ(lefts, -Math.PI / 2 - Math.atan2(1, 3), -Math.PI / 2,
					-0.375f, 0.375f, 0.5f, 0.125);
			lefts.add(new Vector3f(-0.375f, 0.5f, 0.375f));
		}
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getTopCoordinatesRight(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Vector3f> rights = new ArrayList<>();
		if (neighbor1 == null) {
			addBendYZ(rights, Math.PI, Math.PI / 2 - Math.atan2(1, 3), 0.375f,
					-0.375f, 0.5f, 0.125);
		} else {
			rights.add(new Vector3f(0.375f, -0.5f, 0.625f));
			addBendYZ(rights, Math.PI / 2, Math.PI / 2 - Math.atan2(1, 3),
					0.375f, -0.375f, 0.5f, 0.125);
		}
		if (neighbor2 == null) {
			addBendYZ(rights, Math.PI / 2 - Math.atan2(1, 3), 0, 0.375f, 0.375f,
					0.25f, 0.125);
		} else {
			addBendYZ(rights, -Math.PI / 2 - Math.atan2(1, 3), -Math.PI / 2,
					0.375f, 0.375f, 0.5f, 0.125);
			rights.add(new Vector3f(0.375f, 0.5f, 0.375f));
		}
		return rights;
	}

	@Override
	public ArrayList<Double> getTopTextureCoordinates(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Double> texs = new ArrayList<>();
		double texCoord;
		if (neighbor1 == null) {
			texCoord = addBendTextureCoordinates(texs, Math.PI,
					Math.PI / 2 - Math.atan2(1, 3), 0.125, 0.0);
		} else {
			texs.add(0.0);
			texCoord = 1;
			texCoord = addBendTextureCoordinates(texs,
					Math.PI / 2, Math.PI / 2 - Math.atan2(1, 3), 0.125, texCoord);
		}
		if (neighbor2 == null) {
			addBendTextureCoordinates(texs, Math.PI / 2 - Math.atan2(1, 3), 0,
					0.125, texCoord + 6.0);
		} else {
			addBendTextureCoordinates(texs, -Math.PI / 2 - Math.atan2(1, 3),
					-Math.PI / 2, 0.125, texCoord + 6.0);
			texs.add(9.0);
		}
		return texs;
	}

	@Override
	public ArrayList<Vector3f> getBottomCoordinatesLeft(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		if (neighbor2 == null) {
			addBendYZ(lefts, 0, -Math.PI / 2 - Math.atan2(1, 3), -0.375f, 0.375f,
					0.25f, 0.125);
		} else {
			lefts.add(new Vector3f(-0.375f, 0.5f, 0.125f));
			addBendYZ(lefts, -Math.PI / 2, -Math.PI / 2 - Math.atan2(1, 3), -0.375f, 0.375f,
					0.25f, 0.125);
		}
		if (neighbor1 == null) {
			addBendYZ(lefts, Math.PI * 3 / 2 - Math.atan2(1, 3), Math.PI, -0.375f,
					-0.375f, 0.5f, 0.125);
		} else {
			addBendYZ(lefts, -Math.PI * 3 / 2 - Math.atan2(1, 3), -Math.PI * 3 / 2, -0.375f,
					-0.375f, 0.25f, 0.125);
			lefts.add(new Vector3f(-0.375f, -0.5f, 0.375f));
		}
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getBottomCoordinatesRight(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Vector3f> rights = new ArrayList<>();
		if (neighbor2 == null) {
			addBendYZ(rights, 0, -Math.PI / 2 - Math.atan2(1, 3), 0.375f, 0.375f,
					0.25f, 0.125);
		} else {
			rights.add(new Vector3f(0.375f, 0.5f, 0.125f));
			addBendYZ(rights, -Math.PI / 2, -Math.PI / 2 - Math.atan2(1, 3), 0.375f, 0.375f,
					0.25f, 0.125);
		}
		if (neighbor1 == null) {
			addBendYZ(rights, Math.PI * 3 / 2 - Math.atan2(1, 3), Math.PI, 0.375f,
					-0.375f, 0.5f, 0.125);
		} else {
			addBendYZ(rights, -Math.PI * 3 / 2 - Math.atan2(1, 3), -Math.PI * 3 / 2, 0.375f,
					-0.375f, 0.25f, 0.125);
			rights.add(new Vector3f(0.375f, -0.5f, 0.375f));
		}
		return rights;
	}

	@Override
	public ArrayList<Double> getBottomTextureCoordinates(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Double> texs = new ArrayList<>();
		double texCoord;
		if (neighbor2 == null) {
			texCoord = addBendTextureCoordinates(texs,
					0, -Math.PI / 2 - Math.atan2(1, 3), 0.125, 0.0);
		} else {
			texs.add(0.0);
			texCoord = 1;
			texCoord = addBendTextureCoordinates(texs,
					-Math.PI / 2, -Math.PI / 2 - Math.atan2(1, 3), 0.125, 0.0);
		}
		if (neighbor1 == null) {
			addBendTextureCoordinates(texs, Math.PI * 3 / 2 - Math.atan2(1, 3),
					Math.PI, 0.125, texCoord + 6.0);
		} else {
			addBendTextureCoordinates(texs, -Math.PI * 3 / 2 - Math.atan2(1, 3),
					-Math.PI * 3 / 2, 0.125, texCoord + 6.0);
			texs.add(9.0);
		}
		return texs;
	}

	@Override
	public Vector3f getAngularVelocity() {
		return new Vector3f(0, 0, 0);
	}
	
	@Override
	public Vector3f getLinearVelocity() {
		switch (getOrientation()) {
		case DOWN :  return new Vector3f(0, -1, 0);
		case LEFT :  return new Vector3f(-1, 0, 0);
		case RIGHT : return new Vector3f( 1, 0, 0);
		default :    return new Vector3f(0,  1, 0);
		}
	}
}
