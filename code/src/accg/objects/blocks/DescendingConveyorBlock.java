package accg.objects.blocks;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import accg.objects.Luggage;

public class DescendingConveyorBlock extends ConveyorBlock {

	public DescendingConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation, ConveyorBlockType.DESCENDING);
	}
	
	@Override
	public ArrayList<Vector3f> getTopCoordinatesLeft() {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		addBendYZ(lefts, Math.PI, Math.PI / 2 - Math.atan2(1, 3), -0.375f,
				-0.375f, 0.5f, 0.125);
		addBendYZ(lefts, Math.PI / 2 - Math.atan2(1, 3), 0, -0.375f, 0.375f,
				0.25f, 0.125);
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getTopCoordinatesRight() {
		ArrayList<Vector3f> rights = new ArrayList<>();
		addBendYZ(rights, Math.PI, Math.PI / 2 - Math.atan2(1, 3), 0.375f,
				-0.375f, 0.5f, 0.125);
		addBendYZ(rights, Math.PI / 2 - Math.atan2(1, 3), 0, 0.375f, 0.375f,
				0.25f, 0.125);
		return rights;
	}

	@Override
	public ArrayList<Double> getTopTextureCoordinates() {
		ArrayList<Double> texs = new ArrayList<>();
		double texCoord = addBendYZTextureCoordinates(texs, Math.PI,
				Math.PI / 2 - Math.atan2(1, 3), 0.125, 0.0);
		addBendYZTextureCoordinates(texs, Math.PI / 2 - Math.atan2(1, 3), 0,
				0.125, texCoord + 6.0);
		return texs;
	}

	@Override
	public ArrayList<Vector3f> getBottomCoordinatesLeft() {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		addBendYZ(lefts, 0, -Math.PI / 2 - Math.atan2(1, 3), -0.375f, 0.375f,
				0.25f, 0.125);
		addBendYZ(lefts, Math.PI * 3 / 2 - Math.atan2(1, 3), Math.PI, -0.375f,
				-0.375f, 0.5f, 0.125);
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getBottomCoordinatesRight() {
		ArrayList<Vector3f> rights = new ArrayList<>();
		addBendYZ(rights, 0, -Math.PI / 2 - Math.atan2(1, 3), 0.375f, 0.375f,
				0.25f, 0.125);
		addBendYZ(rights, Math.PI * 3 / 2 - Math.atan2(1, 3), Math.PI, 0.375f,
				-0.375f, 0.5f, 0.125);
		return rights;
	}

	@Override
	public ArrayList<Double> getBottomTextureCoordinates() {
		ArrayList<Double> texs = new ArrayList<>();
		double texCoord = addBendYZTextureCoordinates(texs,
				0, -Math.PI / 2 - Math.atan2(1, 3), 0.125, 0.0);
		addBendYZTextureCoordinates(texs, Math.PI * 3 / 2 - Math.atan2(1, 3),
				Math.PI, 0.125, texCoord + 6.0);
		return texs;
	}

	@Override
	public void furtherPosition(Luggage l, double step) {
		// TODO: Reconsider this API
	}

	@Override
	public boolean canTakeLuggage(Luggage l) {
		// TODO: Reconsider this API
		return false;
	}

	@Override
	public void takeLuggage(Luggage l) {
		// TODO: Reconsider this API
	}
}
