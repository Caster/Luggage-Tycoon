package accg.gui.toolkit;

import static org.lwjgl.opengl.GL11.*;

/**
 * A panel that fills its background with transparent black. This is intended
 * to have a Dialog as a child.
 */
public class DialogPanel extends Panel {

	/**
	 * Creates a new dialog panel.
	 * @param child The child.
	 */
	public DialogPanel(Component child) {
		super(child);
	}

	@Override
	public void draw() {
		glColor4f(0, 0, 0, 0.5f);
		
		glBegin(GL_QUADS);
		{
			glVertex2d(0, outline.getHeight());
			glVertex2d(outline.getWidth(), outline.getHeight());
			glVertex2d(outline.getWidth(), 0);
			glVertex2d(0, 0);
		}
		glEnd();
		
		super.draw();
	}
	
	@Override
	public String getComponentName() {
		return "DialogPanel";
	}
}
