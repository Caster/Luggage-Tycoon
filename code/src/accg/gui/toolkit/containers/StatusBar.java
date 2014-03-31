package accg.gui.toolkit.containers;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import accg.gui.toolkit.Component;
import accg.gui.toolkit.Container;
import accg.gui.toolkit.enums.Alignment;
import accg.gui.toolkit.enums.Position;

public class StatusBar extends Container {

	/**
	 * Space between edge of bar and components.
	 */
	public static final int PADDING = 5;
	
	/**
	 * List of components in this StatusBar.
	 */
	protected ArrayList<Component> children;
	/**
	 * The position of this StatusBar in its parent.
	 */
	protected Position position;
	
	/**
	 * Construct a new {@link StatusBar} that is positioned at the bottom of its
	 * parent {@link Container} and has no child {@link Component Components}.
	 */
	public StatusBar() {
		this.children = new ArrayList<Component>();
		this.position = Position.BOTTOM;
	}
	
	/**
	 * Construct a new {@link StatusBar} that is positioned at the given
	 * position in its parent {@link Container} and has no child
	 * {@link Component Components}.
	 * 
	 * @param position Position of the StatusBar to be created in its parent.
	 */
	public StatusBar(Position position) {
		this.children = new ArrayList<Component>();
		this.position = position;
	}
	
	@Override
	public void add(Component toAdd) {
		toAdd.setParent(this);
		children.add(toAdd);
	}

	@Override
	public void draw() {
		// draw background, which is semi-transparent
		glColor4d(1, 1, 1, 0.5);
		glBegin(GL_QUADS);
		{
			glVertex2d(0, outline.getHeight());
			glVertex2d(outline.getWidth(), outline.getHeight());
			glVertex2d(outline.getWidth(), 0);
			glVertex2d(0, 0);
		}
		glEnd();
		
		// draw children on top of the background
		super.draw();
	}
	
	/**
	 * Returns the position of this StatusBar.
	 * @return The position of this StatusBar.
	 */
	public Position getPosition() {
		return position;
	}
	
	@Override
	public void layout() {
		boolean horLayout = this.position.isHorizontal();
		
		// we iterate over all children to position them one by one
		// the x and y here are the coordinates of the top left coordinate
		// of the child to be positioned next in the loop
		int x = PADDING;
		int y = PADDING;
		
		for (Component child : children) {
			if (horLayout) {
				child.setHeight(getHeight());
				child.setWidth(child.getPreferredWidth());
			} else {
				child.setHeight(getPreferredHeight());
				child.setWidth(getWidth());
			}
			
			child.setX(x);
			child.setY(y);
			
			if (horLayout) {
				x += child.getWidth() + PADDING;
			} else {
				y += child.getHeight() + PADDING;
			}
		}
	}

	@Override
	public List<? extends Component> getChildren() {
		return children;
	}

	@Override
	protected boolean isTransparent() {
		return true;
	}

	@Override
	public int getPreferredWidth() {
		if (this.position.isHorizontal()) {
			return parent.getWidth();
		}
		
		int maxWidth = 0;
		int childPrefWidth;
		for (Component child : children) {
			if ((childPrefWidth = child.getPreferredWidth()) > maxWidth) {
				maxWidth = childPrefWidth;
			}
		}
		return maxWidth + 2 * PADDING;
	}

	@Override
	public int getPreferredHeight() {
		if (this.position.isVertical()) {
			return parent.getHeight();
		}
		
		int maxHeight = 0;
		int childPrefHeight;
		for (Component child : children) {
			if ((childPrefHeight = child.getPreferredHeight()) > maxHeight) {
				maxHeight = childPrefHeight;
			}
		}
		return maxHeight + 2 * PADDING;
	}

	@Override
	public String getComponentName() {
		return "StatusBar";
	}
	
	/**
	 * Change the position of this StatusBar.
	 * @param newPosition The new position for this StatusBar.
	 */
	public void setPosition(Position newPosition) {
		this.position = newPosition;
		needsLayout();
	}
	
	/**
	 * Update the position of the status bar to avoid, as good as possible, a
	 * conflict with the given position and alignment of a {@link MenuStack}.
	 * 
	 * @param menuPos Position of the {@link MenuStack}.
	 * @param menuAlign Alignment of the {@link MenuStack}.
	 */
	public void updatePosition(Position menuPos, Alignment menuAlign) {
		switch (menuPos) {
		case TOP:
			setPosition(Position.BOTTOM);
			break;
		case LEFT:
		case RIGHT:
			setPosition(menuAlign == Alignment.END ? Position.TOP :
				Position.BOTTOM);
			break;
		case BOTTOM:
			setPosition(Position.TOP);
			break;
		}
	}
}
