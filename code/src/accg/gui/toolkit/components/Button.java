package accg.gui.toolkit.components;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import accg.gui.toolkit.Component;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.containers.MenuBar;
import accg.gui.toolkit.enums.ButtonType;
import accg.gui.toolkit.enums.Presentation;
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
	 * Color of the shortcut hint.
	 */
	public static final Color SHORTCUT_HINT_COLOR = new Color(0, 0, 0, 0.5f);
	
	/**
	 * Presentation of items.
	 */
	protected Presentation presentation = Presentation.ICON_LEFT_TEXT;
	
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
		this(text, icon, ButtonType.NORMAL);
	}
	
	/**
	 * Construct a new {@link Button} with given text and icon.
	 * The text and icon should not be {@code null}.
	 * 
	 * @param text Text that is displayed next to the icon.
	 * @param icon Icon that is displayed.
	 * @param type {@link ButtonType} of the menu item.
	 * @throws IllegalArgumentException when either {@code text} or
	 *         {@code icon} is {@code null}.
	 */
	public Button(String text, Texture icon, ButtonType type) {
		if (text == null || icon == null) {
			throw new IllegalArgumentException("Neither text nor icon "
					+ "can be null for a MenuBarItem.");
		}
		
		this.text = text;
		this.icon = icon;
		this.type = type;
		this.checked = false;
		
		this.shortcutHint = "";
		
		addListener(this);
	}

	@Override
	public int getPreferredWidth() {
		switch (getPresentation()) {
		default:
		case ICON_ABOVE_TEXT:
			return Math.max(getPreferredHeight(), getFont().getWidth(getTotalText()))
					+ 2 * PADDING;
		case ICON_LEFT_TEXT:
			return getFont().getWidth(getTotalText()) + 3 * PADDING + getFont().getLineHeight();
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
		
		int textWidth = getFont().getWidth(getTotalText());
		int textHeight = getFont().getLineHeight();
		int shortcutWidth = getFont().getWidth(getShortcutHintText());
		if (this.drawText) {
			switch (presentation) {
			default:
			case ICON_ABOVE_TEXT:
				// text
				getFont().drawString((outline.getWidth() -
						textWidth) / 2, getHeight() - PADDING - textHeight,
						text, Color.black);
				// shortcut hint
				getFont().drawString((outline.getWidth() +
						textWidth) / 2 - shortcutWidth, getHeight() - PADDING - textHeight,
						getShortcutHintText(), SHORTCUT_HINT_COLOR);
				break;
			case ICON_LEFT_TEXT:
				// text
				getFont().drawString(getFont().getLineHeight() +
						2 * PADDING, getHeight() - PADDING - textHeight,
						text, Color.black);
				// shortcut hint
				getFont().drawString(getFont().getLineHeight() +
						2 * PADDING + textWidth - shortcutWidth,
						getHeight() - PADDING - textHeight,
						getShortcutHintText(), SHORTCUT_HINT_COLOR);
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
	
	/**
	 * Returns the text shown on this button; with shortcut hint if that is
	 * available. This is only meant for layouting the button.
	 * 
	 * @return The total text, that is "text (shortcutHint)".
	 */
	protected String getTotalText() {
		return text + getShortcutHintText();
	}
	
	/**
	 * Returns the shortcut hint text that is added to the normal text.
	 * @return The shortcut hint text, that is " (shortcutHint)".
	 */
	protected String getShortcutHintText() {
		if (shortcutHint == null || shortcutHint.isEmpty()) {
			return "";
		}
		
		return " (" + shortcutHint + ")";
	}
	
	@Override
	public String getComponentName() {
		return "Button";
	}
	
	/**
	 * Return if this menu item is checked. Only applicable if the type of this
	 * menu item is {@link ButtonType#CHECKABLE} or {@link ButtonType#CHECKABLE_UNIQUE}. If
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
			if (this.type == ButtonType.CHECKABLE ||
					this.type == ButtonType.CHECKABLE_UNIQUE) {
				this.checked = !this.checked;
				if (this.checked && this.type == ButtonType.CHECKABLE_UNIQUE
						&& parent != null && parent instanceof MenuBar) {
					((MenuBar) parent).uncheckOtherItems(this);
				}
			}
		}
	}
	
	/**
	 * Change if this menu item is checked or not. Only has effect if the
	 * type of this menu item is {@link ButtonType#CHECKABLE} or
	 * {@link ButtonType#CHECKABLE_UNIQUE}.
	 * 
	 * @param checked The new value for the isChecked property of this item.
	 */
	public void setChecked(boolean checked) {
		if (this.type == ButtonType.CHECKABLE ||
				this.type == ButtonType.CHECKABLE_UNIQUE) {
			this.checked = checked;
		}
	}
	
	/**
	 * Sets the shortcut hint. This is an indication of the shortcut that the
	 * user can use to activate this action.
	 * 
	 * \note The shortcut hint is only used for display; not for the actual
	 * shortcut handling.
	 * 
	 * @param hint The new shortcut hint.
	 */
	public void setShortcutHint(String hint) {
		this.shortcutHint = hint;
	}
	
	/**
	 * Returns the current shortcut hint.
	 * 
	 * \note The shortcut hint is only used for display; not for the actual
	 * shortcut handling.
	 * 
	 * @return The shortcut hint.
	 */
	public String getShortcutHint() {
		return shortcutHint;
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
	
	/**
	 * Shows a short animation on this button, as if it has been clicked very
	 * quickly.
	 * 
	 * This can be used for example when this button has been activated in some
	 * other way than clicking, for an additional clue that this button
	 * corresponds to that action too.
	 */
	public void touch() {
		
		// set the hoverAmount value to 10, so it will behave as if the mouse
		// just left the button
		hoverAmount = 10;
	}
	
	/** Describing text of this menu item. */
	protected String text;
	/** The shortcut hint of this menu item. */
	protected String shortcutHint;
	/** Icon of this menu item. */
	protected Texture icon;
	/** Type of this menu item. */
	protected ButtonType type;
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
