package accg.camera;

import static accg.utils.GLUtils.*;
import static org.lwjgl.util.glu.GLU.*;

import javax.vecmath.Vector3f;

import accg.State;

/**
 * This class contains the camera position routines.
 */
public class Camera {
	
	/**
	 * Creates a new Camera object.
	 * @param s The state of the program.
	 */
	public Camera(State s) {
		this.state = s;
	}
	
	// Private constants
	private static final Vector3f INITIAL_CAM_POS = new Vector3f(10, (float) (Math.PI / 4), 0);
	private static final Vector3f INITIAL_CAM_LOOK_POS = new Vector3f(0, 0, 0);
	private static final Vector3f INITIAL_CAM_UP = new Vector3f(0, 0, 1);
	private static final Vector3f ZERO_ORIGIN = new Vector3f(0, 0, 0);
	
	/** How much the camera moves in each direction by default. */
	private static final float CAM_MOVE = 0.1f;
	/** How much the camera turns in each direction by default. */
	private static final float CAM_TURN = (float) (Math.PI / 100);
	/** How much the camera is allowed to move outside of the field. */
	private static final float CAM_FIELD_EXTRA_BOUND = 20.0f;
	
	/**
	 * Factor for multiplying mouse deltas with.
	 * 
	 * <p>This basically controls how fast the camera turns and moves in
	 * response to mouse events.</p>
	 */
	private static final float MOUSE_FACTOR = 0.7f;
	
	/** The state of the game. Used to bound the camera movements. */
	private State state;
	
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
	 * Return the viewing direction of the camera as a vector.
	 * This vector will be normalised.
	 * 
	 * @return The viewing direction of the camera as a vector.
	 */
	public Vector3f getCameraViewVector() {
		// where is the camera?
		Vector3f camPos = getCameraPosition();
		// where is it looking at?
		Vector3f camSphericalForward = new Vector3f(camSpherical);
		camSphericalForward.x *= 1.1;
		Vector3f camLookAt = sphericalToCartesian(camSphericalForward, camLookPos);
		// construct a difference vector
		Vector3f result = new Vector3f();
		result.sub(camPos, camLookAt);
		result.normalize();
		return result;
	}
	
	/**
	 * Return the position the camera is at.
	 * 
	 * @return The position the camera is at.
	 */
	public Vector3f getCameraPosition() {
		return sphericalToCartesian(camSpherical, camLookPos);
	}
	
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
	
	/**
	 * Calls <code>gluLookAt</code> with the given camera and view positions,
	 * and up vector (0, 0, 1).
	 * 
	 * @param camPos The camera position.
	 * @param viewPos The view position.
	 */
	public void setLookAt(Vector3f camPos, Vector3f viewPos) {
		gluLookAt(camPos.x, camPos.y, camPos.z,
					viewPos.x, viewPos.y, viewPos.z,
					0.0f, 1.0f, 0.0f);
	}
	
