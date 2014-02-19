package accg.gui;

import static accg.utils.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Font;
import org.newdawn.slick.opengl.Texture;

/**
 * A SliderMenuBarItem is a {@link MenuBarItem} that has a value which can
 * be queried. This value can be adjusted by the user by clicking the item
 * and then scrolling with the mouse wheel.
 * 
 * The value is then indicated by a bar at the place where the text would be.
 */
public class SliderMenuBarItem extends MenuBarItem {

	/** The color in which the slider bar is rendered. */
	public static final Color SLIDER_BAR_COLOR = new Color(0.5f, 0.5f, 0.5f);
	/** The color in which the slider bar edge is rendered. */
	public static final Color SLIDER_BAR_EDGE_COLOR = new Color(0, 0, 0);
	/**
	 * The color in which the background of the slider bar is rendered.
	 * 
	 * This is the part of the slider that is not filled.
	 */
	public static final Color SLIDER_BAR_BACKGROUND_COLOR = Color.WHITE;
	/** The width of the slider bar edge. */
	public static final int SLIDER_BAR_EDGE_WIDTH = 2;
	
	/**
 	 * Construct a new {@link SliderMenuBarItem} with given text and icon.
	 * The text and icon should not be {@code null}. The slider will range
	 * from given minimal to given maximal value and will have the given
	 * initial value.
	 * 
	 * @param text Text that is displayed next to the icon.
	 * @param icon Icon that is displayed.
	 * @param min Minimal value of the slider.
	 * @param max Maximal value of the slider.
	 * @param value Initial value of the slider.
	 * @param step The step that is used when incrementing/decrementing the
	 *             value of the slider.
	 * @throws IllegalArgumentException when either {@code text} or
	 *         {@code icon} is {@code null}.
	 */
	public SliderMenuBarItem(String text, Texture icon, float min, float max,
			float value, float step) {
		super(text, icon);
		
		this.min = min;
		this.max = max;
		this.val = value;
		this.step = step;
	}

	/**
 	 * Construct a new {@link SliderMenuBarItem} with given text and icon.
	 * The text and icon should not be {@code null}. The slider will range
	 * from given minimal to given maximal value and will have the given
	 * initial value.
	 * 
	 * The step will be chosen as (max - min) / 10.
	 * 
	 * @param text Text that is displayed next to the icon.
	 * @param icon Icon that is displayed.
	 * @param min Minimal value of the slider.
	 * @param max Maximal value of the slider.
	 * @param value Initial value of the slider.
	 * @throws IllegalArgumentException when either {@code text} or
	 *         {@code icon} is {@code null}.
	 * @see #SliderMenuBarItem(String, Texture, float, float, float, float)
	 */
	public SliderMenuBarItem(String text, Texture icon, float min, float max,
			float value) {
		this(text, icon, min, max, value, (max - min) / 10f);
	}
	
	@Override
	public void draw(Rectangle outline, Font renderFont) {
		super.draw(outline, renderFont);
		
		// render bar if needed
		if (this.showBar) {
			// the height of the bar depends on that of the text
			int textHeight = renderFont.getHeight(text);
			int barHeight = textHeight;
			int textBarDiff = (textHeight - barHeight) / 2;
			
			// outline
			glColor4f(SLIDER_BAR_EDGE_COLOR);
			drawBar(outline, barHeight, textBarDiff, 0, 1f);
			
			// background
			glColor4f(SLIDER_BAR_BACKGROUND_COLOR);
			drawBar(outline, barHeight, textBarDiff, SLIDER_BAR_EDGE_WIDTH, 1f);
			
			// actual bar
			glColor4f(SLIDER_BAR_COLOR);
			drawBar(outline, barHeight, textBarDiff, SLIDER_BAR_EDGE_WIDTH,
					(val - min) / (max - min));
		}
	}
	
	@Override
	public void onClick() {
		this.showBar = !this.showBar;
		this.drawText = !this.showBar;
	}
	
	@Override
	public void onDrag(int x, int y) {
		// see if the drag is inside the bar
		if (this.showBar && this.barOutline.contains(new Point(x, y))) {
			this.val = this.min + ((x - this.barOutline.getX()) /
					((float) this.barOutline.getWidth() - 2 * SLIDER_BAR_EDGE_WIDTH)) *
					(this.max - this.min);
		}
	}
	
	@Override
	public void onScroll(int dWheel) {
		// only respond if the bar is actually shown
		if (this.showBar) {
			this.val = clamp(this.val + Math.signum(dWheel) * this.step,
					this.min, this.max);
		}
	}
	
	/**
	 * Draw a bar at the correct place with certain parameters. Uses OpenGL's
	 * immediate drawing mode.
	 * 
	 * @param outline Outline of the entire menu item.
	 * @param barHeight Height of the bar (including edge).
	 * @param textBarDiff Half the difference between the height of the bar and
	 *                    the height of the text. Use to center the bar vertically.
	 * @param margin Margin of the bar to be drawn.
	 * @param width Width of the bar to be drawn, lies between 0 and 1.
	 */
	protected void drawBar(Rectangle outline, int barHeight, int textBarDiff,
			int margin, float width) {
		// calculate outline
		Rectangle barOutline = getBarOutline(outline, barHeight, textBarDiff,
				margin, width);
		
		// command OpenGL to draw the bar
		glBegin(GL_QUADS);
		{
			glVertex2d(barOutline.getX(), barOutline.getY());
			glVertex2d(barOutline.getX() + barOutline.getWidth(), barOutline.getY());
			glVertex2d(barOutline.getX() + barOutline.getWidth(),
					barOutline.getY() + barOutline.getHeight());
			glVertex2d(barOutline.getX(), barOutline.getY() + barOutline.getHeight());
		}
		glEnd();
	}
	
	protected Rectangle getBarOutline(Rectangle outline, int barHeight,
			int textBarDiff, int margin, float width) {
		// calculate the width of the bar in pixels
		int barWidth = (int) (width * (outline.getWidth() - 2 * PADDING - 2 * margin));
		// the bar should be at least one pixel wide
		if (barWidth == 0) {
			barWidth = 1;
		}
		// put together the result and return it, possibly save it
		if (margin > 0 || width < 1.0f || this.barOutline == null) {
			Rectangle barOutline = new Rectangle(outline.getX() + PADDING + margin,
					outline.getY() - PADDING - barHeight - textBarDiff + margin,
					barWidth, barHeight - 2 * margin);
			if (margin == 0 && width == 1.0f) {
				if (this.barOutline == null) {
					this.barOutline = barOutline;
				} else {
					barOutline.getBounds(this.barOutline);
				}
			}
			return barOutline;
		} else {
			this.barOutline.setBounds(outline.getX() + PADDING + margin,
					outline.getY() - PADDING - barHeight - textBarDiff + margin,
					barWidth, barHeight - 2 * margin);
			return this.barOutline;
		}
	}
	
	/** The minimal value of this slider. */
	protected float min;
	/** The maximal value of this slider. */
	protected float max;
	/** The current value of this slider. Invariant: should be between min and max. */
	protected float val;
	/** The step to change the value of the slider with. */
	protected float step;
	/** If the slider bar should be drawn, or the text. */
	protected boolean showBar;
	/** Cached outline of slider bar, for use in {@link #onDrag(int, int)}. */
	protected Rectangle barOutline;
}
