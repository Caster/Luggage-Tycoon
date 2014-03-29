package accg.gui.toolkit;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import accg.gui.toolkit.event.MouseClickEvent;

/**
 * A button is a clickable object containing a text and an icon.
 * 
 * \todo Javadoc: There are still quite some references to "menu item".
 * (Earlier, this class was only for menu items.) 
 */
public class Button extends Component implements Listener {

	/**
	 * Distance between edge of this item and the text/icon inside it.
	 */
	public static final int PADDING = 10;
	
	/**
	 * The Type of a {@link Button} indicates what properties it
	 * has. Refer to the documentation of the enum members for details.
	 */
	public enum Type {
		
		/**
		 * A regular menu item.
		 */
		NORMAL,
		
		/**
		 * A menu item that has a boolean isChecked property. When
		 * clicked, the value of that property is toggled and this is
		 * also indicated visually.
		 */
		CHECKABLE,
		
		/**
		 * Same as a {@link #CHECKABLE} menu item, but whenever a menu
		 * item is checked, all other menu items in the same {@link MenuBar}
		 * are automatically unchecked.
		 */
		CHECKABLE_UNIQUE
	}
	
	/**
	 * Presentation of items.
	 */
	protected Presentation presentation = Presentation.ICON_LEFT_TEXT;
	
	/**
	 * Possible presentations/layouts for items in a {@link MenuBar}. A
	 * presentation defines the style of button.
	 */
	public enum Presentation {
		
		/**
		 * A small icon is shown on the left of the text.
		 */
		ICON_LEFT_TEXT("(small) Icon left of text"),
		
		/**
		 * A large icon is shown on top of the text.
		 */
		ICON_ABOVE_TEXT("(large) Icon above text");
		
		private Presentation(String name) {
			this.name = name;
		}
		
		private String name;
		
		/**
		 * Return a human-readable description of the presentation type.
		 * 
		 * @return A human-readable, short description.
		 */
		public String getName() {
			return name;
		}
	}
	
	/**
	 * Construct a new {@link Button} with given text and icon.
	 * The text and icon should not be {@code null}.
	 * 
	 * @param text Text that is displayed next to the icon.
	 * @param icon Icon that is displayed.
	 * @throws IllegalArgumentException when either {@code text} or
	 *         {@code icon} is {@code null}.
	 */
	public Button(String text, Texture icon) {
		this(text, icon, Type.NORMAL);
	}
	
	/**
	 * Construct a new {@link Button} with given text and icon.
	 * The text and icon should not be {@code null}.
	 * 
	 * @param text Text that is displayed next to the icon.
	 * @param icon Icon that is displayed.
	 * @param type {@link Type} of the menu item.
	 * @throws IllegalArgumentException when either {@code text} or
	 *         {@code icon} is {@code null}.
	 */
	public Button(String text, Texture icon, Type type) {
		if (text == null || icon == null) {
			throw new IllegalArgumentException("Neither text nor icon "
					+ "can be null for a MenuBarItem.");
		}
		
		this.text = text;
		this.icon = icon;
		this.type = type;
		this.checked = false;
		
		addListener(this);
	}

	@Override
	public int getPreferredWidth() {
		switch (getPresentation()) {
		default:
		case ICON_ABOVE_TEXT:
			return Math.max(getPreferredHeight(), getFont().getWidth(text))
					+ 2 * PADDING;
		case ICON_LEFT_TEXT:
			return getFont().getWidth(text) + 3 * PADDING + getFont().getLineHeight();
		}
	}

	@Override
	public int getPreferredHeight() {
		switch (getPresentation()) {
		default:
		case ICON_ABOVE_TEXT:
			return 100; // TODO
		case ICON_LEFT_TEXT:
			return getFont().getLineHeight() + 2 * PADDING;
		}
	}
	
