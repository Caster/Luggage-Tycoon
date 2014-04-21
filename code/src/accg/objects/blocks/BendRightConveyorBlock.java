package accg.objects.blocks;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import accg.objects.Block;
import accg.objects.Orientation;
import accg.utils.Utils;

/**
 * A ConveyorBlock that makes a 90 degree bend to the right.
 */
public class BendRightConveyorBlock extends ConveyorBlock {

	/**
	 * Series of points that define the hull around this block.
	 */
	public static final Vector3f[] HULL_POINTS = new Vector3f[BendLeftConveyorBlock.HULL_POINTS.length];
	/**
	 * Series of points that define the roof hull around this block.
	 */
	public static final Vector3f[] HULL_ROOF_POINTS = new Vector3f[BendLeftConveyorBlock.HULL_ROOF_POINTS.length];
	static {
		for (int i = 0; i < HULL_POINTS.length; i += 4) {
			HULL_POINTS[i]     = new Vector3f(-BendLeftConveyorBlock.HULL_POINTS[i].x,
					BendLeftConveyorBlock.HULL_POINTS[i].y, BendLeftConveyorBlock.HULL_POINTS[i].z);
			HULL_POINTS[i + 1] = new Vector3f(-BendLeftConveyorBlock.HULL_POINTS[i + 3].x,
					BendLeftConveyorBlock.HULL_POINTS[i + 3].y, BendLeftConveyorBlock.HULL_POINTS[i + 3].z);
			HULL_POINTS[i + 2] = new Vector3f(-BendLeftConveyorBlock.HULL_POINTS[i + 2].x,
					BendLeftConveyorBlock.HULL_POINTS[i + 2].y, BendLeftConveyorBlock.HULL_POINTS[i + 2].z);
			HULL_POINTS[i + 3] = new Vector3f(-BendLeftConveyorBlock.HULL_POINTS[i + 1].x,
					BendLeftConveyorBlock.HULL_POINTS[i + 1].y, BendLeftConveyorBlock.HULL_POINTS[i + 1].z);
		}
		for (int i = 0; i < HULL_ROOF_POINTS.length; i += 4) {
			HULL_ROOF_POINTS[i]     = new Vector3f(-BendLeftConveyorBlock.HULL_ROOF_POINTS[i].x,
					BendLeftConveyorBlock.HULL_ROOF_POINTS[i].y, BendLeftConveyorBlock.HULL_ROOF_POINTS[i].z);
			HULL_ROOF_POINTS[i + 1] = new Vector3f(-BendLeftConveyorBlock.HULL_ROOF_POINTS[i + 3].x,
					BendLeftConveyorBlock.HULL_ROOF_POINTS[i + 3].y, BendLeftConveyorBlock.HULL_ROOF_POINTS[i + 3].z);
			HULL_ROOF_POINTS[i + 2] = new Vector3f(-BendLeftConveyorBlock.HULL_ROOF_POINTS[i + 2].x,
					BendLeftConveyorBlock.HULL_ROOF_POINTS[i + 2].y, BendLeftConveyorBlock.HULL_ROOF_POINTS[i + 2].z);
			HULL_ROOF_POINTS[i + 3] = new Vector3f(-BendLeftConveyorBlock.HULL_ROOF_POINTS[i + 1].x,
					BendLeftConveyorBlock.HULL_ROOF_POINTS[i + 1].y, BendLeftConveyorBlock.HULL_ROOF_POINTS[i + 1].z);
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
	public BendRightConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation, ConveyorBlockType.BEND_RIGHT);
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
	public BendRightConveyorBlock(int x, int y, int z, Orientation orientation, boolean deletable) {
		super(x, y, z, orientation, deletable, ConveyorBlockType.BEND_RIGHT);
	}
	
	@Override
	protected void drawArrowShape() {
		glNormal3f(0, 0, 1);
		
		double step = Math.PI / 32;
		for (int i = 0; i < 16; i++) {
			glVertex3f(0.3f - 0.18f * (float) Math.cos(step * i), -0.3f + 0.18f * (float) Math.sin(step * i), 1);
			glVertex3f(0.3f - 0.42f * (float) Math.cos(step * i), -0.3f + 0.42f * (float) Math.sin(step * i), 1);
			glVertex3f(0.3f - 0.42f * (float) Math.cos(step * (i + 1)), -0.3f + 0.42f * (float) Math.sin(step * (i + 1)), 1);
			glVertex3f(0.3f - 0.18f * (float) Math.cos(step * (i + 1)), -0.3f + 0.18f * (float) Math.sin(step * (i + 1)), 1);
		}
		
		glVertex3f(0.3f, 0, 1);
		glVertex3f(0.3f, 0.25f, 1);
		glVertex3f(0.5f, 0, 1);
		glVertex3f(0.3f, -0.25f, 1);
	}
	
	@Override
	public Block clone() {
		return new BendRightConveyorBlock(x, y, z, orientation);
	}
	
	@Override
	public String getBlockID() {
		return "cbr";
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
		if (neighbor1 != null && neighbor2 != null) {
			addBendXY(lefts, Math.PI, Math.PI / 2, 0.375f, 0.5f, -0.5f, 0.875);
		} else {
			if (neighbor1 == null) {
				addBendYZ(lefts, Math.PI, Math.PI / 2, -0.375f, -0.375f, 0.25f, 0.125);
			} else {
				lefts.add(new Vector3f(-0.375f, -0.5f, 0.375f));
			}
			
			addBendXY(lefts, Math.PI, Math.PI / 2, 0.375f, 0.375f, -0.375f, 0.75);
			
			if (neighbor2 == null) {
				ArrayList<Vector3f> leftsAppend = new ArrayList<>();
				addBendYZ(leftsAppend, Math.PI / 2, 0, -0.375f, 0.375f, 0.25f, 0.125);
				Utils.rotatePoints(Orientation.RIGHT, leftsAppend);
				lefts.addAll(leftsAppend);
			} else {
				lefts.add(new Vector3f(0.5f, 0.375f, 0.375f));
			}
		}
		
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getTopCoordinatesRight(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Vector3f> rights = new ArrayList<>();
		if (neighbor1 != null && neighbor2 != null) {
			addBendXY(rights, Math.PI, Math.PI / 2, 0.375f, 0.5f, -0.5f, 0.125);
		} else {
			if (neighbor1 == null) {
				addBendYZ(rights, Math.PI, Math.PI / 2, 0.375f, -0.375f, 0.25f, 0.125);
			} else {
				rights.add(new Vector3f(0.375f, -0.5f, 0.375f));
			}
			
			addBendXY(rights, Math.PI, Math.PI / 2, 0.375f, 0.375f, -0.375f, 0);
			
			if (neighbor2 == null) {
				ArrayList<Vector3f> rightsAppend = new ArrayList<>();
				addBendYZ(rightsAppend, Math.PI / 2, 0, 0.375f, 0.375f, 0.25f, 0.125);
				Utils.rotatePoints(Orientation.RIGHT, rightsAppend);
				rights.addAll(rightsAppend);
			} else {
				rights.add(new Vector3f(0.5f, -0.375f, 0.375f));
			}
		}
		
		return rights;
	}

	@Override
	public ArrayList<Double> getTopTextureCoordinates(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Double> texs = new ArrayList<>();
		double texCoord = 0;
		if (neighbor1 == null) {
			texCoord = addBendTextureCoordinates(texs, Math.PI, Math.PI / 2,
					0.125, 0.0);
		} else if (neighbor2 == null) {
			texs.add(0.0);
			texCoord = 1;
		}
		
		texCoord = addBendTextureCoordinates(texs, Math.PI, Math.PI / 2,
				(neighbor1 != null && neighbor2 != null ? 0.75 : 0.625), texCoord);
		
		if (neighbor2 == null) {
			addBendTextureCoordinates(texs, Math.PI / 2, 0, 0.125, texCoord);
		} else if (neighbor1 == null) {
			texs.add(texCoord + 1);
		}
		return texs;
	}

	@Override
	public ArrayList<Vector3f> getBottomCoordinatesLeft(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		if (neighbor1 != null && neighbor2 != null) {
			addBendXY(lefts, Math.PI / 2, Math.PI, 0.125f, 0.5f, -0.5f, 0.875);
		} else {
			if (neighbor2 == null) {
				addBendYZ(lefts, Math.PI * 2, Math.PI * 3 / 2, -0.375f, 0.375f, 0.25f, 0.125);
				Utils.rotatePoints(Orientation.RIGHT, lefts);
			} else {
				lefts.add(new Vector3f(0.5f, 0.375f, 0.125f));
			}
			
			addBendXY(lefts, Math.PI / 2, Math.PI, 0.125f, 0.375f, -0.375f, 0.75);
			
			if (neighbor1 == null) {
				addBendYZ(lefts, Math.PI * 3 / 2, Math.PI, -0.375f, -0.375f, 0.25f, 0.125);
			} else {
				lefts.add(new Vector3f(-0.375f, -0.5f, 0.125f));
			}
		}
		
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getBottomCoordinatesRight(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Vector3f> rights = new ArrayList<>();
		if (neighbor1 != null && neighbor2 != null) {
			addBendXY(rights, Math.PI / 2, Math.PI, 0.125f, 0.5f, -0.5f, 0.125);
		} else {
			if (neighbor2 == null) {
				addBendYZ(rights, Math.PI * 2, Math.PI * 3 / 2, 0.375f, 0.375f, 0.25f, 0.125);
				Utils.rotatePoints(Orientation.RIGHT, rights);
			} else {
				rights.add(new Vector3f(0.5f, -0.375f, 0.125f));
			}
			
			addBendXY(rights, Math.PI / 2, Math.PI, 0.125f, 0.375f, -0.375f, 0);
			
			if (neighbor1 == null) {
				addBendYZ(rights, Math.PI * 3 / 2, Math.PI, 0.375f, -0.375f, 0.25f, 0.125);
			} else {
				rights.add(new Vector3f(0.375f, -0.5f, 0.125f));
			}
		}
		
		return rights;
	}

	@Override
	public ArrayList<Double> getBottomTextureCoordinates(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Double> texs = new ArrayList<>();
		double texCoord = 0;
		if (neighbor2 == null) {
			texCoord = addBendTextureCoordinates(texs, Math.PI * 2,
					Math.PI * 3 / 2, 0.125, 0.0);
		} else if (neighbor1 == null) {
			texs.add(0.0);
			texCoord = 1;
		}
		
		texCoord = addBendTextureCoordinates(texs, Math.PI / 2, Math.PI,
				(neighbor1 != null && neighbor2 != null ? 0.875 : 0.75), texCoord);
		
		if (neighbor1 == null || neighbor2 == null) {
			addBendTextureCoordinates(texs, Math.PI * 3 / 2, Math.PI, 0.125, texCoord);
		}
		return texs;
	}

	@Override
	public Vector3f getAngularVelocity() {
		return new Vector3f(0, 0, (float) -Math.PI);
	}
	
	@Override
	public Vector3f getLinearVelocity() {
		switch (getOrientation()) {
		case DOWN :  return new Vector3f(-1, -1, 0);
		case LEFT :  return new Vector3f(-1,  1, 0);
		case RIGHT : return new Vector3f( 1, -1, 0);
		default :    return new Vector3f( 1,  1, 0);
		}
	}
}
