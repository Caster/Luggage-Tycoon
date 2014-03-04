package accg.objects.blocks;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import accg.objects.Luggage;

public class BendRightConveyorBlock extends ConveyorBlock {

	public BendRightConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation);
	}
	
	@Override
	public ArrayList<Vector3f> getTopCoordinatesLeft() {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		for (int i = 0; i <= 32; i++) {
			lefts.add(new Vector3f(
					0.375f + 0.75f * (float) Math.sin((i - 32) * (Math.PI / 64)),
					-0.375f + 0.75f * (float) Math.cos((i - 32) * (Math.PI / 64)),
					0.375f));
		}
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getTopCoordinatesRight() {
		ArrayList<Vector3f> rights = new ArrayList<>();
		for (int i = 0; i <= 32; i++) {
			rights.add(new Vector3f(0.375f, -0.375f, 0.375f));
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
		for (int i = 32; i >= 0; i--) {
			lefts.add(new Vector3f(
					0.375f + 0.75f * (float) Math.sin((i - 32) * (Math.PI / 64)),
					-0.375f + 0.75f * (float) Math.cos((i - 32) * (Math.PI / 64)),
					0.125f));
		}
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getBottomCoordinatesRight() {
		ArrayList<Vector3f> rights = new ArrayList<>();
		for (int i = 0; i <= 32; i++) {
			rights.add(new Vector3f(0.375f, -0.375f, 0.125f));
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
	public void furtherPosition(Luggage l, double step) {
		// TODO Automatisch gegenereerde methodestub
		
	}

	@Override
	public boolean canTakeLuggage(Luggage l) {
		// TODO Automatisch gegenereerde methodestub
		return false;
	}

	@Override
	public void takeLuggage(Luggage l) {
		// TODO Automatisch gegenereerde methodestub
		
	}
}
