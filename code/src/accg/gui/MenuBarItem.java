package accg.gui;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.opengl.Texture;

/**
 * A MenuBarItem can be placed inside a {@link MenuBar} and has text and an
 * icon. It can be clicked and will fire an action in that case.
 * 
 * The text is always drawn centered below the icon.
 */
public abstract class MenuBarItem {

	/**
	 * Distance between edge of this item and the text/icon inside it.
	 */
	public static final int PADDING = 10;
	
	/**
	 * Construct a new {@link MenuBarItem} with given text and icon.
	 * The text and icon should not be {@code null}.
	 * 
	 * @param text Text that is displayed next to the icon.
	 * @param icon Icon that is displayed.
	 * @throws IllegalArgumentException when either {@code text} or
	 *         {@code icon} is {@code null}.
	 */
	public MenuBarItem(String text, Texture icon) {
		if (text == null || icon == null) {
			throw new IllegalArgumentException("Neither text nor icon "
					+ "can be null for a MenuBarItem.");
		}
		
		this.text = text;
		this.icon = icon;
		this.hovered = false;
	}
	
	/**
	 * Render this {@link MenuBarItem} in the given outline using static
	 * OpenGL functions for immediate mode rendering.
	 * 
	 * @param outline Rectangle in which the item should be rendered.
	 * @param renderFont Font that should be used for text rendering.
	 */
	public void draw(Rectangle outline, Font renderFont) {
		// render hovered background, if needed
		if (this.hovered && this.drawHoveredBackground) {
			glColor4d(1, 1, 1, 1);
			glBegin(GL_QUADS);
			{
				glVertex2d(outline.getX(), outline.getY() - outline.getHeight());
				glVertex2d(outline.getX() + outline.getWidth(),
						outline.getY() - outline.getHeight());
				glVertex2d(outline.getX() + outline.getWidth(), outline.getY());
				glVertex2d(outline.getX(), outline.getY());
			}
			glEnd();
		}
		
		// render text
		int textWidth = renderFont.getWidth(text);
		int textHeight = renderFont.getHeight(text);
		if (this.drawText) {
			renderFont.drawString(outline.getX() + (outline.getWidth() - textWidth) / 2,
									outline.getY() - PADDING - textHeight, text, Color.black);
		}
		
		// render icon
		if (this.drawIcon) {
			int iconSize = Math.min(outline.getWidth() - 2 * PADDING,
					outline.getHeight() - 3 * PADDING - textHeight);
			int iconX = outline.getX() + (outline.getWidth() - iconSize) / 2;
			int iconY = outline.getY() - textHeight - 2 * PADDING -
					(outline.getHeight() - 3 * PADDING - textHeight - iconSize) / 2;
			
			glColor3d(1, 1, 1);
			glEnable(GL_TEXTURE_2D);
			icon.bind();
			glBegin(GL_QUADS);
			{
				glTexCoord2d(0, 0);
				glVertex2d(iconX, iconY - iconSize);
				glTexCoord2d(1, 0);
				glVertex2d(iconX + iconSize, iconY - iconSize);
				glTexCoord2d(1, 1);
				glVertex2d(iconX + iconSize, iconY);
				glTexCoord2d(0, 1);
				glVertex2d(iconX, iconY);
			}
			glEnd();
			glDisable(GL_TEXTURE_2D);
		}
	}

	/**
	 * Return the preferred minimal width of this item.
	 * 
	 * @param renderFont The font in which the text should be rendered.
	 * @return The preferred minimal width of this item.
	 */
	public int getPreferredWidth(Font renderFont) {
		return renderFont.getWidth(text) + 2 * PADDING;
	}
	
	/**
	 * Return if this menu item is currenlty being hovered by the mouse.
	 * 
	 * @return the value of the parameter of the last call to {@link #setHovered(boolean)}.
	 */
	public boolean isHovered() {
		return hovered;
	}
	
	/**
	 * Called when this menu item is clicked. In this method, code that
	 * handles whatever this menu item stands for should be placed.
	 */
	public abstract void onClick();
	
	/**
	 * Called when this menu item is hovered and then the scroll wheel on
	 * the mouse is moved. This may be handy for interactive menu items.
	 * 
	 * @param dWheel The delta of the scroll wheel.
	 */
	public abstract void onScroll(int dWheel);
	
	/**
	 * Change if this menu item is being hovered or not.
	 * 
	 * @param hovered If this item is being hovered by the mouse.
	 */
	public void setHovered(boolean hovered) {
		this.hovered = hovered;
	}
	
	/** Describing text of this menu item. */
	protected String text;
	/** Icon of this menu item. */
	protected Texture icon;
	/** Indicates if this menu item is being hovered by the mouse. */
	protected boolean hovered;
	
	/** Indicates if the background should be rendered differently when hovered. */
	protected boolean drawHoveredBackground = true;
	/** Indicates if the text should be drawn. */
	protected boolean drawText = true;
	/** Indicates if the icon should be drawn. */
	protected boolean drawIcon = true;
}
