package accg.simulation;

import accg.objects.Luggage;
import accg.objects.World;

import com.bulletphysics.ContactProcessedCallback;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;

public class SimulationCallback extends ContactProcessedCallback {

	/**
	 * Construct a new callback for contacts.
	 * 
	 * @param world Simulation world.
	 * @param contactProcessedCallback Current callback.
	 */
	public SimulationCallback(World visualWorld, DynamicsWorld world,
			ContactProcessedCallback contactProcessedCallback) {
		this.processedCallback = contactProcessedCallback;
		this.visWorld = visualWorld;
		this.simWorld = world;
	}
	
	@Override
	public boolean contactProcessed(ManifoldPoint cp, Object body0, Object body1) {
		if (processedCallback == null ||
				!processedCallback.contactProcessed(cp, body0, body1)) {
			if (body0 instanceof RigidBody && body1 instanceof RigidBody) {
				RigidBody rb0 = (RigidBody) body0;
				RigidBody rb1 = (RigidBody) body1;
				if (rb0.getUserPointer() instanceof SimulationBodyInfo &&
						rb1.getUserPointer() instanceof SimulationBodyInfo) {
					SimulationBodyInfo rb0info = (SimulationBodyInfo) rb0.getUserPointer();
					SimulationBodyInfo rb1info = (SimulationBodyInfo) rb1.getUserPointer();
					if (rb0info.getBodyType() == SimulationBodyType.FLOOR &&
							rb1info.getBodyType() == SimulationBodyType.LUGGAGE) {
						simWorld.removeRigidBody(rb1);
						visWorld.remove((Luggage) rb1info.getUserPointer());
					} else if (rb0info.getBodyType() == SimulationBodyType.LUGGAGE &&
							rb1info.getBodyType() == SimulationBodyType.FLOOR) {
						simWorld.removeRigidBody(rb0);
						visWorld.remove((Luggage) rb0info.getUserPointer());
					}
				}
			}
		}
		return true;
	}

	/** Reference to previous ContactProcessedCallback, should be called by us. */
	private ContactProcessedCallback processedCallback;
	/** World in which objects are drawn. */
	private World visWorld;
	/** Reference to simulation world. */
	private DynamicsWorld simWorld;
}
