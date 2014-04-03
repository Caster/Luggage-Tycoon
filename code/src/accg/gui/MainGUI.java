package accg.gui;

import static org.lwjgl.opengl.GL11.*;

import java.io.InputStream;

import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.Component;
import accg.gui.toolkit.containers.LayeredPane;
import accg.gui.toolkit.containers.MenuStack;
import accg.gui.toolkit.enums.Position;
import accg.gui.toolkit.event.KeyEvent;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.gui.toolkit.event.MouseDragEvent;
import accg.gui.toolkit.event.MouseMoveEvent;
import accg.gui.toolkit.event.MouseScrollEvent;

/**
 * This class manages the GUI of the program.
 */
public class MainGUI extends LayeredPane {
	
	private static MainGUI instance;
	private static Font guiFont;
	
	private MainStack stack;
	private MainStatusBar statusBar;
	private double statusBarShownPortion;
	
	/**
	 * Returns a shared instance of the MainGUI.
	 * 
	 * @param state The state of the program, used to instantiate the instance
	 *            if it was not instantiated yet.
	 * @return A shared instance of the MainGUI.
	 */
	public static MainGUI getInstance(State state) {
		if (instance == null) {
			instance = new MainGUI(state);
		}
		
		return instance;
	}
	
	/**
	 * Creates a new MainGUI.
	 * @param state The state of the program.
	 */
	private MainGUI(State state) {
		stack = new MainStack(state);
		add(stack);
		
		statusBar = new MainStatusBar(state);
		statusBar.setParent(this);
		updateStatusBarPosition();
		statusBar.setVisible(false);
		statusBarShownPortion = 0;
		
		setFont(loadFont());
	}
	
	@Override
	public void draw() {
		if (statusBar.isVisible()) {
			statusBarShownPortion = Math.min(1, statusBarShownPortion + 0.05);
		} else {
			statusBarShownPortion = Math.max(0, statusBarShownPortion - 0.05);
		}
		
		if (statusBarShownPortion > 0) {
			Position statusBarPos = statusBar.getPosition();
			float dx = (float) (statusBarPos.isVertical() ? (statusBarPos ==
					Position.LEFT ? -1 : 1) * (1 - statusBarShownPortion) *
					statusBar.getPreferredWidth() : 0);
			float dy = (float) (statusBarPos.isHorizontal() ? (statusBarPos ==
					Position.BOTTOM ? 1 : -1) * (1 - statusBarShownPortion) *
					statusBar.getPreferredHeight() : 0);
			
			glPushMatrix();
			glTranslatef(statusBar.getOutline().getX() + dx,
					statusBar.getOutline().getY() + dy, 0);
			statusBar.draw();
			glPopMatrix();
		}
		
		super.draw();
	}
	
	@Override
	public void layout() {
		statusBar.layoutIfNeeded();
		
		Position statusBarPos = statusBar.getPosition();
		boolean horLayout = statusBarPos.isHorizontal();
		if (horLayout) {
			statusBar.setX(0);
			statusBar.setY(statusBarPos == Position.BOTTOM ?
					getHeight() - statusBar.getPreferredHeight() : 0);
			statusBar.setHeight(statusBar.getPreferredHeight());
			statusBar.setWidth(getWidth());
		} else {
			statusBar.setX(statusBarPos == Position.LEFT ? 0 :
				getWidth() - statusBar.getPreferredWidth());
			statusBar.setY(0);
			statusBar.setHeight(getHeight());
			statusBar.setWidth(statusBar.getPreferredWidth());
		}
		
		super.layout();
	}
	
	/**
	 * Loads the font for this program and returns it.
	 * 
	 * @return The font, or <code>null</code> if it could not be loaded.
	 */
	public static Font loadFont() {
		if (guiFont != null) {
			return guiFont;
		}
		
		try {
			InputStream russoOneFontStream =
					ResourceLoader.getResourceAsStream("res/fonts/RussoOne-Regular.ttf");
			java.awt.Font russoOneAwt = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,
					russoOneFontStream);
			russoOneAwt = russoOneAwt.deriveFont(14f);
			guiFont = new TrueTypeFont(russoOneAwt, true);
			return guiFont;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Handles a key event by giving it to the GUI.
	 * 
	 * @param keyCode The LWJGL event key code.
	 * @param c The character typed.
	 * @return Whether the event has been handled by the GUI.
	 */
	public boolean handleKeyEvent(int keyCode, char c) {
		Component focused = Component.getKeyFocusElement();
		
		if (focused == null) {
			return false;
		}
		
		focused.sendEvent(new KeyEvent(keyCode, c));
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
		MouseClickEvent event = new MouseClickEvent(x, getHeight() - y);
		boolean handled = sendEvent(event);
		if (!handled) {
			handled = statusBar.sendEvent(event);
		}
		return handled;
	}
	
	/**
	 * Handles a mouse drag event by giving it to the GUI.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return Whether the event has been handled by the GUI.
	 */
	public boolean handleMouseDragEvent(int x, int y) {
		MouseDragEvent event = new MouseDragEvent(x, getHeight() - y);
		boolean handled = sendEvent(event);
		if (!handled) {
			handled = statusBar.sendEvent(event);
		}
		return handled;
	}
	
	/**
	 * Handles a mouse move event by giving it to the GUI.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return Whether the event has been handled by the GUI.
	 */
	public boolean handleMouseMoveEvent(int x, int y) {
		MouseMoveEvent event = new MouseMoveEvent(x, getHeight() - y);
		boolean handled = sendEvent(event);
		if (!handled) {
			handled = statusBar.sendEvent(event);
		}
		return handled;
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
		MouseScrollEvent event = new MouseScrollEvent(x, getHeight() - y, dWheel);
		boolean handled = sendEvent(event);
		if (!handled) {
			handled = statusBar.sendEvent(event);
		}
		return handled;
	}
	
	/**
	 * Show or hide the {@link MainStatusBar}.
	 * @param visible If the status bar should be shown.
	 */
	public void setStatusBarVisible(boolean visible) {
		statusBar.setVisible(visible);
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
	
	/**
	 * Update information shown on the {@link MainStatusBar}.
	 */
	public static void updateStatusBarInfo() {
		if (instance != null) {
			instance.statusBar.updateInfo();
		}
	}
	
	/**
	 * Update what is shown in the status bar, depending on the program mode.
	 * 
	 * @param mode The new program mode of the program.
	 */
	public void updateStatusBarMode(ProgramMode mode) {
		statusBar.updateMode(mode);
	}
	
	/**
	 * Change the position of the {@link MainStatusBar} to a suitable one, using
	 * the position and alignment of the {@link MainStack} to determine that.
	 */
	public void updateStatusBarPosition() {
		statusBar.updatePosition(stack.getPosition(), stack.getAlignment());
	}
	
	/**
	 * Returns the {@link MenuStack} of the GUI.
	 * @return The {@link MenuStack}.
	 */
	public MainStack getStack() {
		return stack;
	}
}
