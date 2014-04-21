package accg.objects.blocks;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import accg.objects.Block;
import accg.objects.Orientation;

/**
 * A flat conveyor block. Probably the most basic implementation of ConveyorBlock.
 */
public class FlatConveyorBlock extends ConveyorBlock {

	/**
	 * Series of points that define the hull around this block.
	 */
	public static final Vector3f[] HULL_POINTS = new Vector3f[] {
		// bottom quad
		new Vector3f(-0.5f, -0.5f, 0),
		new Vector3f(0.5f, -0.5f, 0),
		new Vector3f(0.5f, 0.5f, 0),
		new Vector3f(-0.5f, 0.5f, 0),

		// quad below conveyor, facing upwards
		new Vector3f(-0.375f, -0.5f, 0.125f),
		new Vector3f(0.375f, -0.5f, 0.125f),
		new Vector3f(0.375f, 0.5f, 0.125f),
		new Vector3f(-0.375f, 0.5f, 0.125f),
		
		// left top quad
		new Vector3f(-0.5f, -0.5f, 0.625f),
		new Vector3f(-0.375f, -0.5f, 0.625f),
		new Vector3f(-0.375f, 0.5f, 0.625f),
		new Vector3f(-0.5f, 0.5f, 0.625f),
		
		// right top quad
		new Vector3f(0.5f, -0.5f, 0.625f),
		new Vector3f(0.5f, 0.5f, 0.625f),
		new Vector3f(0.375f, 0.5f, 0.625f),
		new Vector3f(0.375f, -0.5f, 0.625f),
		
		// quad below conveyor at back
		new Vector3f(0.5f, -0.5f, 0),
		new Vector3f(0.5f, -0.5f, 0.125f),
		new Vector3f(-0.5f, -0.5f, 0.125f),
		new Vector3f(-0.5f, -0.5f, 0f),
		
		// quad below conveyor at front
		new Vector3f(0.5f, 0.5f, 0),
		new Vector3f(-0.5f, 0.5f, 0),
		new Vector3f(-0.5f, 0.5f, 0.125f),
		new Vector3f(0.5f, 0.5f, 0.125f),
		
		// left outer wall
		new Vector3f(-0.5f, -0.5f, 0),
		new Vector3f(-0.5f, -0.5f, 0.625f),
		new Vector3f(-0.5f, 0.5f, 0.625f),
		new Vector3f(-0.5f, 0.5f, 0),
		
		// left inner wall
		new Vector3f(-0.375f, -0.5f, 0.625f),
		new Vector3f(-0.375f, -0.5f, 0.125f),
		new Vector3f(-0.375f, 0.5f, 0.125f),
		new Vector3f(-0.375f, 0.5f, 0.625f),
		
		// left front side
		new Vector3f(-0.5f, 0.5f, 0.125f),
		new Vector3f(-0.5f, 0.5f, 0.625f),
		new Vector3f(-0.375f, 0.5f, 0.625f),
		new Vector3f(-0.375f, 0.5f, 0.125f),
		
		// left back side
		new Vector3f(-0.5f, -0.5f, 0.125f),
		new Vector3f(-0.375f, -0.5f, 0.125f),
		new Vector3f(-0.375f, -0.5f, 0.625f),
		new Vector3f(-0.5f, -0.5f, 0.625f),
		
		// right outer wall
		new Vector3f(0.5f, -0.5f, 0.625f),
		new Vector3f(0.5f, -0.5f, 0),
		new Vector3f(0.5f, 0.5f, 0),
		new Vector3f(0.5f, 0.5f, 0.625f),
		
		// right inner wall
		new Vector3f(0.375f, -0.5f, 0.125f),
		new Vector3f(0.375f, -0.5f, 0.625f),
		new Vector3f(0.375f, 0.5f, 0.625f),
		new Vector3f(0.375f, 0.5f, 0.125f),
		
		// right front side
		new Vector3f(0.5f, 0.5f, 0.625f),
		new Vector3f(0.5f, 0.5f, 0.125f),
		new Vector3f(0.375f, 0.5f, 0.125f),
		new Vector3f(0.375f, 0.5f, 0.625f),
		
		// right back side
		new Vector3f(0.5f, -0.5f, 0.625f),
		new Vector3f(0.375f, -0.5f, 0.625f),
		new Vector3f(0.375f, -0.5f, 0.125f),
		new Vector3f(0.5f, -0.5f, 0.125f)
	};
	/**
	 * Series of points that define the roof hull around this block.
	 */
	public static final Vector3f[] HULL_ROOF_POINTS = new Vector3f[] {
		// ceiling, facing upwards
		new Vector3f(-0.5f, -0.5f, 0.875f),
		new Vector3f(0.5f, -0.5f, 0.875f),
		new Vector3f(0.5f, 0.5f, 0.875f),
		new Vector3f(-0.5f, 0.5f, 0.875f),
		
		// ceiling, facing downwards
		new Vector3f(-0.5f, -0.5f, 0.75f),
		new Vector3f(-0.5f, 0.5f, 0.75f),
		new Vector3f(0.5f, 0.5f, 0.75f),
		new Vector3f(0.5f, -0.5f, 0.75f),
		
		// quad in ceiling at back
		new Vector3f(0.5f, -0.5f, 0.75f),
		new Vector3f(0.5f, -0.5f, 0.875f),
		new Vector3f(-0.5f, -0.5f, 0.875f),
		new Vector3f(-0.5f, -0.5f, 0.75f),
		
		// quad in ceiling at front
		new Vector3f(0.5f, 0.5f, 0.75f),
		new Vector3f(-0.5f, 0.5f, 0.75f),
		new Vector3f(-0.5f, 0.5f, 0.875f),
		new Vector3f(0.5f, 0.5f, 0.875f),
		
		// left outer wall ceiling
		new Vector3f(-0.5f, -0.5f, 0.75f),
		new Vector3f(-0.5f, -0.5f, 0.875f),
		new Vector3f(-0.5f, 0.5f, 0.875f),
		new Vector3f(-0.5f, 0.5f, 0.75f),
		
		// left outer wall back
		new Vector3f(-0.5f, -0.5f, 0.75f),
		new Vector3f(-0.5f, -0.375f, 0.75f),
		new Vector3f(-0.5f, -0.375f, 0.625f),
		new Vector3f(-0.5f, -0.5f, 0.625f),
		
		// left outer wall front
		new Vector3f(-0.5f, 0.375f, 0.75f),
		new Vector3f(-0.5f, 0.5f, 0.75f),
		new Vector3f(-0.5f, 0.5f, 0.625f),
		new Vector3f(-0.5f, 0.375f, 0.625f),
		
		// left inner wall back
		new Vector3f(-0.375f, -0.5f, 0.75f),
		new Vector3f(-0.375f, -0.5f, 0.625f),
		new Vector3f(-0.375f, -0.375f, 0.625f),
		new Vector3f(-0.375f, -0.375f, 0.75f),
		
		// left inner wall front
		new Vector3f(-0.375f, 0.375f, 0.75f),
		new Vector3f(-0.375f, 0.375f, 0.625f),
		new Vector3f(-0.375f, 0.5f, 0.625f),
		new Vector3f(-0.375f, 0.5f, 0.75f),
		
		// left inner/outer wall back/back
		new Vector3f(-0.375f, -0.5f, 0.625f),
		new Vector3f(-0.375f, -0.5f, 0.75f),
		new Vector3f(-0.5f, -0.5f, 0.75f),
		new Vector3f(-0.5f, -0.5f, 0.625f),
		
		// left inner/outer wall back/front
		new Vector3f(-0.375f, -0.375f, 0.625f),
		new Vector3f(-0.5f, -0.375f, 0.625f),
		new Vector3f(-0.5f, -0.375f, 0.75f),
		new Vector3f(-0.375f, -0.375f, 0.75f),
		
		// left inner/outer wall front/back
		new Vector3f(-0.375f, 0.375f, 0.625f),
		new Vector3f(-0.375f, 0.375f, 0.75f),
		new Vector3f(-0.5f, 0.375f, 0.75f),
		new Vector3f(-0.5f, 0.375f, 0.625f),
		
		// left inner/outer wall front/front
		new Vector3f(-0.375f, 0.5f, 0.625f),
		new Vector3f(-0.5f, 0.5f, 0.625f),
		new Vector3f(-0.5f, 0.5f, 0.75f),
		new Vector3f(-0.375f, 0.5f, 0.75f),
		
		// right outer wall ceiling
		new Vector3f(0.5f, -0.5f, 0.75f),
		new Vector3f(0.5f, 0.5f, 0.75f),
		new Vector3f(0.5f, 0.5f, 0.875f),
		new Vector3f(0.5f, -0.5f, 0.875f),
		
		// right outer wall back
		new Vector3f(0.5f, -0.5f, 0.75f),
		new Vector3f(0.5f, -0.5f, 0.625f),
		new Vector3f(0.5f, -0.375f, 0.625f),
		new Vector3f(0.5f, -0.375f, 0.75f),
		
		// right outer wall front
		new Vector3f(0.5f, 0.375f, 0.75f),
		new Vector3f(0.5f, 0.375f, 0.625f),
		new Vector3f(0.5f, 0.5f, 0.625f),
		new Vector3f(0.5f, 0.5f, 0.75f),
		
		// right inner wall back
		new Vector3f(0.375f, -0.5f, 0.75f),
		new Vector3f(0.375f, -0.375f, 0.75f),
		new Vector3f(0.375f, -0.375f, 0.625f),
		new Vector3f(0.375f, -0.5f, 0.625f),
		
		// right inner wall front
		new Vector3f(0.375f, 0.375f, 0.75f),
		new Vector3f(0.375f, 0.5f, 0.75f),
		new Vector3f(0.375f, 0.5f, 0.625f),
		new Vector3f(0.375f, 0.375f, 0.625f),
		
		// right inner/outer wall back/back
		new Vector3f(0.375f, -0.5f, 0.625f),
		new Vector3f(0.5f, -0.5f, 0.625f),
		new Vector3f(0.5f, -0.5f, 0.75f),
		new Vector3f(0.375f, -0.5f, 0.75f),
		
		// right inner/outer wall back/front
		new Vector3f(0.375f, -0.375f, 0.625f),
		new Vector3f(0.375f, -0.375f, 0.75f),
		new Vector3f(0.5f, -0.375f, 0.75f),
		new Vector3f(0.5f, -0.375f, 0.625f),
		
		// right inner/outer wall front/back
		new Vector3f(0.375f, 0.375f, 0.625f),
		new Vector3f(0.5f, 0.375f, 0.625f),
		new Vector3f(0.5f, 0.375f, 0.75f),
		new Vector3f(0.375f, 0.375f, 0.75f),
		
		// right inner/outer wall front/front
		new Vector3f(0.375f, 0.5f, 0.625f),
		new Vector3f(0.375f, 0.5f, 0.75f),
		new Vector3f(0.5f, 0.5f, 0.75f),
		new Vector3f(0.5f, 0.5f, 0.625f),
	};
	static {
		applyZFightingCorrection(HULL_POINTS);
		applyZFightingCorrection(HULL_ROOF_POINTS);
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
	public FlatConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation, ConveyorBlockType.FLAT);
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
	public FlatConveyorBlock(int x, int y, int z, Orientation orientation, boolean deletable) {
		super(x, y, z, orientation, deletable, ConveyorBlockType.FLAT);
	}
	
