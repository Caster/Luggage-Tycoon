package accg.camera;

import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	// Private constants
	private static final Vector3f INITIAL_CAM_POS = new Vector3f (20, 20, 10);
	private static final Vector3f INITIAL_CAM_LOOK_POS = new Vector3f (0, 0, 0);
	private static final Vector3f INITIAL_CAM_UP = new Vector3f (0, 0, 1);
	
	// Private variables
	private Vector3f camPos = new Vector3f(INITIAL_CAM_POS);
	private Vector3f camLookPosSpherical = new Vector3f(INITIAL_CAM_LOOK_POS);
	private Vector3f camUpPos = new Vector3f(INITIAL_CAM_UP);

	// Public methods
	
	/**
	 * Call lookAt method of glu so that camera is repositioned.
	 */
	public void setLookAt() {
		// Use saved position and viewing direction
		Vector3f camLookPos = sphericalToCartesian(camLookPosSpherical, camPos);
		gluLookAt(camPos.x, camPos.y, camPos.z,
					camLookPos.x, camLookPos.y, camLookPos.z,
					camUpPos.x, camUpPos.y, camUpPos.z);
	}
	
	public void setLookAt(Vector3f camPos, Vector3f viewPos) {
		gluLookAt(camPos.x, camPos.y, camPos.z,
					viewPos.x, viewPos.y, viewPos.z,
					0.0f, 1.0f, 0.0f);
	}
	
	public void setLookAt(Vector3f camPos, Vector3f viewPos, Vector3f upPos) {
		gluLookAt(camPos.x, camPos.y, camPos.z,
					viewPos.x, viewPos.y, viewPos.z,
					upPos.x, upPos.y, upPos.z);
	}

	public void turnLeft() {
		camLookPosSpherical.z = (float) (camLookPosSpherical.z - (float) (Math.PI / 100) % (2 * Math.PI));
	}

	public void turnRight() {
		camLookPosSpherical.z = (float) (camLookPosSpherical.z + (float) (Math.PI / 100) % (2 * Math.PI));
	}

	public void turnDown() {
		camLookPosSpherical.y = (float) (camLookPosSpherical.y - (float) (Math.PI / 100) % (2 * Math.PI));
	}

	public void turnUp() {
		camLookPosSpherical.y = (float) (camLookPosSpherical.y + (float) (Math.PI / 100) % (2 * Math.PI));
	}
	
	public void moveForward() {
		camPos = sphericalToCartesian(camLookPosSpherical, camPos);
	}

	public void moveBackward() {
		Vector3f lookPos = sphericalToCartesian(camLookPosSpherical, new Vector3f(0, 0, 0));
		Vector3f.add(camPos, (Vector3f) lookPos.scale(-1.0f), camPos);
	}
	
	public void moveLeft() {
		Vector3f viewVector = new Vector3f();
		Vector3f.sub(sphericalToCartesian(camLookPosSpherical, camPos), camPos, viewVector);
		Vector3f sideVector = new Vector3f();
		Vector3f.cross(viewVector, camUpPos, sideVector);
		Vector3f.add(camPos, (Vector3f) sideVector.normalise().scale(-1.0f), camPos);
	}
	
	public void moveRight() {
		Vector3f viewVector = new Vector3f();
		Vector3f.sub(sphericalToCartesian(camLookPosSpherical, camPos), camPos, viewVector);
		Vector3f sideVector = new Vector3f();
		Vector3f.cross(viewVector, camUpPos, sideVector);
		Vector3f.add(camPos, (Vector3f) sideVector.normalise(), camPos);
	}
	
	public void moveUp() {
		Vector3f.add(camPos, (Vector3f) camUpPos.normalise(), camPos);
	}
	
	public void moveDown() {
		Vector3f.add(camPos, (Vector3f) camUpPos.normalise().scale(-1f), camPos);
	}
	
	// Private methods
	/**
	 * Convert sperhical coordinates to cartesian, given spherical coordinates and the absolute cartesian
	 * coordinates of the origin.
	 * 
	 * @param spherical Spherical coordinates in the form {radius, first angle, second angle}
	 *                  with the angles in radials.
	 * @return A vector in cartesian coordinates
	 */
	private Vector3f sphericalToCartesian(Vector3f spherical, Vector3f origin) {
		Vector3f result = new Vector3f (
				(float) (spherical.x * Math.sin(spherical.y) * Math.cos(spherical.z)) + origin.x,
				(float) (spherical.x * Math.cos(spherical.y)) + origin.y,
			    (float) (spherical.x * Math.sin(spherical.y) * Math.sin(spherical.z) + origin.z)
		);
		return result;
	}
}
