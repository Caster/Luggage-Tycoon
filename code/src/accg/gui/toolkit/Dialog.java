package accg.gui.toolkit;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.newdawn.slick.Color;

/**
 * A dialog is a component that shows a caption on top, and some other component
 * in its body. Furthermore a dialog can show buttons (or other components) below
 * the body.
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
	 * The footer that contains the buttons.
	 */
	private DialogFooter footer;

	/**
	 * Distance between the border of the dialog and the contents. Also, the
	 * distance between the caption and the body of the dialog.
	 */
	public static final int PADDING = 10;
	
	/**
	 * The mode of this dialog.
	 */
	public DialogMode mode = DialogMode.BACKDROP;
	
	/**
	 * Possible modes for dialogs.
	 */
	public enum DialogMode {
		
		/**
		 * If the dialog is larger than its preferred size, keep the dialog
		 * itself the same size. Furthermore a black "backdrop" will be shown
		 * below the dialog, using all of the available space. (This is the
		 * default mode.)
		 */
		BACKDROP,
		
		/**
		 * If the dialog is larger than its preferred size, just relayout it
		 * to fill all available space. (This is what most other components do.)
		 */
		STRETCH
	}
	
	/**
	 * Creates a new dialog with the given caption and body.
	 * 
	 * @param caption The caption of this dialog.
	 * @param body The body.
	 * @param buttons Optional components (mostly used for buttons) to show
	 * below the body.
	 */
	public Dialog(String caption, Component body, Component... buttons) {
		this.caption = caption;
		
		body.setParent(this);
		this.body = body;

		this.footer = new DialogFooter();
		footer.setParent(this);
		for (Component c : buttons) {
			footer.add(c);
		}
	}
	
	@Override
	public int getPreferredWidth() {
		return Math.max(Math.max(getFont().getWidth(caption),
				body.getPreferredWidth()), footer.getPreferredWidth()) + 2 * PADDING;
	}

	@Override
	public int getPreferredHeight() {
		return getFont().getLineHeight() + body.getPreferredHeight() +
				footer.getPreferredHeight() + 4 * PADDING;
	}

	@Override
	public void draw() {
		
		layoutIfNeeded();
		
		if (mode == DialogMode.BACKDROP) {
			glColor4f(0, 0, 0, 0.5f);
			glBegin(GL_QUADS);
			{
				drawBackdrop();
			}
			glEnd();
		}
		
		glColor4f(1, 1, 1, 0.5f);
		glBegin(GL_QUADS);
		{
			drawBackground();
		}
		glEnd();

		glEnable(GL_TEXTURE_2D);
		if (mode == DialogMode.STRETCH) {
			getFont().drawString((getWidth() - getFont().getWidth(caption)) / 2, PADDING,
					caption, Color.black);
		} else {
			getFont().drawString((getWidth() - getFont().getWidth(caption)) / 2,
					(getHeight() - getPreferredHeight()) / 2 + PADDING,
					caption, Color.black);
		}
		glDisable(GL_TEXTURE_2D);
		
		super.draw();
	}
	
	/**
	 * Assumes that it is called inside GL_QUADS. Will render a backdrop
	 * on the correct spot.
	 */
	private void drawBackdrop() {
		glVertex2d(0, outline.getHeight());
		glVertex2d(outline.getWidth(), outline.getHeight());
		glVertex2d(outline.getWidth(), 0);
		glVertex2d(0, 0);
	}
	
	/**
	 * Assumes that it is called inside GL_QUADS. Will render a background
	 * on the correct spot.
	 */
	private void drawBackground() {
		if (mode == DialogMode.STRETCH) {
			glVertex2d(0, outline.getHeight());
			glVertex2d(outline.getWidth(), outline.getHeight());
			glVertex2d(outline.getWidth(), 0);
			glVertex2d(0, 0);
		} else {
			// only draw in the middle part
			glVertex2d((getWidth() - getPreferredWidth()) / 2,
					(getHeight() + getPreferredHeight()) / 2);
			glVertex2d((getWidth() + getPreferredWidth()) / 2,
					(getHeight() + getPreferredHeight()) / 2);
			glVertex2d((getWidth() + getPreferredWidth()) / 2,
					(getHeight() - getPreferredHeight()) / 2);
			glVertex2d((getWidth() - getPreferredWidth()) / 2,
					(getHeight() - getPreferredHeight()) / 2);
		}
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
		
		int x, width, y, height;
		
		if (mode == DialogMode.STRETCH) {
			x = 0;
			width = getWidth();
			y = 0;
			height = getHeight();
		} else {
			x = (getWidth() - getPreferredWidth()) / 2;
			width = getPreferredWidth();
			y = (getHeight() - getPreferredHeight()) / 2;
			height = getPreferredHeight();
		}
		
		footer.setX(x + PADDING);
		footer.setY(y + height - PADDING - footer.getPreferredHeight());
		footer.setWidth(width - 2 * PADDING);
		footer.setHeight(footer.getPreferredHeight());
		
		body.setX(x + PADDING);
		body.setY(y + 2 * PADDING + getFont().getLineHeight());
		body.setWidth(width - 2 * PADDING);
		body.setHeight(height - 4 * PADDING - getFont().getLineHeight() - footer.getPreferredHeight());
	}

	@Override
	public java.util.List<? extends Component> getChildren() {
		ArrayList<Component> children = new ArrayList<>();
		
		children.add(body);
		children.add(footer);
		
		return children;
	}

	@Override
	protected boolean isTransparent() {
		return false;
	}
}
