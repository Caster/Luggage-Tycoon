package accg.simulation;

import accg.State;
import accg.gui.MainGUI;
import accg.objects.Luggage;
import accg.objects.blocks.LeaveBlock;

import com.bulletphysics.ContactProcessedCallback;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.dynamics.RigidBody;

/**
 * Representation of a callback that is fired when there is a new contact.
 * The callback ensures that luggage that touches the ground is deleted.
 */
public class SimulationCallback extends ContactProcessedCallback {

	/**
	 * Construct a new callback for contacts.
	 * 
	 * @param state State, used to access world in which objects are drawn.
	 * @param contactProcessedCallback Current callback.
	 */
	public SimulationCallback(State state,
			ContactProcessedCallback contactProcessedCallback) {
		this.processedCallback = contactProcessedCallback;
		this.state = state;
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
						state.world.luggage.remove((Luggage) rb1info.getUserPointer());
						state.world.incrementLostLuggageCount();
						MainGUI.updateStatusBarInfo();
					} else if (rb0info.getBodyType() == SimulationBodyType.LUGGAGE &&
							rb1info.getBodyType() == SimulationBodyType.FLOOR) {
						state.world.luggage.remove((Luggage) rb0info.getUserPointer());
						state.world.incrementLostLuggageCount();
						MainGUI.updateStatusBarInfo();
					} else if (rb0info.getBodyType() == SimulationBodyType.LEAVE_BLOCK &&
							rb1info.getBodyType() == SimulationBodyType.LUGGAGE) {
						LeaveBlock lb = (LeaveBlock) rb0info.getUserPointer();
						Luggage lug = (Luggage) rb1info.getUserPointer();
						if (lb.getAcceptColors() == null ||
								lb.getAcceptColors().contains(lug.getColor())) {
							state.world.luggage.remove((Luggage) rb1info.getUserPointer());
							lb.incrementArrivedLuggageCount();
							MainGUI.updateStatusBarInfo();
						}
					} else if (rb0info.getBodyType() == SimulationBodyType.LUGGAGE &&
							rb1info.getBodyType() == SimulationBodyType.LEAVE_BLOCK) {
						LeaveBlock lb = (LeaveBlock) rb1info.getUserPointer();
						Luggage lug = (Luggage) rb0info.getUserPointer();
						if (lb.getAcceptColors() == null ||
								lb.getAcceptColors().contains(lug.getColor())) {
							state.world.luggage.remove((Luggage) rb1info.getUserPointer());
							lb.incrementArrivedLuggageCount();
							MainGUI.updateStatusBarInfo();
						}
					}
				}
			}
		}
		return true;
	}

	/** Reference to previous ContactProcessedCallback, should be called by us. */
	private ContactProcessedCallback processedCallback;
	/** State of program, used to access visual world. */
	private State state;
}
