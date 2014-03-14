package accg.objects.blocks;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import accg.objects.Block;

public class StraightConveyorBlock extends ConveyorBlock {

	public StraightConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation, ConveyorBlockType.STRAIGHT);
	}
	
	@Override
	public Block clone() {
		return new StraightConveyorBlock(x, y, z, orientation);
	}
	
	@Override
	public ArrayList<Vector3f> getTopCoordinatesLeft(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		if (neighbor1 == null) {
			addBendYZ(lefts, Math.PI, Math.PI / 2, -0.375f, -0.375f, 0.25f, 0.125);
		} else {
			lefts.add(new Vector3f(-0.375f, -0.5f, 0.375f));
		}
		if (neighbor2 == null) {
			addBendYZ(lefts, Math.PI / 2, 0, -0.375f, 0.375f, 0.25f, 0.125);
		} else {
			lefts.add(new Vector3f(-0.375f, 0.5f, 0.375f));
		}
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getTopCoordinatesRight(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Vector3f> rights = new ArrayList<>();
		if (neighbor1 == null) {
			addBendYZ(rights, Math.PI, Math.PI / 2, 0.375f, -0.375f, 0.25f, 0.125);
		} else {
			rights.add(new Vector3f(0.375f, -0.5f, 0.375f));
		}
		if (neighbor2 == null) {
			addBendYZ(rights, Math.PI / 2, 0, 0.375f, 0.375f, 0.25f, 0.125);
		} else {
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
			texCoord = addBendTextureCoordinates(texs, Math.PI, Math.PI / 2,
					0.125, 0.0);
		} else {
			texs.add(0.0);
			texCoord = 0;
		}
		if (neighbor2 == null) {
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
		if (neighbor2 == null) {
			addBendYZ(lefts, Math.PI * 2, Math.PI * 3 / 2, -0.375f, 0.375f, 0.25f, 0.125);
		} else {
			lefts.add(new Vector3f(-0.375f, 0.5f, 0.125f));
		}
		if (neighbor1 == null) {
			addBendYZ(lefts, Math.PI * 3 / 2, Math.PI, -0.375f, -0.375f, 0.25f, 0.125);
		} else {
			lefts.add(new Vector3f(-0.375f, -0.5f, 0.125f));
		}
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getBottomCoordinatesRight(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Vector3f> rights = new ArrayList<>();
		if (neighbor2 == null) {
			addBendYZ(rights, Math.PI * 2, Math.PI * 3 / 2, 0.375f, 0.375f, 0.25f, 0.125);
		} else {
			rights.add(new Vector3f(0.375f, 0.5f, 0.125f));
		}
		if (neighbor1 == null) {
			addBendYZ(rights, Math.PI * 3 / 2, Math.PI, 0.375f, -0.375f, 0.25f, 0.125);
		} else {
			rights.add(new Vector3f(0.375f, -0.5f, 0.125f));
		}
		return rights;
	}

	@Override
	public ArrayList<Double> getBottomTextureCoordinates(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Double> texs = new ArrayList<>();
		double texCoord;
		if (neighbor2 == null) {
			texCoord = addBendTextureCoordinates(texs, Math.PI * 2,
					Math.PI * 3 / 2, 0.125, 0.0);
		} else {
			texs.add(0.0);
			texCoord = 0;
		}
		if (neighbor1 == null) {
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
