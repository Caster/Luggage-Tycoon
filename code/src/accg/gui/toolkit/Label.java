package accg.gui.toolkit;

/**
 * A component that displays one line of static text.
 */
public class Label extends Component {
	
	/**
	 * The text shown in this label.
	 */
	private String text;
	
	@Override
	public int getPreferredWidth() {
		return font.getWidth(text);
	}

	@Override
	public int getPreferredHeight() {
		return font.getLineHeight();
	}

	@Override
	public void draw() {
		font.drawString(0, 0, text);
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
