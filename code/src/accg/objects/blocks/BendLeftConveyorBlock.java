package accg.objects.blocks;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import accg.objects.Block;
import accg.objects.Orientation;
import accg.utils.Utils;

/**
 * A ConveyorBlock that makes a 90 degree bend to the left.
 */
public class BendLeftConveyorBlock extends ConveyorBlock {

	/**
	 * Number of segments used for constructing bend hull.
	 */
	public static final int NUM_SEGMENTS = 16;
	/**
	 * Series of points that define the hull around this block.
	 * The resolution of the bend is {@link #NUM_SEGMENTS} segments. This is multiplied
	 * with the number of vertices per segment. This is the number of quads per segment
	 * times four. The extra 14 quads are because the beginning and end of the bend have
	 * a straight piece.
	 */
	public static final Vector3f[] HULL_POINTS = new Vector3f[4 * 14 + NUM_SEGMENTS * 4 * 7];
	/**
	 * Series of points that define the roof hull around this block.
	 */
	public static final Vector3f[] HULL_ROOF_POINTS = new Vector3f[4 * 16 + NUM_SEGMENTS * 4 * 4];
	static {
		int j = 0;
		
		// quad below conveyor at start of bend
		HULL_POINTS[j++] = new Vector3f(0.5f, -0.5f, 0);
		HULL_POINTS[j++] = new Vector3f(0.5f, -0.5f, 0.125f);
		HULL_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0.125f);
		HULL_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0);
		
		// quad left of conveyor at start of bend
		HULL_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0.125f);
		HULL_POINTS[j++] = new Vector3f(-0.375f, -0.5f, 0.125f);
		HULL_POINTS[j++] = new Vector3f(-0.375f, -0.5f, 0.625f);
		HULL_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0.625f);
		
		// quad right of conveyor at start of bend
		HULL_POINTS[j++] = new Vector3f(0.375f, -0.5f, 0.125f);
		HULL_POINTS[j++] = new Vector3f(0.5f, -0.5f, 0.125f);
		HULL_POINTS[j++] = new Vector3f(0.5f, -0.5f, 0.625f);
		HULL_POINTS[j++] = new Vector3f(0.375f, -0.5f, 0.625f);
		
		// quad below conveyor at end of bend
		HULL_POINTS[j++] = new Vector3f(-0.5f, 0.5f, 0);
		HULL_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0);
		HULL_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0.125f);
		HULL_POINTS[j++] = new Vector3f(-0.5f, 0.5f, 0.125f);
		
		// quad left of conveyor at end of bend
		HULL_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0.125f);
		HULL_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0.625f);
		HULL_POINTS[j++] = new Vector3f(-0.5f, -0.375f, 0.625f);
		HULL_POINTS[j++] = new Vector3f(-0.5f, -0.375f, 0.125f);
		
		// quad right of conveyor at end of bend
		HULL_POINTS[j++] = new Vector3f(-0.5f, 0.375f, 0.125f);
		HULL_POINTS[j++] = new Vector3f(-0.5f, 0.375f, 0.625f);
		HULL_POINTS[j++] = new Vector3f(-0.5f, 0.5f, 0.625f);
		HULL_POINTS[j++] = new Vector3f(-0.5f, 0.5f, 0.125f);
		
		// floor quad at start of bend
		HULL_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0);
		HULL_POINTS[j++] = new Vector3f(-0.5f, -0.375f, 0);
		HULL_POINTS[j++] = new Vector3f(0.5f, -0.375f, 0);
		HULL_POINTS[j++] = new Vector3f(0.5f, -0.5f, 0);
		
		// right outer wall at start of bend
		HULL_POINTS[j++] = new Vector3f(0.5f, -0.5f, 0);
		HULL_POINTS[j++] = new Vector3f(0.5f, -0.375f, 0);
		HULL_POINTS[j++] = new Vector3f(0.5f, -0.375f, 0.625f);
		HULL_POINTS[j++] = new Vector3f(0.5f, -0.5f, 0.625f);
		
		// right inner wall at start of bend
		HULL_POINTS[j++] = new Vector3f(0.375f, -0.5f, 0.125f);
		HULL_POINTS[j++] = new Vector3f(0.375f, -0.5f, 0.625f);
		HULL_POINTS[j++] = new Vector3f(0.375f, -0.375f, 0.625f);
		HULL_POINTS[j++] = new Vector3f(0.375f, -0.375f, 0.125f);
		
		// right wall top at start of bend
		HULL_POINTS[j++] = new Vector3f(0.375f, -0.5f, 0.625f);
		HULL_POINTS[j++] = new Vector3f(0.5f, -0.5f, 0.625f);
		HULL_POINTS[j++] = new Vector3f(0.5f, -0.375f, 0.625f);
		HULL_POINTS[j++] = new Vector3f(0.375f, -0.375f, 0.625f);
		
		// floor quad at end of bend
		HULL_POINTS[j++] = new Vector3f(-0.5f, -0.375f, 0);
		HULL_POINTS[j++] = new Vector3f(-0.5f, 0.5f, 0);
		HULL_POINTS[j++] = new Vector3f(-0.375f, 0.5f, 0);
		HULL_POINTS[j++] = new Vector3f(-0.375f, -0.375f, 0);
		
		// left outer wall at end of bend
		HULL_POINTS[j++] = new Vector3f(-0.5f, 0.5f, 0);
		HULL_POINTS[j++] = new Vector3f(-0.5f, 0.5f, 0.625f);
		HULL_POINTS[j++] = new Vector3f(-0.375f, 0.5f, 0.625f);
		HULL_POINTS[j++] = new Vector3f(-0.375f, 0.5f, 0);
		
		// left inner wall at end of bend
		HULL_POINTS[j++] = new Vector3f(-0.5f, 0.375f, 0.125f);
		HULL_POINTS[j++] = new Vector3f(-0.375f, 0.375f, 0.125f);
		HULL_POINTS[j++] = new Vector3f(-0.375f, 0.375f, 0.625f);
		HULL_POINTS[j++] = new Vector3f(-0.5f, 0.375f, 0.625f);
		
		// left wall top at end of bend
		HULL_POINTS[j++] = new Vector3f(-0.5f, 0.375f, 0.625f);
		HULL_POINTS[j++] = new Vector3f(-0.375f, 0.375f, 0.625f);
		HULL_POINTS[j++] = new Vector3f(-0.375f, 0.5f, 0.625f);
		HULL_POINTS[j++] = new Vector3f(-0.5f, 0.5f, 0.625f);
		
		double rad = 0;
		for (int i = 0; i < NUM_SEGMENTS; i++) {
			// floor quad
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.001, rad, -0.375f),
					getBendXYCoordY(0.001, rad, -0.375f), 0);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.001, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.375f),
					getBendXYCoordY(0.001, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.375f), 0);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.875f, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.375f),
					getBendXYCoordY(0.875f, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.375f), 0);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.875f, rad, -0.375f),
					getBendXYCoordY(0.875f, rad, -0.375f), 0);
			
			// quad just below conveyor
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.125, rad, -0.5f),
					getBendXYCoordY(0.125, rad, -0.5f), 0.125f);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.875, rad, -0.5f),
					getBendXYCoordY(0.875, rad, -0.5f), 0.125f);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.875, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.5f),
					getBendXYCoordY(0.875, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.5f), 0.125f);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.125, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.5f),
					getBendXYCoordY(0.125, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.5f), 0.125f);
			
			// left inner wall
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.125, rad, -0.5f),
					getBendXYCoordY(0.125, rad, -0.5f), 0.125f);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.125, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.5f),
					getBendXYCoordY(0.125, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.5f), 0.125f);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.125, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.5f),
					getBendXYCoordY(0.125, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.5f), 0.625f);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.125, rad, -0.5f),
					getBendXYCoordY(0.125, rad, -0.5f), 0.625f);
			
			// left wall top
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.001, rad, -0.5f),
					getBendXYCoordY(0.001, rad, -0.5f), 0.625f);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.125, rad, -0.5f),
					getBendXYCoordY(0.125, rad, -0.5f), 0.625f);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.125, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.5f),
					getBendXYCoordY(0.125, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.5f), 0.625f);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.001, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.5f),
					getBendXYCoordY(0.001, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.5f), 0.625f);
			if (i == 0 || i == NUM_SEGMENTS - 1) {
				Vector3f tmp = HULL_POINTS[j - 1];
				HULL_POINTS[j - 1] = HULL_POINTS[j - 3];
				HULL_POINTS[j - 3] = tmp;
			}
			
			// right inner wall
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.75, rad, -0.375f),
					getBendXYCoordY(0.75, rad, -0.375f), 0.125f);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.75, rad, -0.375f),
					getBendXYCoordY(0.75, rad, -0.375f), 0.625f);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.75, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.375f),
					getBendXYCoordY(0.75, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.375f), 0.625f);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.75, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.375f),
					getBendXYCoordY(0.75, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.375f), 0.125f);
			
			// right outer wall
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.875, rad, -0.375f),
					getBendXYCoordY(0.875, rad, -0.375f), 0);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.875, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.375f),
					getBendXYCoordY(0.875, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.375f), 0);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.875, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.375f),
					getBendXYCoordY(0.875, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.375f), 0.625f);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.875, rad, -0.375f),
					getBendXYCoordY(0.875, rad, -0.375f), 0.625f);
			
			// right wall top
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.75, rad, -0.375f),
					getBendXYCoordY(0.75, rad, -0.375f), 0.625f);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.875, rad, -0.375f),
					getBendXYCoordY(0.875, rad, -0.375f), 0.625f);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.875, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.375f),
					getBendXYCoordY(0.875, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.375f), 0.625f);
			HULL_POINTS[j++] = new Vector3f(getBendXYCoordX(0.75, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.375f),
					getBendXYCoordY(0.75, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.375f), 0.625f);
			
			rad += (Math.PI / (2 * NUM_SEGMENTS));
		}
		
		// roof hull
		j = 0;
		
		// quad left of conveyor at start of bend
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0.625f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.375f, -0.5f, 0.625f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.375f, -0.5f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0.75f);
		
		// quad right of conveyor at start of bend
		HULL_ROOF_POINTS[j++] = new Vector3f(0.375f, -0.5f, 0.625f);
		HULL_ROOF_POINTS[j++] = new Vector3f(0.5f, -0.5f, 0.625f);
		HULL_ROOF_POINTS[j++] = new Vector3f(0.5f, -0.5f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(0.375f, -0.5f, 0.75f);
		
		// back quad right of conveyor at start of bend
		HULL_ROOF_POINTS[j++] = new Vector3f(0.375f, -0.375f, 0.625f);
		HULL_ROOF_POINTS[j++] = new Vector3f(0.375f, -0.375f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(0.5f, -0.375f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(0.5f, -0.375f, 0.625f);
		
		// quad left of conveyor at end of bend
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0.625f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, -0.375f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, -0.375f, 0.625f);
		
		// quad right of conveyor at end of bend
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, 0.375f, 0.625f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, 0.375f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, 0.5f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, 0.5f, 0.625f);
		
		// back quad right of conveyor at end of bend
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.375f, 0.375f, 0.625f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.375f, 0.5f, 0.625f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.375f, 0.5f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.375f, 0.375f, 0.75f);
		
		// right outer wall at start of bend
		HULL_ROOF_POINTS[j++] = new Vector3f(0.5f, -0.5f, 0.625f);
		HULL_ROOF_POINTS[j++] = new Vector3f(0.5f, -0.375f, 0.625f);
		HULL_ROOF_POINTS[j++] = new Vector3f(0.5f, -0.375f, 0.875f);
		HULL_ROOF_POINTS[j++] = new Vector3f(0.5f, -0.5f, 0.875f);
		
		// right inner wall at start of bend
		HULL_ROOF_POINTS[j++] = new Vector3f(0.375f, -0.5f, 0.625f);
		HULL_ROOF_POINTS[j++] = new Vector3f(0.375f, -0.5f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(0.375f, -0.375f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(0.375f, -0.375f, 0.625f);
		
		// right outer wall at end of bend
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, 0.5f, 0.625f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, 0.5f, 0.875f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.375f, 0.5f, 0.875f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.375f, 0.5f, 0.625f);
		
		// right inner wall at end of bend
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, 0.375f, 0.625f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.375f, 0.375f, 0.625f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.375f, 0.375f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, 0.375f, 0.75f);
		
		// quad above conveyor at start of bend
		HULL_ROOF_POINTS[j++] = new Vector3f(0.5f, -0.5f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(0.5f, -0.5f, 0.875f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0.875f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0.75f);
		
		// quad above conveyor top ceiling at start of bend
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0.875f);
		HULL_ROOF_POINTS[j++] = new Vector3f(0.5f, -0.5f, 0.875f);
		HULL_ROOF_POINTS[j++] = new Vector3f(0.5f, -0.375f, 0.875f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, -0.375f, 0.875f);
		
		// quad above conveyor bottom ceiling at start of bend
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, -0.375f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(0.5f, -0.375f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(0.5f, -0.5f, 0.75f);
		
		// quad above conveyor at end of bend
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, 0.5f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0.875f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, 0.5f, 0.875f);
		
		// quad above conveyor top ceiling at end of bend
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0.875f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.375f, -0.5f, 0.875f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.375f, 0.5f, 0.875f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, 0.5f, 0.875f);
		
		// quad above conveyor bottom ceiling at end of bend
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, -0.5f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.375f, -0.5f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.375f, 0.5f, 0.75f);
		HULL_ROOF_POINTS[j++] = new Vector3f(-0.5f, 0.5f, 0.75f);
		
		rad = 0;
		for (int i = 0; i < NUM_SEGMENTS; i++) {
			// left inner wall
			HULL_ROOF_POINTS[j++] = new Vector3f(getBendXYCoordX(0.125, rad, -0.5f),
					getBendXYCoordY(0.125, rad, -0.5f), 0.625f);
			HULL_ROOF_POINTS[j++] = new Vector3f(getBendXYCoordX(0.125, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.5f),
					getBendXYCoordY(0.125, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.5f), 0.625f);
			HULL_ROOF_POINTS[j++] = new Vector3f(getBendXYCoordX(0.125, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.5f),
					getBendXYCoordY(0.125, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.5f), 0.75f);
			HULL_ROOF_POINTS[j++] = new Vector3f(getBendXYCoordX(0.125, rad, -0.5f),
					getBendXYCoordY(0.125, rad, -0.5f), 0.75f);
			
			// right outer wall
			HULL_ROOF_POINTS[j++] = new Vector3f(getBendXYCoordX(0.875, rad, -0.375f),
					getBendXYCoordY(0.875, rad, -0.375f), 0.75f);
			HULL_ROOF_POINTS[j++] = new Vector3f(getBendXYCoordX(0.875, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.375f),
					getBendXYCoordY(0.875, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.375f), 0.75f);
			HULL_ROOF_POINTS[j++] = new Vector3f(getBendXYCoordX(0.875, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.375f),
					getBendXYCoordY(0.875, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.375f), 0.875f);
			HULL_ROOF_POINTS[j++] = new Vector3f(getBendXYCoordX(0.875, rad, -0.375f),
					getBendXYCoordY(0.875, rad, -0.375f), 0.875f);
			
			// ceiling top
			HULL_ROOF_POINTS[j++] = new Vector3f(getBendXYCoordX(0.001, rad, -0.375f),
					getBendXYCoordY(0.001, rad, -0.375f), 0.875f);
			HULL_ROOF_POINTS[j++] = new Vector3f(getBendXYCoordX(0.875, rad, -0.375f),
					getBendXYCoordY(0.875, rad, -0.375f), 0.875f);
			HULL_ROOF_POINTS[j++] = new Vector3f(getBendXYCoordX(0.875, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.375f),
					getBendXYCoordY(0.875, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.375f), 0.875f);
			HULL_ROOF_POINTS[j++] = new Vector3f(getBendXYCoordX(0.001, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.375f),
					getBendXYCoordY(0.001, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.375f), 0.875f);
			
			// ceiling bottom
			HULL_ROOF_POINTS[j++] = new Vector3f(getBendXYCoordX(0.001, rad, -0.375f),
					getBendXYCoordY(0.001, rad, -0.375f), 0.75f);
			HULL_ROOF_POINTS[j++] = new Vector3f(getBendXYCoordX(0.001, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.375f),
					getBendXYCoordY(0.001, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.375f), 0.75f);
			HULL_ROOF_POINTS[j++] = new Vector3f(getBendXYCoordX(0.875, rad +
					(Math.PI / (2 * NUM_SEGMENTS)), -0.375f),
					getBendXYCoordY(0.875, rad + (Math.PI / (2 * NUM_SEGMENTS)),
							-0.375f), 0.75f);
			HULL_ROOF_POINTS[j++] = new Vector3f(getBendXYCoordX(0.875, rad, -0.375f),
					getBendXYCoordY(0.875, rad, -0.375f), 0.75f);
			
			rad += (Math.PI / (2 * NUM_SEGMENTS));
		}
		
		applyZFightingCorrection(HULL_POINTS);
		applyZFightingCorrection(HULL_ROOF_POINTS);
	}
	
	/**
	 * Construct a new conveyor block at given position and with given orientation.
	 * The constructed block will be deletable.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @param orientation The orientation.
	 */
	public BendLeftConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation, ConveyorBlockType.BEND_LEFT);
	}

	/**
	 * Construct a new conveyor block at given position and with given orientation.
	 * The constructed block will be deletable depending on the parameter.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @param orientation The orientation.
	 * @param deletable If the block can be deleted or not.
	 */
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
	public Vector3f[] getHullPoints() {
		return HULL_POINTS;
	}
	
	@Override
	public Vector3f[] getHullRoofPoints() {
		return HULL_ROOF_POINTS;
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
