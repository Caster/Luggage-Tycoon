package accg.objects.blocks;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import accg.objects.Luggage;

public class StraightConveyorBlock extends ConveyorBlock {

	public StraightConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation);
	}
	
	@Override
	protected ArrayList<Vector3f> getTopCoordinatesLeft() {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		lefts.add(new Vector3f(-0.375f, -0.375f, 0.375f));
		lefts.add(new Vector3f(-0.375f, 0.375f, 0.375f));
		return lefts;
	}

	@Override
	protected ArrayList<Vector3f> getTopCoordinatesRight() {
		ArrayList<Vector3f> rights = new ArrayList<>();
		rights.add(new Vector3f(0.375f, -0.375f, 0.375f));
		rights.add(new Vector3f(0.375f, 0.375f, 0.375f));
		return rights;
	}

	@Override
	protected ArrayList<Double> getTopTextureCoordinates() {
		ArrayList<Double> texs = new ArrayList<>();
		texs.add(0.0);
		texs.add(2.0);
		return texs;
	}

	@Override
	protected ArrayList<Vector3f> getBottomCoordinatesLeft() {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		lefts.add(new Vector3f(-0.375f, 0.375f, 0.125f));
		lefts.add(new Vector3f(-0.375f, -0.375f, 0.125f));
		return lefts;
	}

	@Override
	protected ArrayList<Vector3f> getBottomCoordinatesRight() {
		ArrayList<Vector3f> rights = new ArrayList<>();
		rights.add(new Vector3f(0.375f, 0.375f, 0.125f));
		rights.add(new Vector3f(0.375f, -0.375f, 0.125f));
		return rights;
	}

	@Override
	protected ArrayList<Double> getBottomTextureCoordinates() {
		ArrayList<Double> texs = new ArrayList<>();
		texs.add(0.0);
		texs.add(2.0);
		return texs;
	}

	@Override
	protected void furtherPositionInternal(Luggage l, double step) {
		// TODO Automatisch gegenereerde methodestub
		
	}
}