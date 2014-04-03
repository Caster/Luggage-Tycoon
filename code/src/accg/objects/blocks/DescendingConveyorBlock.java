package accg.objects.blocks;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import accg.objects.Block;
import accg.objects.Orientation;

public class DescendingConveyorBlock extends ConveyorBlock {

	public DescendingConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation, ConveyorBlockType.DESCENDING);
	}
	
	public DescendingConveyorBlock(int x, int y, int z, Orientation orientation, boolean deletable) {
		super(x, y, z, orientation, deletable, ConveyorBlockType.DESCENDING);
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
