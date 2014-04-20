package accg.simulation;

import java.util.HashMap;

import javax.vecmath.Vector3f;

import accg.State;
import accg.objects.Luggage;
import accg.objects.Orientation;
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
	 * @param s State, used to look-up neighbors.
	 * @param cb The conveyor belt block of which the shape must be known.
	 * @return A shape that matches the type (and orientation) of the given
	 *         conveyor belt block.
	 */
	public static CollisionShape getConveyorShape(State s, ConveyorBlock cb) {
		// make sure the look-up table is ready
		if (conveyorBlockShapeMap == null) {
			conveyorBlockShapeMap = new HashMap<>();
		}
		
		// check the look-up table first
		ConveyorBlockInfo cbInfo = new ConveyorBlockInfo(s, cb);
		if (conveyorBlockShapeMap.containsKey(cbInfo)) {
			return conveyorBlockShapeMap.get(cbInfo);
		}
		
		// we did not create the shape earlier, do it now
		ObjectArrayList<Vector3f> points = new ObjectArrayList<>();
		points.addAll(cb.getTopCoordinatesLeft(cbInfo.neighbor1,
				cbInfo.neighbor2));
		points.addAll(cb.getTopCoordinatesRight(cbInfo.neighbor1,
				cbInfo.neighbor2));
		points.addAll(cb.getBottomCoordinatesLeft(cbInfo.neighbor1,
				cbInfo.neighbor2));
		points.addAll(cb.getBottomCoordinatesRight(cbInfo.neighbor1,
				cbInfo.neighbor2));
		Utils.rotatePoints(cb.getOrientation(), points);
		CollisionShape result = new ConvexHullShape(points);
		
		// store the result in the look-up table for future reference
		conveyorBlockShapeMap.put(cbInfo, result);
		
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
	
	/**
	 * Wrapper class that has some information defining a conveyor block type.
	 * This class is used to index the HashMap used for caching.
	 */
	private static class ConveyorBlockInfo {
		/**
		 * Type of the represented conveyor block.
		 */
		public ConveyorBlockType type;
		/**
		 * Orientation of the represented conveyor block.
		 */
		public Orientation orientation;
		/**
		 * If the block has a neighbor on one side.
		 * (In the direction of its orientation.)
		 */
		public boolean hasNeighbor1;
		/**
		 * If the block has a neigbor, this is stored here.
		 * See {@link #hasNeighbor1}. Otherwise, this is {@code null}.
		 */
		public ConveyorBlock neighbor1;
		/**
		 * If the block has a neighbor on the other side.
		 * (Opposite to the direction of its orientation.)
		 */
		public boolean hasNeighbor2;
		/**
		 * If the block has a neigbor, this is stored here.
		 * See {@link #hasNeighbor2}. Otherwise, this is {@code null}.
		 */
		public ConveyorBlock neighbor2;
		
		/**
		 * Construct an object holding information about the given block.
		 * 
		 * @param s State, used to look-up neighbors.
		 * @param cb Block to construct an information object from.
		 */
		public ConveyorBlockInfo(State s, ConveyorBlock cb) {
			this.type = cb.getConveyorBlockType();
			this.orientation = cb.getOrientation();
			ConveyorBlock[] neighbors = s.world.getNeighbors(cb);
			this.neighbor1 = neighbors[0];
			this.neighbor2 = neighbors[1];
			this.hasNeighbor1 = (neighbor1 != null);
			this.hasNeighbor2 = (neighbor2 != null);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (hasNeighbor1 ? 1231 : 1237);
			result = prime * result + (hasNeighbor2 ? 1231 : 1237);
			result = prime * result
					+ ((orientation == null) ? 0 : orientation.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ConveyorBlockInfo other = (ConveyorBlockInfo) obj;
			if (hasNeighbor1 != other.hasNeighbor1)
				return false;
			if (hasNeighbor2 != other.hasNeighbor2)
				return false;
			if (orientation != other.orientation)
				return false;
			if (type != other.type)
				return false;
			return true;
		}
	}
	
	/** The shape of a piece of luggage. */
	private static CollisionShape luggageShape;
	/** Inertia vector of a piece of luggage. */ 
	private static Vector3f luggageShapeInertia;
	/** Map containing shapes of conveyor blocks. */
	private static HashMap<ConveyorBlockInfo, CollisionShape> conveyorBlockShapeMap;
}
