package accg.objects.blocks;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

public class BendLeftConveyorBlock extends ConveyorBlock {

	public BendLeftConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation, ConveyorBlockType.BEND_LEFT);
	}
	
	@Override
	public ArrayList<Vector3f> getTopCoordinatesLeft() {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		for (int i = 0; i <= 32; i++) {
			lefts.add(new Vector3f(-0.375f, -0.375f, 0.375f));
		}
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getTopCoordinatesRight() {
		ArrayList<Vector3f> rights = new ArrayList<>();
		for (int i = 0; i <= 32; i++) {
			rights.add(new Vector3f(
					-0.375f + 0.75f * (float) Math.cos(i * (Math.PI / 64)),
					-0.375f + 0.75f * (float) Math.sin(i * (Math.PI / 64)),
					0.375f));
		}
		return rights;
	}

	@Override
	public ArrayList<Double> getTopTextureCoordinates() {
		ArrayList<Double> texs = new ArrayList<>();
		for (int i = 0; i <= 32; i++) {
			texs.add(i / 16.0);
		}
		return texs;
	}

	@Override
	public ArrayList<Vector3f> getBottomCoordinatesLeft() {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		for (int i = 0; i <= 32; i++) {
			lefts.add(new Vector3f(-0.375f, -0.375f, 0.125f));
		}
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getBottomCoordinatesRight() {
		ArrayList<Vector3f> rights = new ArrayList<>();
		for (int i = 32; i >= 0; i--) {
			rights.add(new Vector3f(
					-0.375f + 0.75f * (float) Math.cos(i * (Math.PI / 64)),
					-0.375f + 0.75f * (float) Math.sin(i * (Math.PI / 64)),
					0.125f));
		}
		return rights;
	}

	@Override
	public ArrayList<Double> getBottomTextureCoordinates() {
		ArrayList<Double> texs = new ArrayList<>();
		for (int i = 0; i <= 32; i++) {
			texs.add(i / 16.0);
		}
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
