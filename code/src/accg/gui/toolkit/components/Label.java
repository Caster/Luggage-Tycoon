package accg.gui.toolkit.components;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Color;

import accg.gui.toolkit.Component;

/**
 * A component that displays one line of static text.
 */
public class Label extends Component {
	
	/**
	 * The text shown in this label.
	 */
	private String text;
	
	/**
	 * Creates a new label showing the given text.
	 * @param text The text to show.
	 */
	public Label(String text) {
		this.text = text;
	}

	@Override
	public int getPreferredWidth() {
		return getFont().getWidth(text);
	}

	@Override
	public int getPreferredHeight() {
		return getFont().getLineHeight();
	}

	@Override
	public void draw() {
		glEnable(GL_TEXTURE_2D);
		getFont().drawString(0, 0, text, Color.black);
		glDisable(GL_TEXTURE_2D);
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
