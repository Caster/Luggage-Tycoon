package accg.gui.toolkit;

import org.newdawn.slick.Color;

import static org.lwjgl.opengl.GL11.*;

/**
 * A component in which the user can type text.
 */
public class TextField extends Component {
	
	/**
	 * The text shown in this text field.
	 */
	private String text;
	
	/**
	 * The approximate amount of characters that this TextField should be
	 * able to contain.
	 */
	private int characters;

	/**
	 * Distance between the border of the text field and the contents.
	 */
	public static final int PADDING = 4;
	
	/**
	 * Width of the border.
	 */
	public static final int BORDER_WIDTH = 2;
	
	/**
	 * Creates a new label without any text.
	 * 
	 * @param characters The approximate amount of characters that this
	 * TextField should be able to contain. This is not at all an absolute
	 * limit; it is only used for determining the size of the text field.
	 */
	public TextField(int characters) {
		this.characters = characters;
	}

	@Override
	public int getPreferredWidth() {
		return characters * getFont().getWidth("n");
	}

	@Override
	public int getPreferredHeight() {
		return getFont().getLineHeight();
	}

	@Override
	public void draw() {
		
		// background
		glColor4f(1, 1, 1, 0.5f);
		glBegin(GL_QUADS);
		{
			glVertex2d(0, outline.getHeight());
			glVertex2d(outline.getWidth(), outline.getHeight());
			glVertex2d(outline.getWidth(), 0);
			glVertex2d(0, 0);
		}
		glEnd();
		
		// border
		glColor4f(0, 0, 0, 1);
		glBegin(GL_QUADS);
		{
			glVertex2d(0, outline.getHeight());
			glVertex2d(outline.getWidth(), outline.getHeight());
			glVertex2d(outline.getWidth(), 0);
			glVertex2d(0, 0);
		}
		glEnd();
		
		getFont().drawString(PADDING, PADDING, text, Color.black);
	}

	@Override
	public String getComponentName() {
		return "Label";
	}
	
	/**
	 * Changes the text in this label.
	 * @param text The new text.
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Returns the text shown in this label.
	 * @return The text of this label.
	 */
	public String getText() {
		return text;
	}
}