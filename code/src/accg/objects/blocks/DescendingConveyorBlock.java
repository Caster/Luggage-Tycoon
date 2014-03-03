package accg.objects.blocks;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import accg.objects.Luggage;

public class DescendingConveyorBlock extends ConveyorBlock {

	public DescendingConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation);
	}
	
	@Override
	protected ArrayList<Vector3f> getTopCoordinatesLeft() {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		addBendYZ(lefts, Math.PI, Math.PI / 2 - Math.atan2(1, 3), -0.375f,
				-0.375f, 0.5f, 0.125);
		addBendYZ(lefts, Math.PI / 2 - Math.atan2(1, 3), 0, -0.375f, 0.375f,
				0.25f, 0.125);
		return lefts;
	}

	@Override
	protected ArrayList<Vector3f> getTopCoordinatesRight() {
		ArrayList<Vector3f> rights = new ArrayList<>();
		addBendYZ(rights, Math.PI, Math.PI / 2 - Math.atan2(1, 3), 0.375f,
				-0.375f, 0.5f, 0.125);
		addBendYZ(rights, Math.PI / 2 - Math.atan2(1, 3), 0, 0.375f, 0.375f,
				0.25f, 0.125);
		return rights;
	}

	@Override
	protected ArrayList<Double> getTopTextureCoordinates() {
		ArrayList<Double> texs = new ArrayList<>();
		double texCoord = addBendYZTextureCoordinates(texs, Math.PI,
				Math.PI / 2 - Math.atan2(1, 3), 0.125, 0.0);
		addBendYZTextureCoordinates(texs, Math.PI / 2 - Math.atan2(1, 3), 0,
				0.125, texCoord + 6.0);
		return texs;
	}

	@Override
	protected ArrayList<Vector3f> getBottomCoordinatesLeft() {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		addBendYZ(lefts, 0, -Math.PI / 2 - Math.atan2(1, 3), -0.375f, 0.375f,
				0.25f, 0.125);
		addBendYZ(lefts, Math.PI * 3 / 2 - Math.atan2(1, 3), Math.PI, -0.375f,
				-0.375f, 0.5f, 0.125);
		return lefts;
	}

	@Override
	protected ArrayList<Vector3f> getBottomCoordinatesRight() {
		ArrayList<Vector3f> rights = new ArrayList<>();
		addBendYZ(rights, 0, -Math.PI / 2 - Math.atan2(1, 3), 0.375f, 0.375f,
				0.25f, 0.125);
		addBendYZ(rights, Math.PI * 3 / 2 - Math.atan2(1, 3), Math.PI, 0.375f,
				-0.375f, 0.5f, 0.125);
		return rights;
	}

	@Override
	protected ArrayList<Double> getBottomTextureCoordinates() {
		ArrayList<Double> texs = new ArrayList<>();
		double texCoord = addBendYZTextureCoordinates(texs,
				0, -Math.PI / 2 - Math.atan2(1, 3), 0.125, 0.0);
		addBendYZTextureCoordinates(texs, Math.PI * 3 / 2 - Math.atan2(1, 3),
				Math.PI, 0.125, texCoord + 6.0);
		return texs;
	}

	@Override
	public void furtherPosition(Luggage l, double step) {
		assert (l.supportingBlock == this);
		
		switch (orientation) {
		case UP:
			l.y += step;
			l.z -= step / 3;
			if (l.y > y + 0.5) {
				l.supportingBlock = null;
				l.vx = 0;
				l.vy = 1;
				l.vz = -1 / 3.0f;
			}
			break;
		case DOWN:
			l.y -= step;
			l.z -= step / 3;
			if (l.y < y - 0.5) {
				l.supportingBlock = null;
				l.vx = 0;
				l.vy = -1;
				l.vz = -1 / 3.0f;
			}
			break;
		case RIGHT:
			l.x += step;
			l.z -= step / 3;
			if (l.x > x + 0.5) {
				l.supportingBlock = null;
				l.vx = 1;
				l.vy = 0;
				l.vz = -1 / 3.0f;
			}
			break;
		case LEFT:
			l.x -= step;
			l.z -= step / 3;
			if (l.x < x - 0.5) {
				l.supportingBlock = null;
				l.vx = -1;
				l.vy = 0;
				l.vz = -1 / 3.0f;
			}
			break;
		}
	}

	@Override
	public boolean canTakeLuggage(Luggage l) {
		// TODO adjust this to the DescendingConveyorBelt
		switch (orientation) {
		case RIGHT:
			return l.x > x - 0.5 && l.x < x + 0.5
				    && l.y > y - 0.375 && l.y < y + 0.375
				    && l.z > z / 4.0 + 0.375 && l.z < z / 4.0 + 0.625;
		case LEFT:
			return l.x > x - 0.5 && l.x < x + 0.5
				    && l.y > y - 0.375 && l.y < y + 0.375
				    && l.z > z / 4.0 + 0.375 && l.z < z / 4.0 + 0.625;
		case UP:
			return l.x > x - 0.375 && l.x < x + 0.375
				    && l.y > y - 0.5 && l.y < y + 0.5
				    && l.z > z / 4.0 + 0.375 && l.z < z / 4.0 + 0.625;
		case DOWN:
		default:
			return l.x > x - 0.375 && l.x < x + 0.375
				    && l.y > y - 0.5 && l.y < y + 0.5
				    && l.z > z / 4.0 + 0.375 && l.z < z / 4.0 + 0.625;
		}
	}

	@Override
	public void takeLuggage(Luggage l) {
		l.z = z / 4.0f + 0.625f; // TODO adjust this to the DescendingConveyorBelt
		l.vz = 0;
	}
}
