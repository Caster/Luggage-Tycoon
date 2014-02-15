package accg.objects.blocks;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import accg.State;
import accg.objects.Block.Orientation;
import accg.objects.Luggage;

public class BendLeftConveyorBlock extends ConveyorBlock {

	public BendLeftConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation);
	}
	
	@Override
	protected ArrayList<Vector3f> getTopCoordinatesLeft() {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		for (int i = 0; i <= 32; i++) {
			lefts.add(new Vector3f(-0.375f, -0.375f, 0.375f));
		}
		return lefts;
	}

	@Override
	protected ArrayList<Vector3f> getTopCoordinatesRight() {
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
	protected ArrayList<Double> getTopTextureCoordinates() {
		ArrayList<Double> texs = new ArrayList<>();
		for (int i = 0; i <= 32; i++) {
			texs.add(i / 16.0);
		}
		return texs;
	}

	@Override
	protected ArrayList<Vector3f> getBottomCoordinatesLeft() {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		for (int i = 0; i <= 32; i++) {
			lefts.add(new Vector3f(-0.375f, -0.375f, 0.125f));
		}
		return lefts;
	}

	@Override
	protected ArrayList<Vector3f> getBottomCoordinatesRight() {
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
	protected ArrayList<Double> getBottomTextureCoordinates() {
		ArrayList<Double> texs = new ArrayList<>();
		for (int i = 0; i <= 32; i++) {
			texs.add(i / 16.0);
		}
		return texs;
	}

	@Override
	protected void furtherPositionInternal(Luggage l, double step) {
		// TODO Automatisch gegenereerde methodestub
		
	}
}