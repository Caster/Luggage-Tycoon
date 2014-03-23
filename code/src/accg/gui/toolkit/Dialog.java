package accg.gui.toolkit;

import static org.lwjgl.opengl.GL11.*;

import java.util.Collection;
import java.util.Collections;

import org.newdawn.slick.Color;

/**
 * A dialog is a component that shows a caption on top, and some other component
 * in its body.
 */
public class Dialog extends Container {
	
	/**
	 * The caption of this dialog.
	 */
	private String caption;
	
	/**
	 * The component shown as the body of this dialog.
	 */
	private Component body;

	/**
	 * Distance between the border of the dialog and the contents. Also, the
	 * distance between the caption and the body of the dialog.
	 */
	public static final int PADDING = 10;
	
	/**
	 * Creates a new dialog with the given caption and body.
	 * 
	 * @param caption The caption of this dialog.
	 * @param body The body.
	 */
	public Dialog(String caption, Component body) {
		this.caption = caption;
		this.body = body;
	}
	
	@Override
	public int getPreferredWidth() {
		return Math.max(font.getWidth(caption), body.getPreferredWidth()) + 2 * PADDING;
	}

	@Override
	public int getPreferredHeight() {
		return font.getLineHeight() + 3 * PADDING;
	}

	@Override
	public void draw() {
		
		layoutIfNeeded();
		
		glColor4d(1, 1, 1, 0.5);
		glBegin(GL_QUADS);
		{
			drawBackground();
		}
		glEnd();

		getFont().drawString((getWidth() - getFont().getWidth(caption)) / 2, PADDING,
				caption, Color.black);
	}
	
	/**
	 * Assumes that it is called inside GL_QUADS. Will render a background
	 * on the correct spot.
	 */
	private void drawBackground() {
		glVertex2d(0, outline.getHeight());
		glVertex2d(outline.getWidth(), outline.getHeight());
		glVertex2d(outline.getWidth(), 0);
		glVertex2d(0, 0);
	}

	@Override
	public String getComponentName() {
		return "Dialog";
	}
	
	/**
	 * Changes the caption of this dialog.
	 * @param caption The new caption.
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * Returns the caption of this dialog.
	 * @return The caption.
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * Changes the body of this dialog.
	 * @param body The new body.
	 */
	public void setBody(Component body) {
		this.body = body;
	}

	/**
	 * Returns the body of this dialog.
	 * @return The body.
	 */
	public Component getBody() {
		return body;
	}

	@Override
	public void add(Component toAdd) {
		throw new UnsupportedOperationException("Adding to a Dialog is not supported; use setBody() instead");
	}

	@Override
	public void layout() {
		body.setX(PADDING);
		body.setY(2 * PADDING + getFont().getLineHeight());
		body.setWidth(getWidth() - 2 * PADDING);
		body.setHeight(getHeight() - 3 * PADDING - getFont().getLineHeight());
	}

	@Override
	public Collection<? extends Component> getChildren() {
		return Collections.singleton(body);
	}

	@Override
	protected boolean isTransparent() {
		return false;
	}
}