	@Override
	public void draw() {
		
		if (isHovered() && hoverAmount < 10) {
			hoverAmount++;
		}
		if (!isHovered() && hoverAmount > 0) {
			hoverAmount--;
		}
		
		// render hovered background, if needed
		if ((hoverAmount > 0 && this.drawHoveredBackground) ||
				(this.checked && this.drawCheckedBackground)) {
			if (this.checked) {
				glColor4d(1, 1, 1, 1);
			} else {
				glColor4d(1, 1, 1, hoverAmount / 10f);
			}
			glBegin(GL_QUADS);
			{
				glVertex2d(0, outline.getHeight());
				glVertex2d(outline.getWidth(), outline.getHeight());
				glVertex2d(outline.getWidth(), 0);
				glVertex2d(0, 0);
			}
			glEnd();
		}
		
		// render text
		glEnable(GL_TEXTURE_2D);
		
		int textWidth = getFont().getWidth(text);
		int textHeight = getFont().getLineHeight();
		if (this.drawText) {
			switch (presentation) {
			default :
			case ICON_ABOVE_TEXT :
				getFont().drawString(0 + (outline.getWidth() -
						textWidth) / 2, getHeight() - PADDING - textHeight,
						text, Color.black);
				break;
			case ICON_LEFT_TEXT :
				getFont().drawString(0 + getFont().getLineHeight() +
						2 * PADDING, getHeight() - PADDING - textHeight,
						text, Color.black);
				break;
			}
		}
		
		// render icon
		if (this.drawIcon) {
			int iconSize, iconX, iconY;
			
			switch (presentation) {
			default:
			case ICON_ABOVE_TEXT:
				iconSize = Math.min(outline.getWidth() - 2 * PADDING,
						outline.getHeight() - 3 * PADDING - textHeight);
				iconX = (outline.getWidth() - iconSize) / 2;
				iconY = (outline.getHeight() - 2 * PADDING - textHeight - iconSize) / 2;
				break;
			case ICON_LEFT_TEXT:
				iconSize = textHeight;
				iconX = PADDING;
				iconY = PADDING;
			}
			
			glColor3d(1, 1, 1);
			icon.bind();
			glBegin(GL_QUADS);
			{
				glTexCoord2d(0, 0);
				glVertex2d(iconX, iconY);
				glTexCoord2d(1, 0);
				glVertex2d(iconX + iconSize, iconY);
				glTexCoord2d(1, 1);
				glVertex2d(iconX + iconSize, iconY + iconSize);
				glTexCoord2d(0, 1);
				glVertex2d(iconX, iconY + iconSize);
			}
			glEnd();
		}
		
		glDisable(GL_TEXTURE_2D);
	}
	
	@Override
	public String getComponentName() {
		return "Button";
	}
	
	/**
	 * Return if this menu item is checked. Only applicable if the type of this
	 * menu item is {@link Type#CHECKABLE} or {@link Type#CHECKABLE_UNIQUE}. If
	 * that is not the case, this method will always return false.
	 * 
	 * @return If this menu item is checked, or false if the notion of checked
	 *         does not apply to this menu item type.
	 */
	public boolean isChecked() {
		return checked;
	}

	@Override
	public void event(Event event) {
		if (event instanceof MouseClickEvent) {
			if (this.type == Type.CHECKABLE || this.type == Type.CHECKABLE_UNIQUE) {
				this.checked = !this.checked;
				if (this.checked && this.type == Type.CHECKABLE_UNIQUE
						&& parent != null && parent instanceof MenuBar) {
					((MenuBar) parent).uncheckOtherItems(this);
				}
			}
		}
	}
	
	/**
	 * Change if this menu item is checked or not. Only has effect if the
	 * type of this menu item is {@link Type#CHECKABLE} or
	 * {@link Type#CHECKABLE_UNIQUE}.
	 * 
	 * @param checked The new value for the isChecked property of this item.
	 */
	public void setChecked(boolean checked) {
		if (this.type == Type.CHECKABLE || this.type == Type.CHECKABLE_UNIQUE) {
			this.checked = checked;
		}
	}
	
	/**
	 * Changes the presentation of this menu item.
	 * @param presentation New presentation for this menu item.
	 */
	public void setPresentation(Presentation presentation) {
		this.presentation = presentation;
	}

	/**
	 * Returns the presentation of this menu item.
	 * @return The presentation.
	 */
	public Presentation getPresentation() {
		return presentation;
	}
	
	/** Describing text of this menu item. */
	protected String text;
	/** Icon of this menu item. */
	protected Texture icon;
	/** Type of this menu item. */
	protected Type type;
	/** In [0, 10); used for the hover animation. */
	protected int hoverAmount;
	/** Indicates if this menu item is checked. */
	protected boolean checked;
	
	/** Indicates if the background should be rendered differently when checked. */
	protected boolean drawCheckedBackground = true;
	/** Indicates if the background should be rendered differently when hovered. */
	protected boolean drawHoveredBackground = true;
	/** Indicates if the text should be drawn. */
	protected boolean drawText = true;
	/** Indicates if the icon should be drawn. */
	protected boolean drawIcon = true;
}
