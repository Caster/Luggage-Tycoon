package accg.simulation;

import javax.vecmath.Matrix4f;

import accg.objects.Luggage;

import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

/**
 * A LuggageMotionState is a special {@link MotionState} that ensures that the
 * position of a piece of {@link Luggage} is updated correctly whenever the
 * {@link MotionState} is updated. This is more efficient that updating the
 * position of every piece of luggage after every simulation step, because a
 * piece of luggage may not move at all, in which case no update will be
 * performed. It is recommended by Bullet to do it this way, I think.
 */
public class LuggageMotionState extends MotionState {

	/**
	 * Construct a new {@link MotionState} that will use the position of the
	 * given piece of luggage as an initial position and will update the
	 * position of the given {@link Luggage} every time it changes.
	 * 
	 * @param luggage Luggage to track/update.
	 */
	public LuggageMotionState(Luggage luggage) {
		this.luggage = luggage;
		this.lugPos = new Transform();
		this.lugPos.set(luggage.transform);
	}
	
	@Override
	public Transform getWorldTransform(Transform out) {
		out.set(this.lugPos);		
		return out;
	}
	
	@Override
	public void setWorldTransform(Transform worldTrans) {
		this.lugPos = worldTrans;
		Matrix4f mat = new Matrix4f();
		this.lugPos.getMatrix(mat);
		this.luggage.transform = mat;
	}

	private Luggage luggage;
	private Transform lugPos;
}
