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
		addBendYZ(lefts, Math.PI, Math.PI / 2, -0.375f, -0.375f, 0.25f, 0.125);
		addBendYZ(lefts, Math.PI / 2, 0, -0.375f, 0.375f, 0.25f, 0.125);
		return lefts;
	}

	@Override
	protected ArrayList<Vector3f> getTopCoordinatesRight() {
		ArrayList<Vector3f> rights = new ArrayList<>();
		addBendYZ(rights, Math.PI, Math.PI / 2, 0.375f, -0.375f, 0.25f, 0.125);
		addBendYZ(rights, Math.PI / 2, 0, 0.375f, 0.375f, 0.25f, 0.125);
		return rights;
	}

	@Override
	protected ArrayList<Double> getTopTextureCoordinates() {
		ArrayList<Double> texs = new ArrayList<>();
		double texCoord = addBendYZTextureCoordinates(texs, Math.PI, Math.PI / 2,
				0.125, 0.0);
		texCoord = addBendYZTextureCoordinates(texs, Math.PI / 2, 0, 0.125, texCoord + 6.0);
		return texs;
	}

	@Override
	protected ArrayList<Vector3f> getBottomCoordinatesLeft() {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		addBendYZ(lefts, Math.PI * 2, Math.PI * 3 / 2, -0.375f, 0.375f, 0.25f, 0.125);
		addBendYZ(lefts, Math.PI * 3 / 2, Math.PI, -0.375f, -0.375f, 0.25f, 0.125);
		return lefts;
	}

	@Override
	protected ArrayList<Vector3f> getBottomCoordinatesRight() {
		ArrayList<Vector3f> rights = new ArrayList<>();
		addBendYZ(rights, Math.PI * 2, Math.PI * 3 / 2, 0.375f, 0.375f, 0.25f, 0.125);
		addBendYZ(rights, Math.PI * 3 / 2, Math.PI, 0.375f, -0.375f, 0.25f, 0.125);
		return rights;
	}

	@Override
	protected ArrayList<Double> getBottomTextureCoordinates() {
		ArrayList<Double> texs = new ArrayList<>();
		double texCoord = addBendYZTextureCoordinates(texs, Math.PI * 2,
				Math.PI * 3 / 2, 0.125, 1.5);
		texCoord = addBendYZTextureCoordinates(texs, Math.PI * 3 / 2,
				Math.PI, 0.125, texCoord + 6.0);
		return texs;
	}

	@Override
	public void furtherPosition(Luggage l, double step) {
		assert (l.supportingBlock == this);
		
		switch (orientation) {
		case UP:
			l.y += step;
			if (l.y > y + 0.5) {
				l.supportingBlock = null;
				l.vx = 0;
				l.vy = 1;
				l.vz = 0;
			}
			break;
		case DOWN:
			l.y -= step;
			if (l.y < y - 0.5) {
				l.supportingBlock = null;
				l.vx = 0;
				l.vy = -1;
				l.vz = 0;
			}
			break;
		case RIGHT:
			l.x += step;
			if (l.x > x + 0.5) {
				l.supportingBlock = null;
				l.vx = 1;
				l.vy = 0;
				l.vz = 0;
			}
			break;
		case LEFT:
			l.x -= step;
			if (l.x < x - 0.5) {
				l.supportingBlock = null;
				l.vx = -1;
				l.vy = 0;
				l.vz = 0;
			}
			break;
		}
	}

	@Override
	public boolean canTakeLuggage(Luggage l) {
		if (orientation == Orientation.LEFT || orientation == Orientation.RIGHT) {
			return l.x > x - 0.5 && l.x < x + 0.5
			    && l.y > y - 0.375 && l.y < y + 0.375
			    && l.z > z / 4.0 + 0.125 && l.z < z / 4.0 + 0.375;
		} else {
			return l.x > x - 0.375 && l.x < x + 0.375
				    && l.y > y - 0.5 && l.y < y + 0.5
				    && l.z > z / 4.0 + 0.125 && l.z < z / 4.0 + 0.375;
		}
	}

	@Override
	public void takeLuggage(Luggage l) {
		l.z = z / 4.0 + 0.375;
	}
}
