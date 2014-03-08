package accg.simulation;

import java.util.HashMap;

import javax.vecmath.Vector3f;

import accg.objects.Block.Orientation;
import accg.objects.Luggage;
import accg.objects.blocks.ConveyorBlock;
import accg.objects.blocks.ConveyorBlock.ConveyorBlockType;
import accg.utils.Utils;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.ConvexHullShape;
import com.bulletphysics.util.ObjectArrayList;

/**
 * This class provides some static methods to obtain certain JBullet shapes.
 * All shapes provided by this class are initialised in a lazy manner and shared
 * across all instances. That is, whenever a shape has been initialised, a
 * reference to it will be returned in a next similar call.
 */
public class ShapeFactory {

	/**
	 * The shape of the given conveyor belt block.
	 * 
	 * @param cb The conveyor belt block of which the shape must be known.
	 * @return A shape that matches the type (and orientation) of the given
	 *         conveyor belt block.
	 */
	public static CollisionShape getConveyorShape(ConveyorBlock cb) {
		// make sure the look-up table is ready
		if (conveyorBlockShapeMap == null) {
			conveyorBlockShapeMap = new HashMap<>();
		}
		
		// check the look-up table first
		if (conveyorBlockShapeMap.containsKey(cb.getConveyorBlockType())) {
			HashMap<Orientation, CollisionShape> map =
					conveyorBlockShapeMap.get(cb.getConveyorBlockType());
			if (map.containsKey(cb.getOrientation())) {
				return map.get(cb.getOrientation());
			}
		}
		
		// we did not create the shape earlier, do it now
		ObjectArrayList<Vector3f> points = new ObjectArrayList<>();
		points.addAll(cb.getTopCoordinatesLeft());
		points.addAll(cb.getTopCoordinatesRight());
		points.addAll(cb.getBottomCoordinatesLeft());
		points.addAll(cb.getBottomCoordinatesRight());
		Utils.rotatePoints(cb.getOrientation(), points);
		CollisionShape result = new ConvexHullShape(points);
		
		// store the result in the look-up table for future reference
		HashMap<Orientation, CollisionShape> map;
		boolean mustAdd = false;
		if (!conveyorBlockShapeMap.containsKey(cb.getConveyorBlockType())) {
			map = new HashMap<>();
			mustAdd = true;
		} else {
			map = conveyorBlockShapeMap.get(cb.getConveyorBlockType());
		}
		map.put(cb.getOrientation(), result);
		if (mustAdd) {
			conveyorBlockShapeMap.put(cb.getConveyorBlockType(), map);
		}
		
		// return result as well :)
		return result;
	}
	
	/**
	 * The shape of a piece of luggage, in a form JBullet can understand.
	 * It is shared by all pieces of luggage.
	 * 
	 * @return A reference to a shared {@link CollisionShape} that can be used
	 *         to represent a piece of luggage.
	 */
	public static CollisionShape getLuggageShape() {
		if (luggageShape == null) {
			luggageShape = new BoxShape(new Vector3f(0.174f, 0.131f, 0.030f));
		}
		
		return luggageShape;
	}
	
	/**
	 * The inertia vector for the shape of a piece of luggage.
	 * 
	 * @return The inertia vector for the shape of a piece of luggage.
	 * @see #getLuggageShape()
	 */
	public static Vector3f getLuggageShapeInertia() {
		if (luggageShapeInertia == null) {
			luggageShapeInertia = new Vector3f();
			getLuggageShape().calculateLocalInertia(Luggage.WEIGHT, luggageShapeInertia);
		}
		
		return luggageShapeInertia;
	}
	
	/** The shape of a piece of luggage. */
	private static CollisionShape luggageShape;
	/** Inertia vector of a piece of luggage. */ 
	private static Vector3f luggageShapeInertia;
	/** Map containing shapes of conveyor blocks. */
	private static HashMap<ConveyorBlockType,
			HashMap<Orientation, CollisionShape>> conveyorBlockShapeMap;
}