	/**
	 * Calls <code>gluLookAt</code> with the given camera and view positions
	 * and up vector.
	 * 
	 * @param camPos The camera position.
	 * @param viewPos The view position.
	 * @param upPos The up vector.
	 */
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
		turnLeftRight(-CAM_TURN * amount);
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
		turnLeftRight(CAM_TURN * amount);
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
		turnDownUp(-CAM_TURN * amount);
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
		turnDownUp(CAM_TURN * amount);
	}
	
	/**
	 * Moves the camera forwards by a default amount.
	 */
	public void moveForward() {
		moveForward(camSpherical.x / INITIAL_CAM_POS.x);
	}

	/**
	 * Moves the camera forwards by a given amount.
	 * @param amount The distance to move.
	 */
	public void moveForward(float amount) {
		moveForwardBackward(-CAM_MOVE * amount);
	}
	
	/**
	 * Moves the camera backwards by a default amount.
	 */
	public void moveBackward() {
		moveBackward(camSpherical.x / INITIAL_CAM_POS.x);
	}

	/**
	 * Moves the camera backwards by a given amount.
	 * @param amount The distance to move.
	 */
	public void moveBackward(float amount) {
		moveForwardBackward(CAM_MOVE * amount);
	}
	
	/**
	 * Moves the camera to the left by a default amount.
	 */
	public void moveLeft() {
		moveLeft(camSpherical.x / INITIAL_CAM_POS.x);
	}

	/**
	 * Moves the camera to the left by a given amount.
	 * @param amount The distance to move.
	 */
	public void moveLeft(float amount) {
		moveLeftRight(CAM_MOVE * amount);
	}

	/**
	 * Moves the camera to the right by a default amount.
	 */
	public void moveRight() {
		moveRight(camSpherical.x / INITIAL_CAM_POS.x);
	}
	
	/**
	 * Moves the camera to the right by a given amount.
	 * @param amount The distance to move.
	 */
	public void moveRight(float amount) {
		moveLeftRight(-CAM_MOVE * amount);
	}

	/**
	 * Moves the camera upwards by a default amount.
	 */
	public void moveUp() {
		camSpherical.x *= 1.1;
		if (!ensureBounds(camSpherical)) {
			moveDown();
		}
	}
	
	/**
	 * Moves the camera downwards by a default amount.
	 */
	public void moveDown() {
		camSpherical.x /= 1.1;
		if (!ensureBounds(camSpherical)) {
			moveUp();
		}
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
	
	// Private methods	
	/**
	 * Moves the camera forward or backwards with the given distance,
	 * multiplied by the mouse sensitivity constant from the {@link State}.
	 * 
	 * @param scale Length to move. A positive value means moving forward,
	 *              a negative one means moving backwards. It is guaranteed
	 *              that the mouse sensitivity constant with which this value
	 *              will be multiplied is positive and will hence not change
	 *              the sign/direction.
	 */
	private void moveForwardBackward(float scale) {
		Vector3f addVector = sphericalToCartesian(camSpherical, ZERO_ORIGIN);
		addVector.z = 0;
		addVector.normalize();
		addVector.scale(scale * state.mouseSensitivityFactor);
		camLookPos.add(camLookPos, addVector);
		if (!ensureBounds(camSpherical)) {
			camLookPos.sub(camLookPos, addVector);
		}
	}
	
	/**
	 * Moves the camera left or right with the given distance,
	 * multiplied by the mouse sensitivity constant from the {@link State}.
	 * 
	 * @param scale Length to move. A positive value means moving left,
	 *              a negative one means moving right. It is guaranteed
	 *              that the mouse sensitivity constant with which this value
	 *              will be multiplied is positive and will hence not change
	 *              the sign/direction.
	 */
	private void moveLeftRight(float scale) {
		Vector3f addVector = sphericalToCartesian(camSpherical, ZERO_ORIGIN);
		addVector.cross(addVector, camUpPos);
		addVector.normalize();
		addVector.scale(scale * state.mouseSensitivityFactor);
		camLookPos.add(camLookPos, addVector);
		if (!ensureBounds(camSpherical)) {
			camLookPos.sub(camLookPos, addVector);
		}
	}
	
	/**
	 * Turns the camera upwards or downwards with the given angle in radians,
	 * multiplied by the mouse sensitivity constant from the {@link State}.
	 * 
	 * @param amount Angle to turn. A positive value means turning upwards,
	 *               a negative one means turning downwards. It is guaranteed
	 *               that the mouse sensitivity constant with which this value
	 *               will be multiplied is positive and will hence not change
	 *               the sign/direction.
	 */
	private void turnDownUp(float amount) {
		float oldCamY = camSpherical.y;
		camSpherical.y = clamp(camSpherical.y + amount * state.mouseSensitivityFactor);
		if (!ensureBounds(camSpherical)) {
			camSpherical.y = oldCamY;
		}
	}
	
	/**
	 * Turns the camera left or right with the given angle in radians,
	 * multiplied by the mouse sensitivity constant from the {@link State}.
	 * 
	 * @param amount Angle to turn. A positive value means turning right,
	 *               a negative one means turning left. It is guaranteed
	 *               that the mouse sensitivity constant with which this value
	 *               will be multiplied is positive and will hence not change
	 *               the sign/direction.
	 */
	private void turnLeftRight(float amount) {
		float oldCamZ = camSpherical.z;
		camSpherical.z = modulo(camSpherical.z + amount * state.mouseSensitivityFactor);
		if (!ensureBounds(camSpherical)) {
			camSpherical.z = oldCamZ;
		}
	}
	
	/**
	 * Convert sperhical coordinates to cartesian, given spherical coordinates and the absolute cartesian
	 * coordinates of the origin.
	 * 
	 * @param spherical Spherical coordinates in the form {radius, first angle, second angle}
	 *                  with the angles in radials.
	 * @return A vector in cartesian coordinates
	 */
	private static Vector3f sphericalToCartesian(Vector3f spherical, Vector3f origin) {
		Vector3f result = new Vector3f (
				(float) (spherical.x * Math.sin(spherical.y) * Math.cos(spherical.z)) + origin.x,
			    (float) (spherical.x * Math.sin(spherical.y) * Math.sin(spherical.z)) + origin.y,
						(float) (spherical.x * Math.cos(spherical.y)) + origin.z
		);
		return result;
	}

	private boolean ensureBounds(Vector3f newSpherical) {
		Vector3f newCartesian = sphericalToCartesian(newSpherical, camLookPos);
		if (newCartesian.x < -CAM_FIELD_EXTRA_BOUND ||
				newCartesian.x > state.fieldLength + 1 + CAM_FIELD_EXTRA_BOUND ||
				newCartesian.y < -CAM_FIELD_EXTRA_BOUND ||
				newCartesian.y > state.fieldWidth + 1 + CAM_FIELD_EXTRA_BOUND ||
				newCartesian.z < 0.5f ||
				newCartesian.z > state.fieldHeight + CAM_FIELD_EXTRA_BOUND) {
			return false;
		}
			
		return true;
	}
}
