package accg.objects;

import static org.lwjgl.opengl.GL11.*;

import javax.vecmath.Vector3f;

import accg.State;

/**
 * Draws an object as a "shadow" object.
 */
public class ShadowBlock extends Block {
	
	private Block object;
	private boolean transparent;
	private boolean visible;
	
	/**
	 * Creates a new shadow object with the given object.
	 * @param object The object to draw.
	 */
	public ShadowBlock(Block object) {
		super(object.x, object.y, object.z, object.orientation);
		this.object = object;
		this.transparent = true;
		this.visible = false;
	}

	@Override
	public Block clone() {
		return this.object.clone();
	}
	
	@Override
	public void draw(State s) {
		
		if (!visible) {
			return;
		}
		
		if (transparent) {
			glEnable(GL_BLEND);
			glColor4f(1, 1, 1, 0.50f);
		} else {
			glColor4f(1,  1, 1, 1);
		}
		
		object.draw(s);
		
		if (transparent) {
			glDisable(GL_BLEND);
		}
	}
	

	@Override
	public int getHeight() {
		return this.object.getHeight();
	}
	
	@Override
	public int getX() {
		return this.object.getX();
	}
	
	@Override
	public int getY() {
		return this.object.getY();
	}
	
	@Override
	public int getZ() {
		return this.object.getZ();
	}
	
	@Override
	public Orientation getOrientation() {
		return this.object.getOrientation();
	}
	
	/**
	 * Return if this block is currently transparent.
	 * @return If this block would be drawn semi-transparently.
	 */
	public boolean isTransparent() {
		return transparent;
	}
	
	/**
	 * Return if this block is currently visible.
	 * @return If this block would be drawn.
	 */
	public boolean isVisible() {
		return visible;
	}
	
	@Override
	public void setX(int x) {
		this.object.setX(x);
	}
	
	@Override
	public void setY(int y) {
		this.object.setY(y);
	}
	
	@Override
	public void setZ(int z) {
		this.object.setZ(z);
	}
	
	@Override
	public void setOrientation(Orientation orientation) {
		this.object.setOrientation(orientation);
	}
	
	/**
	 * Change the position of this object. Note that the contents of the given
	 * vector are cast to integers.
	 * 
	 * @param position New position.
	 */
	public void setPosition(Vector3f position) {
		this.object.x = (int) position.x;
		this.object.y = (int) position.y;
		this.object.z = (int) position.z;
	}
	
	/**
	 * Change if this block is drawn semi-transparently or not.
	 * 
	 * @param transparent If the block should be drawn semi-transparently or not.
	 */
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
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
