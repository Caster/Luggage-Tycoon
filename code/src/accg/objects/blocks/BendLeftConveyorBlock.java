package accg.objects.blocks;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import accg.State;
import accg.objects.Block;
import accg.objects.Orientation;
import accg.utils.Utils;

public class BendLeftConveyorBlock extends ConveyorBlock {

	public BendLeftConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation, ConveyorBlockType.BEND_LEFT);
	}

	public BendLeftConveyorBlock(int x, int y, int z, Orientation orientation, boolean deletable) {
		super(x, y, z, orientation, deletable, ConveyorBlockType.BEND_LEFT);
	}
	
	@Override
	protected void drawArrowShape() {
		glNormal3f(0, 0, 1);
		
		double step = Math.PI / 32;
		for (int i = 0; i < 16; i++) {
			glVertex3f(-0.3f + 0.18f * (float) Math.cos(step * i), -0.3f + 0.18f * (float) Math.sin(step * i), 1);
			glVertex3f(-0.3f + 0.42f * (float) Math.cos(step * i), -0.3f + 0.42f * (float) Math.sin(step * i), 1);
			glVertex3f(-0.3f + 0.42f * (float) Math.cos(step * (i + 1)), -0.3f + 0.42f * (float) Math.sin(step * (i + 1)), 1);
			glVertex3f(-0.3f + 0.18f * (float) Math.cos(step * (i + 1)), -0.3f + 0.18f * (float) Math.sin(step * (i + 1)), 1);
		}
		
		glVertex3f(-0.3f, 0, 1);
		glVertex3f(-0.3f, 0.25f, 1);
		glVertex3f(-0.5f, 0, 1);
		glVertex3f(-0.3f, -0.25f, 1);
	}
	
	@Override
	public Block clone() {
		return new BendLeftConveyorBlock(x, y, z, orientation);
	}
	
	@Override
	public String getBlockID() {
		return "cbl";
	}
	
	@Override
	public ArrayList<Vector3f> getTopCoordinatesLeft(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		if (neighbor1 != null && neighbor2 != null) {
			addBendXY(lefts, 0, Math.PI / 2, 0.375f, -0.5f, -0.5f, 0.125);
		} else {
			if (neighbor1 == null) {
				addBendYZ(lefts, Math.PI, Math.PI / 2, -0.375f, -0.375f, 0.25f, 0.125);
			} else {
				lefts.add(new Vector3f(-0.375f, -0.5f, 0.375f));
			}
			
			addBendXY(lefts, 0, Math.PI / 2, 0.375f, -0.375f, -0.375f, 0);
			
			if (neighbor2 == null) {
				ArrayList<Vector3f> leftsAppend = new ArrayList<>();
				addBendYZ(leftsAppend, Math.PI / 2, 0, -0.375f, 0.375f, 0.25f, 0.125);
				Utils.rotatePoints(Orientation.LEFT, leftsAppend);
				lefts.addAll(leftsAppend);
			} else {
				lefts.add(new Vector3f(-0.5f, -0.375f, 0.375f));
			}
		}
		
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getTopCoordinatesRight(
			ConveyorBlock neighbor1, ConveyorBlock neighbor2) {
		ArrayList<Vector3f> rights = new ArrayList<>();
		if (neighbor1 != null && neighbor2 != null) {
			addBendXY(rights, 0, Math.PI / 2, 0.375f, -0.5f, -0.5f, 0.875);
		} else {
			if (neighbor1 == null) {
				addBendYZ(rights, Math.PI, Math.PI / 2, 0.375f, -0.375f, 0.25f, 0.125);
			} else {
				rights.add(new Vector3f(0.375f, -0.5f, 0.375f));
			}
			
			addBendXY(rights, 0, Math.PI / 2, 0.375f, -0.375f, -0.375f, 0.75);
			
			if (neighbor2 == null) {
				ArrayList<Vector3f> rightsAppend = new ArrayList<>();
				addBendYZ(rightsAppend, Math.PI / 2, 0, 0.375f, 0.375f, 0.25f, 0.125);
				Utils.rotatePoints(Orientation.LEFT, rightsAppend);
				rights.addAll(rightsAppend);
			} else {
				rights.add(new Vector3f(-0.5f, 0.375f, 0.375f));
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
		
		texCoord = addBendTextureCoordinates(texs, 0, Math.PI / 2,
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
			addBendXY(lefts, Math.PI / 2, 0, 0.125f, -0.5f, -0.5f, 0.125);
		} else {
			if (neighbor2 == null) {
				addBendYZ(lefts, Math.PI * 2, Math.PI * 3 / 2, -0.375f, 0.375f, 0.25f, 0.125);
				Utils.rotatePoints(Orientation.LEFT, lefts);
			} else {
				lefts.add(new Vector3f(-0.5f, -0.375f, 0.125f));
			}
			
			addBendXY(lefts, Math.PI / 2, 0, 0.125f, -0.375f, -0.375f, 0);
			
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
			addBendXY(rights, Math.PI / 2, 0, 0.125f, -0.5f, -0.5f, 0.875);
		} else {
			if (neighbor2 == null) {
				addBendYZ(rights, Math.PI * 2, Math.PI * 3 / 2, 0.375f, 0.375f, 0.25f, 0.125);
				Utils.rotatePoints(Orientation.LEFT, rights);
			} else {
				rights.add(new Vector3f(-0.5f, 0.375f, 0.125f));
			}
			
			addBendXY(rights, Math.PI / 2, 0, 0.125f, -0.375f, -0.375f, 0.75);
			
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
		
		texCoord = addBendTextureCoordinates(texs, Math.PI / 2, 0,
				(neighbor1 != null && neighbor2 != null ? 0.875 : 0.75), texCoord);
		
		if (neighbor1 == null || neighbor2 == null) {
			addBendTextureCoordinates(texs, Math.PI * 3 / 2, Math.PI, 0.125, texCoord);
		}
		return texs;
	}

	@Override
	public Vector3f getAngularVelocity() {
		return new Vector3f(0, 0, (float) Math.PI);
	}
	
	@Override
	public Vector3f getLinearVelocity() {
		switch (getOrientation()) {
		case DOWN :  return new Vector3f( 1, -1, 0);
		case LEFT :  return new Vector3f(-1, -1, 0);
		case RIGHT : return new Vector3f( 1,  1, 0);
		default :    return new Vector3f(-1,  1, 0);
		}
	}
}
