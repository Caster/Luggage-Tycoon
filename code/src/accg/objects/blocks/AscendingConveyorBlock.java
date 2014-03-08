package accg.objects.blocks;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

/**
 * An {@code AscendingConveyorBlock} is a conveyor block that tilts luggage up.
 * 
 * Its coordinate space is within [-0.5, 0.5] x [-0.5, 0.5], just like other
 * {@link ConveyorBelt}. The cylinders of this belt are located at [-0.375; -0.25]
 * and [0.375; 0.0] in the YZ plane (and span in the X direction). The radii of
 * these cylinders is 0.125.
 */
public class AscendingConveyorBlock extends ConveyorBlock {

	public AscendingConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation, ConveyorBlockType.ASCENDING);
	}
	
	@Override
	public ArrayList<Vector3f> getTopCoordinatesLeft() {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		addBendYZ(lefts, Math.PI, Math.PI / 2 + Math.atan2(1, 3), -0.375f,
				-0.375f, 0.25f, 0.125);
		addBendYZ(lefts, Math.PI / 2 + Math.atan2(1, 3), 0, -0.375f, 0.375f,
				0.5f, 0.125);
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getTopCoordinatesRight() {
		ArrayList<Vector3f> rights = new ArrayList<>();
		addBendYZ(rights, Math.PI, Math.PI / 2 + Math.atan2(1, 3), 0.375f,
				-0.375f, 0.25f, 0.125);
		addBendYZ(rights, Math.PI / 2 + Math.atan2(1, 3), 0, 0.375f, 0.375f,
				0.5f, 0.125);
		return rights;
	}

	@Override
	public ArrayList<Double> getTopTextureCoordinates() {
		ArrayList<Double> texs = new ArrayList<>();
		double texCoord = addBendYZTextureCoordinates(texs, Math.PI,
				Math.PI / 2 + Math.atan2(1, 3), 0.125, 0.0);
		addBendYZTextureCoordinates(texs, Math.PI / 2 + Math.atan2(1, 3), 0,
				0.125, texCoord + 6.0);
		return texs;
	}

	@Override
	public ArrayList<Vector3f> getBottomCoordinatesLeft() {
		ArrayList<Vector3f> lefts = new ArrayList<>();
		addBendYZ(lefts, 0, -Math.PI / 2 + Math.atan2(1, 3), -0.375f, 0.375f,
				0.5f, 0.125);
		addBendYZ(lefts, Math.PI * 3 / 2 + Math.atan2(1, 3), Math.PI, -0.375f,
				-0.375f, 0.25f, 0.125);
		return lefts;
	}

	@Override
	public ArrayList<Vector3f> getBottomCoordinatesRight() {
		ArrayList<Vector3f> rights = new ArrayList<>();
		addBendYZ(rights, 0, -Math.PI / 2 + Math.atan2(1, 3), 0.375f, 0.375f,
				0.5f, 0.125);
		addBendYZ(rights, Math.PI * 3 / 2 + Math.atan2(1, 3), Math.PI, 0.375f,
				-0.375f, 0.25f, 0.125);
		return rights;
	}

	@Override
	public ArrayList<Double> getBottomTextureCoordinates() {
		ArrayList<Double> texs = new ArrayList<>();
		double texCoord = addBendYZTextureCoordinates(texs,
				0, -Math.PI / 2 + Math.atan2(1, 3), 0.125, 0.0);
		addBendYZTextureCoordinates(texs, Math.PI * 3 / 2 + Math.atan2(1, 3),
				Math.PI, 0.125, texCoord + 6.0);
		return texs;
	}

	@Override
	public Vector3f getLinearVelocity() {
		switch (getOrientation()) {
		case DOWN :  return new Vector3f(0, -1, 1);
		case LEFT :  return new Vector3f(-1, 0, 1);
		case RIGHT : return new Vector3f( 1, 0, 1);
		default :    return new Vector3f(0,  1, 1);
		}
	}
}
