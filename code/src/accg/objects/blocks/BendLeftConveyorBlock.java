package accg.objects.blocks;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import accg.utils.Utils;

public class BendLeftConveyorBlock extends ConveyorBlock {

	public BendLeftConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation, ConveyorBlockType.BEND_LEFT);
	}


	
	@Override
	public ArrayList<Vector3f> getTopCoordinatesLeft() {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		addBendYZ(lefts, Math.PI, Math.PI / 2, -0.375f, -0.375f, 0.25f, 0.125);
		
		addBendXY(lefts, 0, Math.PI / 2, 0.375f, -0.375f, -0.375f, 0);
		
		ArrayList<Vector3f> leftsAppend = new ArrayList<>();
		addBendYZ(leftsAppend, Math.PI / 2, 0, -0.375f, 0.375f, 0.25f, 0.125);
		Utils.rotatePoints(Orientation.LEFT, leftsAppend);
		lefts.addAll(leftsAppend);
		
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getTopCoordinatesRight() {
		ArrayList<Vector3f> rights = new ArrayList<>();
		addBendYZ(rights, Math.PI, Math.PI / 2, 0.375f, -0.375f, 0.25f, 0.125);
		
		addBendXY(rights, 0, Math.PI / 2, 0.375f, -0.375f, -0.375f, 0.75);
		
		ArrayList<Vector3f> rightsAppend = new ArrayList<>();
		addBendYZ(rightsAppend, Math.PI / 2, 0, 0.375f, 0.375f, 0.25f, 0.125);
		Utils.rotatePoints(Orientation.LEFT, rightsAppend);
		rights.addAll(rightsAppend);
		
		return rights;
	}

	@Override
	public ArrayList<Double> getTopTextureCoordinates() {
		ArrayList<Double> texs = new ArrayList<>();
		double texCoord = addBendTextureCoordinates(texs, Math.PI, Math.PI / 2,
				0.125, 0.0);
		texCoord = addBendTextureCoordinates(texs, 0, Math.PI / 2, 0.75, texCoord);
		addBendTextureCoordinates(texs, Math.PI / 2, 0, 0.125, texCoord);
		return texs;
	}

	@Override
	public ArrayList<Vector3f> getBottomCoordinatesLeft() {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		addBendYZ(lefts, Math.PI * 2, Math.PI * 3 / 2, -0.375f, 0.375f, 0.25f, 0.125);
		Utils.rotatePoints(Orientation.LEFT, lefts);
		
		addBendXY(lefts, Math.PI / 2, 0, 0.125f, -0.375f, -0.375f, 0);
		
		addBendYZ(lefts, Math.PI * 3 / 2, Math.PI, -0.375f, -0.375f, 0.25f, 0.125);
		
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getBottomCoordinatesRight() {
		ArrayList<Vector3f> rights = new ArrayList<>();
		addBendYZ(rights, Math.PI * 2, Math.PI * 3 / 2, 0.375f, 0.375f, 0.25f, 0.125);
		Utils.rotatePoints(Orientation.LEFT, rights);
		
		addBendXY(rights, Math.PI / 2, 0, 0.125f, -0.375f, -0.375f, 0.75);
		
		addBendYZ(rights, Math.PI * 3 / 2, Math.PI, 0.375f, -0.375f, 0.25f, 0.125);
		
		return rights;
	}

	@Override
	public ArrayList<Double> getBottomTextureCoordinates() {
		ArrayList<Double> texs = new ArrayList<>();
		double texCoord = addBendTextureCoordinates(texs, Math.PI * 2,
				Math.PI * 3 / 2, 0.125, 0.0);
		texCoord = addBendTextureCoordinates(texs, Math.PI / 2, 0, 0.625, texCoord);
		addBendTextureCoordinates(texs, Math.PI * 3 / 2, Math.PI, 0.125, texCoord);
		return texs;
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
