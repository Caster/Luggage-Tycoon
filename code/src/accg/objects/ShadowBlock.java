package accg.objects;

import static org.lwjgl.opengl.GL11.*;
import static accg.gui.toolkit.GLUtils.*;

import java.awt.Color;

import javax.vecmath.Vector3f;

import accg.State;
import accg.objects.blocks.AscendingConveyorBlock;
import accg.objects.blocks.BendLeftConveyorBlock;
import accg.objects.blocks.BendRightConveyorBlock;
import accg.objects.blocks.ConveyorBlock;
import accg.objects.blocks.ConveyorBlock.ConveyorBlockType;
import accg.objects.blocks.DescendingConveyorBlock;
import accg.objects.blocks.EnterBlock;
import accg.objects.blocks.FlatConveyorBlock;
import accg.objects.blocks.LeaveBlock;

/**
 * Draws an object as a "shadow" object.
 */
public class ShadowBlock extends Block {
	
	private Block block;
	private boolean alerted;
	private boolean transparent;
	private boolean visible;
	
	/**
	 * Colors used for drawing:
	 *  0 - not alerted, not transparent
	 *  1 - not alerted,     transparent
	 *  2 -     alerted, not transparent
	 *  3 -     alerted,     transparent
	 */
	public static final Color[] COLORS = new Color[] {
		new Color(1, 1, 1, 1f),
		new Color(1, 1, 1, 0.50f),
		new Color(0.75f, 0.01f, 0.01f, 1f),
		new Color(0.75f, 0.01f, 0.01f, 0.50f)
	};
	
	/**
	 * Creates a new shadow object with no object yet.
	 */
	public ShadowBlock() {
		super(0, 0, 0, Orientation.UP);
		this.block = null;
		this.alerted = false;
		this.transparent = true;
		this.visible = false;
	}
	
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
		
		if (!visible || block == null) {
			return;
		}
		
		if (transparent) {
			glEnable(GL_BLEND);
		}
		
		glColor4f(COLORS[(alerted ? 2 : 0) + (transparent ? 1 : 0)]);
		
		block.draw(s);
		
		if (block instanceof ConveyorBlock) {
			((ConveyorBlock) block).drawArrow(s);
		}
		
		if (transparent) {
			glDisable(GL_BLEND);
		}
	}

	@Override
	public String getBlockID() {
		return "sb";
	}
	
	/**
	 * In case the block that is 'shadowed' by this object is an instance of a
	 * ConveyorBlock, return its type. Otherwise, return {@code null}.
	 * 
	 * @return Type of ConveyorBlock that is shadowed, if the shadowed block is
	 *         a ConveyorBlock. If not, {@code null}. The return value is also
	 *         {@code null} in case there is no object that is shadowed.
	 */
	public ConveyorBlockType getConveyorBlockType() {
		if (this.block instanceof ConveyorBlock) {
			return ((ConveyorBlock) this.block).getConveyorBlockType();
		} else {
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>In case there is no object that is shadowed by this block, the value
	 * -1 is returned.
	 */
	@Override
	public int getHeight() {
		if (this.block == null) {
			return -1;
		}
		return this.block.getHeight();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>In case there is no object that is shadowed by this block, the value
	 * -1 is returned.
	 */
	@Override
	public int getX() {
		if (this.block == null) {
			return -1;
		}
		return this.block.getX();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>In case there is no object that is shadowed by this block, the value
	 * -1 is returned.
	 */
	@Override
	public int getY() {
		if (this.block == null) {
			return -1;
		}
		return this.block.getY();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>In case there is no object that is shadowed by this block, the value
	 * -1 is returned.
	 */
	@Override
	public int getZ() {
		if (this.block == null) {
			return -1;
		}
		return this.block.getZ();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>In case there is no object that is shadowed by this block, the value
	 * {@code null} is returned.
	 */
	@Override
	public Orientation getOrientation() {
		if (this.block == null) {
			return null;
		}
		return this.block.getOrientation();
	}
	
	/**
	 * Return if this {@link ShadowBlock} is actually shadowing a block or not.
	 * 
	 * @return If this {@link ShadowBlock} is actually shadowing a block or not.
	 */
	public boolean hasBlock() {
		return (this.block != null);
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
	
	/**
	 * Change the 'shadowed' block to match the given type. Does nothing if the
	 * given type is equal to the type returned by the method
	 * {@link #getConveyorBlockType()}. If the type is <code>null</code>, the
	 * block will be removed from the {@link ShadowBlock} such that no block is
	 * shadowed anymore.
	 * 
	 * <p>The position and orientation of the block that is 'shadowed' are kept.
	 * 
	 * @param type The (possibly new) type to change to.
	 */
	public void setConveyorBlockType(ConveyorBlockType type) {
		if (type == null) {
			this.block = null;
			return;
		}
		
		if (type == getConveyorBlockType()) {
			return;
		}
		
		switch (type) {
		case ASCENDING: this.block = new AscendingConveyorBlock(getX(), getY(),
				getZ(), getOrientation()); break;
		case BEND_LEFT: this.block = new BendLeftConveyorBlock(getX(), getY(),
				getZ(), getOrientation()); break;
		case BEND_RIGHT: this.block = new BendRightConveyorBlock(getX(), getY(),
				getZ(), getOrientation()); break;
		case DESCENDING: this.block = new DescendingConveyorBlock(getX(), getY(),
				getZ(), getOrientation()); break;
		case FLAT: this.block = new FlatConveyorBlock(getX(), getY(),
				getZ(), getOrientation()); break;
		case ENTER: this.block = new EnterBlock(getX(), getY(), getZ(),
				getOrientation(), EnterBlock.DEFAULT_TIME_BETWEEN_LUGGAGE); break;
		case LEAVE: this.block = new LeaveBlock(getX(), getY(), getZ(),
				getOrientation()); break;
		}
		
		// also initialize scale factor properly
		setAlerted(this.alerted);
		// If the shadowed block was null before, the position is not properly
		// initialised, so we fix that here. Note that explicit use of member
		// variables instead of the getters at this point!
		setX(this.x);
		setY(this.y);
		setZ(this.z);
		setOrientation(this.orientation);
	}
	
	@Override
	public void setX(int x) {
		super.setX(x);
		if (this.block != null) {
			this.block.setX(x);
		}
	}
	
	@Override
	public void setY(int y) {
		super.setY(y);
		if (this.block != null) {
			this.block.setY(y);
		}
	}
	
	@Override
	public void setZ(int z) {
		super.setZ(z);
		if (this.block != null) {
			this.block.setZ(z);
		}
	}
	
	@Override
	public void setOrientation(Orientation orientation) {
		super.setOrientation(orientation);
		if (this.block != null) {
			this.block.setOrientation(orientation);
		}
	}
	
	/**
	 * Change the position of this object. Note that the contents of the given
	 * vector are cast to integers.
	 * 
	 * @param position New position.
	 */
	public void setPosition(Vector3f position) {
		setX((int) position.x);
		setY((int) position.y);
		setZ((int) position.z);
	}
	
	/**
	 * Change if this block is drawn 'alerted' or not.
	 * 
	 * @param alerted If the block should be drawn 'alerted'.
	 */
	public void setAlerted(boolean alerted) {
		this.alerted = alerted;
		if (this.block != null) {
			block.setScaleFactor(alerted ? 1.0005f : 1);
		}
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
