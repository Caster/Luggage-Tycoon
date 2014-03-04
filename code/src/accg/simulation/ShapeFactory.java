package accg.simulation;

import javax.vecmath.Vector3f;

import accg.objects.blocks.ConveyorBlock;

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
	 * @return A shape that matches the type (not orientation) of the given
	 *         conveyor belt block.
	 */
	public static CollisionShape getConveyorShape(ConveyorBlock cb) {
		ObjectArrayList<Vector3f> points = new ObjectArrayList<>();
		points.addAll(cb.getTopCoordinatesLeft());
		points.addAll(cb.getTopCoordinatesRight());
		points.addAll(cb.getBottomCoordinatesLeft());
		points.addAll(cb.getBottomCoordinatesRight());
		return new ConvexHullShape(points);
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
			getLuggageShape().calculateLocalInertia(1, luggageShapeInertia);
		}
		
		return luggageShapeInertia;
	}
	
	private static CollisionShape luggageShape;
	private static Vector3f luggageShapeInertia;
}
