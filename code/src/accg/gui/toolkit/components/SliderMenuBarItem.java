package accg.gui.toolkit.components;

import static accg.gui.toolkit.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;

import accg.gui.toolkit.Event;
import accg.gui.toolkit.GLUtils;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.enums.Presentation;
import accg.gui.toolkit.event.*;

/**
 * A SliderMenuBarItem is a {@link Button} that has a value which can
 * be queried. This value can be adjusted by the user by clicking the item
 * and then scrolling with the mouse wheel.
 * 
 * The value is then indicated by a bar at the place where the text would be.
 */
public class SliderMenuBarItem extends Button {

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
	 * @param messageKey Key of the text to display in this button in the
	 *            ResourceBundle from which text may be loaded using the
	 *            Messages class.
	 * @param defaultText Text that is displayed next to the icon in case
	 *            loading a text through the given key fails.
	 * @param icon Icon that is displayed.
	 * @param min Minimal value of the slider.
	 * @param max Maximal value of the slider.
	 * @param value Initial value of the slider.
	 * @param step The step that is used when incrementing/decrementing the
	 *             value of the slider.
	 * @throws IllegalArgumentException when either {@code text} or
	 *         {@code icon} is {@code null}.
	 */
	public SliderMenuBarItem(String messageKey, String defaultText, Texture icon,
			float min, float max, float value, float step) {
		super(messageKey, defaultText, icon);
		
		this.min = min;
		this.max = max;
		this.val = value;
		this.step = step;
		
		addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					onClick(((MouseClickEvent) e).getX(), ((MouseClickEvent) e).getY());
				}
				if (e instanceof MouseDragEvent) {
					onDrag(((MouseDragEvent) e).getX(), ((MouseDragEvent) e).getY());
				}
				if (e instanceof MouseScrollEvent) {
					onScroll(((MouseScrollEvent) e).getdWheel());
				}
			}
		});
	}

	/**
 	 * Construct a new {@link SliderMenuBarItem} with given text and icon.
	 * The text and icon should not be {@code null}. The slider will range
	 * from given minimal to given maximal value and will have the given
	 * initial value.
	 * 
	 * The step will be chosen as (max - min) / 10.
	 * 
	 * @param messageKey Key of the text to display in this button in the
	 *            ResourceBundle from which text may be loaded using the
	 *            Messages class.
	 * @param defaultText Text that is displayed next to the icon in case
	 *            loading a text through the given key fails.
	 * @param icon Icon that is displayed.
	 * @param min Minimal value of the slider.
	 * @param max Maximal value of the slider.
	 * @param value Initial value of the slider.
	 * @throws IllegalArgumentException when either {@code text} or
	 *         {@code icon} is {@code null}.
	 * @see #SliderMenuBarItem(String, String, Texture, float, float, float, float)
	 */
	public SliderMenuBarItem(String messageKey, String defaultText, Texture icon,
			float min, float max, float value) {
		this(messageKey, defaultText, icon, min, max, value, (max - min) / 10f);
	}
	
	/**
	 * Changes the value of this slider menu item.
	 * 
	 * @param value The value.
	 * @throws IllegalArgumentException If <code>value</code> is not between
	 * <code>min</code> and <code>max</code>.
	 */
	public void setValue(float value) {
		
		if (value < min || value > max) {
			throw new IllegalArgumentException("Cannot set slider to value out of range");
		}
		
		this.val = value;
	}
	
	/**
	 * Returns the value of this slider menu item.
	 * @return The value.
	 */
	public float getValue() {
		return val;
	}
	
	@Override
	public void draw() {
		super.draw();
		
		// render bar if needed
		if (this.showBar) {
			// the height of the bar depends on that of the text
			int textHeight = getFont().getLineHeight();
			int barHeight = textHeight;
			int textBarDiff = (textHeight - barHeight) / 2;
			
			// outline
			glColor4f(SLIDER_BAR_EDGE_COLOR);
			drawBar(barHeight, textBarDiff, 0, 1f);
			
			// background
			glColor4f(SLIDER_BAR_BACKGROUND_COLOR);
			drawBar(barHeight, textBarDiff, SLIDER_BAR_EDGE_WIDTH, 1f);
			
			// actual bar
			glColor4f(SLIDER_BAR_COLOR);
			drawBar(barHeight, textBarDiff, SLIDER_BAR_EDGE_WIDTH,
					(val - min) / (max - min));
		}
	}
	
	@Override
	public String getComponentName() {
		return "SliderMenuBarItem";
	}
	
	/**
	 * Handles a click event.
	 * 
	 * @param x The mouse x-coordinate.
	 * @param y The mouse y-coordinate.
	 */
	protected void onClick(int x, int y) {
		// when clicking inside the bar, change the value
		if (this.showBar && this.cachedBarOutline.contains(x, y)) {
			updateValueFromPoint(x, y);
		} else {
			// toggle between showing the bar and showing text
			this.showBar = !this.showBar;
			this.drawText = !this.showBar;
		}
	}
	
	/**
	 * Handles a drag event.
	 * 
	 * @param x The mouse x-coordinate.
	 * @param y The mouse y-coordinate.
	 */
	protected void onDrag(int x, int y) {
		// see if the drag is inside the bar
		if (this.showBar && this.cachedBarOutline.contains(x, y)) {
			updateValueFromPoint(x, y);
		}
	}

	
	/**
	 * Handles a scroll event.
	 * 
	 * @param dWheel The amount of scroll steps.
	 */
	protected void onScroll(int dWheel) {
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
	 * @param barHeight Height of the bar (including edge).
	 * @param textBarDiff Half the difference between the height of the bar and
	 *                    the height of the text. Use to center the bar vertically.
	 * @param margin Margin of the bar to be drawn.
	 * @param width Width of the bar to be drawn, lies between 0 and 1.
	 */
	protected void drawBar(int barHeight, int textBarDiff, int margin, float width) {
		// calculate outline
		Rectangle barOutline = getBarOutline(barHeight,
				textBarDiff, margin, width);
		
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
	
	/**
	 * Return an outline of the slider bar.
	 * 
	 * @param barHeight Height of the bar (including edge).
	 * @param textBarDiff Half the difference between the height of the bar and
	 *                    the height of the text. Use to center the bar vertically.
	 * @param margin Margin of the bar to be drawn.
	 * @param width Width of the bar to be drawn, lies between 0 and 1.
	 * @return The computed outline.
	 * @see #drawBar(int, int, int, float)
	 */
	protected Rectangle getBarOutline(int barHeight, int textBarDiff, int margin, float width) {
		// calculate the width of the bar in pixels
		int barWidth;
		switch (presentation) {
			default :
			case ICON_ABOVE_TEXT :
				barWidth = (int) (width * (outline.getWidth() - 2 * PADDING -
						2 * margin));
				break;
			case ICON_LEFT_TEXT :
				// since barHeight == textHeight and the width of the icon is
				// precisely that, we misuse this parameter a bit here...
				barWidth = (int) (width * (outline.getWidth() - 3 * PADDING -
						2 * margin - barHeight));
				break;
		}
		// the bar should be at least one pixel wide
		if (barWidth == 0) {
			barWidth = 1;
		}
		
		// put together the result
		Rectangle barOutline = new Rectangle(PADDING + margin,
				outline.getHeight() - PADDING - barHeight - textBarDiff + margin,
				barWidth, barHeight - 2 * margin);
		
		
		// presentation specific tweaks
		if (presentation == Presentation.ICON_LEFT_TEXT) {
			barOutline.setX(barOutline.getX() + PADDING + barHeight);
		}
		
		// possibly save the result if it is the actual outline
		if (margin == 0 && width == 1.0f) {
			if (this.cachedBarOutline == null) {
				this.cachedBarOutline = barOutline;
			} else {
				barOutline.getBounds(this.cachedBarOutline);
			}
		}
		
		return barOutline;
	}
	
	/**
	 * Given a point inside the bar outline, update the value of this slider to
	 * make the bar be filled up to the given point.
	 * 
	 * @param x X-coordinate of point in bar outline.
	 * @param y Y-coordinate of point in bar outline.
	 */
	protected void updateValueFromPoint(int x, int y) {
		this.val = this.min + ((x - this.cachedBarOutline.getX()) /
				((float) this.cachedBarOutline.getWidth() - 2 * SLIDER_BAR_EDGE_WIDTH)) *
				(this.max - this.min);
		
		this.val = GLUtils.clamp(this.val, 0.1f, 2.0f);
		
		sendEvent(new ValueChangeEvent());
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
	protected Rectangle cachedBarOutline;
}
