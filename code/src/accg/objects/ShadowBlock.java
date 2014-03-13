package accg.objects;

import static org.lwjgl.opengl.GL11.*;

import javax.vecmath.Vector3f;

import accg.State;

/**
 * Draws an object as a "shadow" object.
 */
public class ShadowBlock extends Block {
	
	private Block block;
	private boolean alerted;
	private boolean transparent;
	private boolean visible;
	
	/**
	 * Creates a new shadow object with the given object.
	 * @param object The object to draw.
	 */
	public ShadowBlock(Block object) {
		super(object.x, object.y, object.z, object.orientation);
		this.block = object;
		this.alerted = false;
		this.transparent = true;
		this.visible = false;
	}

	@Override
	public Block clone() {
		return this.block.clone();
	}
	
	@Override
	public void draw(State s) {
		
		if (!visible) {
			return;
		}
		
		if (transparent) {
			glEnable(GL_BLEND);
		}
		
		if (alerted) {
			glColor4f(0.75f, 0.01f, 0.01f, (transparent ? 0.50f : 1));
			block.setScaleFactor(1.05f);
		} else {
			glColor4f(1, 1, 1, (transparent ? 0.50f : 1));
		}
		
		block.draw(s);
		
		if (alerted) {
			block.setScaleFactor(1);
		}
		
		if (transparent) {
			glDisable(GL_BLEND);
		}
	}

	@Override
	public int getHeight() {
		return this.block.getHeight();
	}
	
	@Override
	public int getX() {
		return this.block.getX();
	}
	
	@Override
	public int getY() {
		return this.block.getY();
	}
	
	@Override
	public int getZ() {
		return this.block.getZ();
	}
	
	@Override
	public Orientation getOrientation() {
		return this.block.getOrientation();
	}
	
	/**
	 * Return if this block has the 'alert' status, so if it is drawn in some
	 * kind of 'alert' color.
	 * @return If this block is 'alerted'.
	 */
	public boolean isAlerted() {
		return alerted;
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
		this.block.setX(x);
	}
	
	@Override
	public void setY(int y) {
		this.block.setY(y);
	}
	
	@Override
	public void setZ(int z) {
		this.block.setZ(z);
	}
	
	@Override
	public void setOrientation(Orientation orientation) {
		this.block.setOrientation(orientation);
	}
	
	/**
	 * Change the position of this object. Note that the contents of the given
	 * vector are cast to integers.
	 * 
	 * @param position New position.
	 */
	public void setPosition(Vector3f position) {
		this.block.x = (int) position.x;
		this.block.y = (int) position.y;
		this.block.z = (int) position.z;
	}
	
	/**
	 * Change if this block is drawn 'alerted' or not.
	 * 
	 * @param alerted If the block should be drawn 'alerted'.
	 */
	public void setAlerted(boolean alerted) {
		this.alerted = alerted;
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
