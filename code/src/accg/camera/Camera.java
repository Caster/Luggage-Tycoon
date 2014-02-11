package accg.camera;

import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;

/**
 * This class contains the camera position routines.
 */
public class Camera {
	
	// Private constants
	private static final Vector3f INITIAL_CAM_POS = new Vector3f(10, (float) (Math.PI / 3), (float) (Math.PI / 3));
	private static final Vector3f INITIAL_CAM_LOOK_POS = new Vector3f(0, 0, 0);
	private static final Vector3f INITIAL_CAM_UP = new Vector3f(0, 0, 1);
	
	/**
	 * The camera position relative to {@link #camLookPos}, in spherical coordinates.
	 */
	private Vector3f camSpherical = new Vector3f(INITIAL_CAM_POS);
	
	/**
	 * The position the camera is looking at.
	 */
	private Vector3f camLookPos = new Vector3f(INITIAL_CAM_LOOK_POS);
	
	/**
	 * The camera up vector.
	 */
	private Vector3f camUpPos = new Vector3f(INITIAL_CAM_UP);

	// Public methods
	
	/**
	 * Call lookAt method of glu so that camera is repositioned.
	 */
	public void setLookAt() {
		// Use saved position and viewing direction
		Vector3f camPos = sphericalToCartesian(camSpherical, camLookPos);
		//System.out.println("camPos = " + camPos);
		//System.out.println("camLookPos = " + camLookPos);
		//System.out.println("camUpPos = " + camUpPos);
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
		camSpherical.z = (float) (camSpherical.z - (float) (Math.PI / 100) % (2 * Math.PI));
	}

	public void turnRight() {
		camSpherical.z = (float) (camSpherical.z + (float) (Math.PI / 100) % (2 * Math.PI));
	}

	public void turnDown() {
		camSpherical.y = (float) (camSpherical.y - (float) (Math.PI / 100) % (2 * Math.PI));
	}

	public void turnUp() {
		camSpherical.y = (float) (camSpherical.y + (float) (Math.PI / 100) % (2 * Math.PI));
	}
	
	public void moveForward() {
		Vector3f addVector = sphericalToCartesian(camSpherical, new Vector3f(0, 0, 0));
		addVector.z = 0;
		addVector.normalise().scale(-0.1f);
		Vector3f.add(camLookPos, addVector, camLookPos);
	}

	public void moveBackward() {
		Vector3f addVector = sphericalToCartesian(camSpherical, new Vector3f(0, 0, 0));
		addVector.z = 0;
		addVector.normalise().scale(0.1f);
		Vector3f.add(camLookPos, addVector, camLookPos);
	}
	
	public void moveLeft() {
		Vector3f addVector = sphericalToCartesian(camSpherical, new Vector3f(0, 0, 0));
		Vector3f.cross(addVector, camUpPos, addVector);
		addVector.normalise().scale(0.1f);
		Vector3f.add(camLookPos, addVector, camLookPos);
	}
	
	public void moveRight() {
		Vector3f addVector = sphericalToCartesian(camSpherical, new Vector3f(0, 0, 0));
		Vector3f.cross(addVector, camUpPos, addVector);
		addVector.normalise().scale(-0.1f);
		Vector3f.add(camLookPos, addVector, camLookPos);
	}
	
	public void moveUp() {
		camSpherical.x *= 1.1;
	}
	
	public void moveDown() {
		camSpherical.x /= 1.1;
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
