package accg.gui;

import java.io.InputStream;

import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import accg.State;
import accg.gui.toolkit.Component;
import accg.gui.toolkit.containers.LayeredPane;
import accg.gui.toolkit.event.KeyEvent;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.gui.toolkit.event.MouseDragEvent;
import accg.gui.toolkit.event.MouseMoveEvent;
import accg.gui.toolkit.event.MouseScrollEvent;

/**
 * This class manages the GUI of the program.
 */
public class MainGUI extends LayeredPane {
	
	private MainStack stack;
	
	/**
	 * Creates a new MainGUI.
	 * @param state The state of the program.
	 */
	public MainGUI(State state) {
		
		stack = new MainStack(state);
		add(stack);
		
		setFont(loadFont());
	}
	
	/**
	 * Loads the font for this program and returns it.
	 * 
	 * @return The font, or <code>null</code> if it could not be loaded.
	 */
	public static Font loadFont() {
		try {
			InputStream russoOneFontStream =
					ResourceLoader.getResourceAsStream("res/fonts/RussoOne-Regular.ttf");
			java.awt.Font russoOneAwt = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,
					russoOneFontStream);
			russoOneAwt = russoOneAwt.deriveFont(14f);
			return new TrueTypeFont(russoOneAwt, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Handles a key event by giving it to the GUI.
	 * 
	 * @param c The character typed.
	 * (TODO This is quite ugly; investigate other ways to do this.)
	 * @return Whether the event has been handled by the GUI.
	 */
	public boolean handleKeyEvent(char c) {
		Component focused = Component.getKeyFocusElement();
		
		if (focused == null) {
			return false;
		}
		
		focused.sendEvent(new KeyEvent(c));
		return true;
	}
	
	/**
	 * Handles a mouse click event by giving it to the GUI.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return Whether the event has been handled by the GUI.
	 */
	public boolean handleMouseClickEvent(int x, int y) {
		return sendEvent(new MouseClickEvent(x, getHeight() - y));
	}
	
	/**
	 * Handles a mouse drag event by giving it to the GUI.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return Whether the event has been handled by the GUI.
	 */
	public boolean handleMouseDragEvent(int x, int y) {
		return sendEvent(new MouseDragEvent(x, getHeight() - y));
	}
	
	/**
	 * Handles a mouse move event by giving it to the GUI.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return Whether the event has been handled by the GUI.
	 */
	public boolean handleMouseMoveEvent(int x, int y) {
		return sendEvent(new MouseMoveEvent(x, getHeight() - y));
	}
	
	/**
	 * Handles a mouse scroll event by giving it to the GUI.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param dWheel The amount of scroll steps.
	 * @return Whether the event has been handled by the GUI.
	 */
	public boolean handleMouseScrollEvent(int x, int y, int dWheel) {
		return sendEvent(new MouseScrollEvent(x, getHeight() - y, dWheel));
	}
	
	/**
	 * Updates the GUI to the current state.
	 * 
	 * This method should be called whenever something changes in the
	 * {@link State} that causes the GUI to need updating. For example,
	 * this includes setting another mode.
	 */
	public void updateItems() {
		stack.updateItems();
	}
}
