package accg.objects;

import static org.lwjgl.opengl.GL11.*;

import javax.vecmath.Vector3f;

import accg.State;

/**
 * Draws an object as a "shadow" object.
 */
public class ShadowObject extends DrawableObject {
	
	private DrawableObject object;
	private boolean visible;
	
	/**
	 * Creates a new shadow object with the given object.
	 * @param object The object to draw.
	 */
	public ShadowObject(DrawableObject object) {
		this.object = object;
		this.visible = false;
	}

	@Override
	public void draw(State s) {
		
		if (!visible) {
			return;
		}
		
		glEnable(GL_BLEND);
		glColor4f(1, 1, 1, 0.50f);
		
		object.draw(s);
		
		glDisable(GL_BLEND);
	}
	
	/**
	 * Change the position of this object.
	 * 
	 * @param position New position.
	 */
	public void setPosition(Vector3f position) {
		this.object.setPosition(position);
	}
	
	/**
	 * Change visibility of this object. 
	 * 
	 * @param visible If the object should be drawn or not.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
