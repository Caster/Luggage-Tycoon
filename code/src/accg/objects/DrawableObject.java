package accg.objects;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import accg.State;

/**
 * Any object that can be drawn in the 3D world.
 */
public abstract class DrawableObject {
	
	/** List of listeners. */
	protected ArrayList<DrawableObjectListener> listeners = new ArrayList<>();
	
	/**
	 * Attach the given listener to this object.
	 * 
	 * @param dol The listener to attach to this object.
	 */
	public void addListener(DrawableObjectListener dol) {
		listeners.add(dol);
	}
	
	/**
	 * Draws the object. You should ensure to keep the OpenGL transform the same.
	 * 
	 * @param s The {@link State} object that indicates the state of the
	 * program.
	 */
	public abstract void draw(State s);
	
	/**
	 * Change the position of this object.
	 * 
	 * @param position New position.
	 */
	public abstract void setPosition(Vector3f position);
	
	/**
	 * This function should be called right before an object is removed from the
	 * scene. Some cleanup may be done or a body in the simulation may be
	 * removed. The default implementation notifies its listeners.
	 */
	public void onDestroy() {
		for (DrawableObjectListener dol : listeners) {
			dol.onDestroy();
		}
	}
}