	@Override
	public Block clone() {
		return new FlatConveyorBlock(x, y, z, orientation);
	}
	
	@Override
	public String getBlockID() {
		return "cf";
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
		if (neighbor1 == null && getConveyorBlockType() != ConveyorBlockType.ENTER) {
			addBendYZ(lefts, Math.PI, Math.PI / 2, -0.375f, -0.375f, 0.25f, 0.125);
		} else {
			lefts.add(new Vector3f(-0.375f, -0.5f + (getConveyorBlockType() ==
					ConveyorBlockType.ENTER ? 2 * Z_DELTA : 0), 0.375f));
		}
		if (neighbor2 == null && getConveyorBlockType() != ConveyorBlockType.LEAVE) {
			addBendYZ(lefts, Math.PI / 2, 0, -0.375f, 0.375f, 0.25f, 0.125);
		} else {
			lefts.add(new Vector3f(-0.375f, 0.5f - (getConveyorBlockType() ==
					ConveyorBlockType.LEAVE ? 2 * Z_DELTA : 0), 0.375f));
		}
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getTopCoordinatesRight(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Vector3f> rights = new ArrayList<>();
		if (neighbor1 == null && getConveyorBlockType() != ConveyorBlockType.ENTER) {
			addBendYZ(rights, Math.PI, Math.PI / 2, 0.375f, -0.375f, 0.25f, 0.125);
		} else {
			rights.add(new Vector3f(0.375f, -0.5f + (getConveyorBlockType() ==
					ConveyorBlockType.ENTER ? 2 * Z_DELTA : 0), 0.375f));
		}
		if (neighbor2 == null && getConveyorBlockType() != ConveyorBlockType.LEAVE) {
			addBendYZ(rights, Math.PI / 2, 0, 0.375f, 0.375f, 0.25f, 0.125);
		} else {
			rights.add(new Vector3f(0.375f, 0.5f - (getConveyorBlockType() ==
					ConveyorBlockType.LEAVE ? 2 * Z_DELTA : 0), 0.375f));
		}
		return rights;
	}

	@Override
	public ArrayList<Double> getTopTextureCoordinates(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Double> texs = new ArrayList<>();
		double texCoord;
		if (neighbor1 == null && getConveyorBlockType() != ConveyorBlockType.ENTER) {
			texCoord = addBendTextureCoordinates(texs, Math.PI, Math.PI / 2,
					0.125, 0.0);
		} else {
			texs.add(0.0);
			texCoord = 0;
		}
		if (neighbor2 == null && getConveyorBlockType() != ConveyorBlockType.LEAVE) {
			addBendTextureCoordinates(texs, Math.PI / 2, 0, 0.125, texCoord + 6.0);
		} else {
			texs.add(8.0);
		}
		return texs;
	}

	@Override
	public ArrayList<Vector3f> getBottomCoordinatesLeft(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		if (neighbor2 == null && getConveyorBlockType() != ConveyorBlockType.LEAVE) {
			addBendYZ(lefts, Math.PI * 2, Math.PI * 3 / 2, -0.375f, 0.375f, 0.25f, 0.125);
		} else {
			lefts.add(new Vector3f(-0.375f, 0.5f - (getConveyorBlockType() ==
					ConveyorBlockType.LEAVE ? 2 * Z_DELTA : 0), 0.125f));
		}
		if (neighbor1 == null && getConveyorBlockType() != ConveyorBlockType.ENTER) {
			addBendYZ(lefts, Math.PI * 3 / 2, Math.PI, -0.375f, -0.375f, 0.25f, 0.125);
		} else {
			lefts.add(new Vector3f(-0.375f, -0.5f + (getConveyorBlockType() ==
					ConveyorBlockType.ENTER ? 2 * Z_DELTA : 0), 0.125f));
		}
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getBottomCoordinatesRight(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Vector3f> rights = new ArrayList<>();
		if (neighbor2 == null && getConveyorBlockType() != ConveyorBlockType.LEAVE) {
			addBendYZ(rights, Math.PI * 2, Math.PI * 3 / 2, 0.375f, 0.375f, 0.25f, 0.125);
		} else {
			rights.add(new Vector3f(0.375f, 0.5f - (getConveyorBlockType() ==
					ConveyorBlockType.LEAVE ? 2 * Z_DELTA : 0), 0.125f));
		}
		if (neighbor1 == null && getConveyorBlockType() != ConveyorBlockType.ENTER) {
			addBendYZ(rights, Math.PI * 3 / 2, Math.PI, 0.375f, -0.375f, 0.25f, 0.125);
		} else {
			rights.add(new Vector3f(0.375f, -0.5f + (getConveyorBlockType() ==
					ConveyorBlockType.ENTER ? 2 * Z_DELTA : 0), 0.125f));
		}
		return rights;
	}

	@Override
	public ArrayList<Double> getBottomTextureCoordinates(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Double> texs = new ArrayList<>();
		double texCoord;
		if (neighbor2 == null && getConveyorBlockType() != ConveyorBlockType.LEAVE) {
			texCoord = addBendTextureCoordinates(texs, Math.PI * 2,
					Math.PI * 3 / 2, 0.125, 0.0);
		} else {
			texs.add(0.0);
			texCoord = 0;
		}
		if (neighbor1 == null && getConveyorBlockType() != ConveyorBlockType.ENTER) {
			addBendTextureCoordinates(texs, Math.PI * 3 / 2, Math.PI, 0.125,
					texCoord + 6.0);
		} else {
			texs.add(8.0);
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
