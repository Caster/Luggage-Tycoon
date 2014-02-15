package accg.camera;

import static org.lwjgl.util.glu.GLU.gluLookAt;

import org.lwjgl.util.vector.Vector3f;

/**
 * This class contains the camera position routines.
 */
public class Camera {
	
	// Private constants
	private static final Vector3f INITIAL_CAM_POS = new Vector3f(10, (float) (Math.PI / 4), 0);
	private static final Vector3f INITIAL_CAM_LOOK_POS = new Vector3f(0, 0, 0);
	private static final Vector3f INITIAL_CAM_UP = new Vector3f(0, 0, 1);
	
	/** How much the camera moves in each direction by default. */
	private static final float CAM_MOVE = 0.1f;
	/** How much the camera turns in each direction by default. */
	private static final float CAM_TURN = (float) (Math.PI / 100);
	
	/**
	 * Factor for multiplying mouse deltas with.
	 * 
	 * <p>This basically controls how fast the camera turns and moves in
	 * response to mouse events.</p>
	 */
	private static final float MOUSE_FACTOR = 0.7f;
	
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

	/**
	 * Turn the camera left a bit.
	 * 
	 * @see #turnLeft(float)
	 */
	public void turnLeft() {
		turnLeft(1.0f);
	}
	
	/**
	 * Turn the camera left a bit, where the "default bit" is multiplied
	 * by the given factor. This can be used for faster or slower turning.
	 * 
	 * @param amount The factor for multiplication.
	 * @see #turnLeft()
	 */
	public void turnLeft(float amount) {
		camSpherical.z = modulo(camSpherical.z - (CAM_TURN * amount));
	}

	/**
	 * Turn the camera right a bit.
	 * 
	 * @see #turnRight(float)
	 */
	public void turnRight() {
		turnRight(1.0f);
	}
	
	/**
	 * Turn the camera right a bit, where the "default bit" is multiplied
	 * by the given factor. This can be used for faster or slower turning.
	 * 
	 * @param amount The factor for multiplication.
	 * @see #turnRight()
	 */
	public void turnRight(float amount) {
		camSpherical.z = modulo(camSpherical.z + (CAM_TURN * amount));
	}

	/**
	 * Turn the camera downwards a bit.
	 * 
	 * @see #turnDown(float)
	 */
	public void turnDown() {
		turnDown(1.0f);
	}
	
	/**
	 * Turn the camera downwards a bit, where the "default bit" is multiplied
	 * by the given factor. This can be used for faster or slower turning.
	 * 
	 * @param amount The factor for multiplication.
	 * @see #turnDown()
	 */
	public void turnDown(float amount) {
		camSpherical.y = clamp(camSpherical.y - (CAM_TURN * amount));
	}

	/**
	 * Turn the camera upwards a bit.
	 * 
	 * @see #turnUp(float)
	 */
	public void turnUp() {
		turnUp(1.0f);
	}
	
	/**
	 * Turn the camera upwards a bit, where the "default bit" is multiplied
	 * by the given factor. This can be used for faster or slower turning.
	 * 
	 * @param amount The factor for multiplication.
	 * @see #turnUp()
	 */
	public void turnUp(float amount) {
		camSpherical.y = clamp(camSpherical.y + (CAM_TURN * amount));
	}
	
	public void moveForward() {
		moveForward(camSpherical.x / INITIAL_CAM_POS.x);
	}
	
	public void moveForward(float amount) {
		moveForwardBackward(-CAM_MOVE * amount);
	}

	public void moveBackward() {
		moveBackward(camSpherical.x / INITIAL_CAM_POS.x);
	}
	
	public void moveBackward(float amount) {
		moveForwardBackward(CAM_MOVE * amount);
	}
	
	private void moveForwardBackward(float scale) {
		Vector3f addVector = sphericalToCartesian(camSpherical, new Vector3f(0, 0, 0));
		addVector.z = 0;
		addVector.normalise().scale(scale);
		Vector3f.add(camLookPos, addVector, camLookPos);
	}
	
	public void moveLeft() {
		moveLeft(camSpherical.x / INITIAL_CAM_POS.x);
	}
	
	public void moveLeft(float amount) {
		moveLeftRight(CAM_MOVE * amount);
	}
	
	public void moveRight() {
		moveRight(camSpherical.x / INITIAL_CAM_POS.x);
	}
	
	public void moveRight(float amount) {
		moveLeftRight(-CAM_MOVE * amount);
	}
	
	private void moveLeftRight(float scale) {
		Vector3f addVector = sphericalToCartesian(camSpherical, new Vector3f(0, 0, 0));
		Vector3f.cross(addVector, camUpPos, addVector);
		addVector.normalise().scale(scale);
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
	 * Clamp a given value between 0.1 and PI - 0.1.
	 * 
	 * @param value The value to be clamped.
	 * @return A clamped value.
	 * @see #clamp(float, float, float)
	 */
	private float clamp(float value) {
		return clamp(value, 0.1f, (float) (Math.PI - 0.1));
	}
	
	/**
	 * Clamp a given value between a given minimum and maximum value.
	 * Clamping means that when the given value is less than the given
	 * minimum, this minimum is returned. If the value is greater than
	 * the maximum, this maximum is returned. Otherwise, the value is
	 * unchanged.
	 * 
	 * <p>This function does not change any of its parameters.</p>
	 * 
	 * @param value The value to be clamped.
	 * @param min The minimum value for clamping.
	 * @param max The maximum value for clamping.
	 * @return A value between {@code min} and {@code max}.
	 */
	private float clamp(float value, float min, float max) {
		if (value <= min)  return min;
		if (value >= max)  return max;
		return value;
	}
	
	private float modulo(float value) {
		return modulo(value, 0.0f, (float) (2 * Math.PI));
	}
	
	private float modulo(float value, float min, float max) {
		return min + ((value - min) % (max - min));
	}
	
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
			    (float) (spherical.x * Math.sin(spherical.y) * Math.sin(spherical.z)) + origin.y,
						(float) (spherical.x * Math.cos(spherical.y)) + origin.z
		);
		return result;
	}

	/**
	 * Move the camera left, right, backward and/or forward in response to a mouse movement.
	 * 
	 * @param dx The number of pixels the mouse moved in horizontal direction.
	 * @param dy The number of pixels the mouse moved in vertical direction.
	 */
	public void moveByMouse(int dx, int dy) {
		if (dx < 0)  moveRight(MOUSE_FACTOR * -dx * (camSpherical.x / INITIAL_CAM_POS.x));
		if (dx > 0)  moveLeft(MOUSE_FACTOR * dx * (camSpherical.x / INITIAL_CAM_POS.x));
		if (dy < 0)  moveForward(MOUSE_FACTOR * -dy * (camSpherical.x / INITIAL_CAM_POS.x));
		if (dy > 0)  moveBackward(MOUSE_FACTOR * dy * (camSpherical.x / INITIAL_CAM_POS.x));
	}
	
	/**
	 * Turn the camera left, right, down and/or up in response to a mouse movement.
	 * 
	 * @param dx The number of pixels the mouse moved in horizontal direction.
	 * @param dy The number of pixels the mouse moved in vertical direction.
	 */
	public void turnByMouse(int dx, int dy) {
		if (dx < 0)  turnRight(MOUSE_FACTOR * -dx);
		if (dx > 0)  turnLeft(MOUSE_FACTOR * dx);
		if (dy < 0)  turnDown(MOUSE_FACTOR * -dy);
		if (dy > 0)  turnUp(MOUSE_FACTOR * dy);
	}
}
