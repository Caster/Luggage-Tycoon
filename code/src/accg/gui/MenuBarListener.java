package accg.gui;

import accg.gui.MenuBar.Alignment;
import accg.gui.MenuBar.Position;
import accg.gui.MenuBar.Presentation;

/**
 * Listener that can be used to hide a {@link MenuBar} when its parent is
 * hidden for example. The child can listen for a {@link #onHide()} event
 * from its parent and hide when that happens.
 */
public interface MenuBarListener {

	/**
	 * Fired when the {@link MenuBar}'s {@link MenuBar#setVisible(boolean)}
	 * is called with a value of {@code false}. A child may want to hide
	 * as well if this happens.
	 */
	public void onHide();
	
	/**
	 * Fired when the alignment of a {@link MenuBar} is changed.
	 * 
	 * @param newAlignment New alignment of the {@link MenuBar}.
	 */
	public void onAlignmentChanged(Alignment newAlignment);
	
	/**
	 * Fired when the position of a {@link MenuBar} is changed.
	 * 
	 * @param newPosition New position of the {@link MenuBar}.
	 */
	public void onPositionChanged(Position newPosition);
	
	/**
	 * Fired when the presentation of a {@link MenuBar} is changed.
	 * 
	 * @param newPresentation New presentation of the {@link MenuBar}.
	 */
	public void onPresentationChanged(Presentation newPresentation);
	
	/**
	 * Fired when the {@link MenuBar} has to handle a resize event.
	 * 
	 * @param width New width of the display.
	 * @param height New height of the display.
	 * @see {@link MenuBar#handleResizeEvent(int, int)}
	 */
	public void onResize(int width, int height);
	
	/**
	 * Fired when the parent wants its child to hide, that is,
	 * {@code setVisible(false)}. This may be triggered when some other
	 * child on the same level wants to show itself.
	 */
	public void requestHide();
}
