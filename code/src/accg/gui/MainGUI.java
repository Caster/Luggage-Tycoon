package accg.gui;

import java.io.InputStream;

import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.MenuStack;
import accg.gui.toolkit.event.*;

/**
 * This class manages the GUI.
 */
public class MainGUI extends MenuStack {
	
	public static final String SETTINGS_MENU = "settings";
	public static final String POSITION_MENU = "position";
	public static final String ALIGNMENT_MENU = "alignment";
	public static final String PRESENTATION_MENU = "presentation";
	
	/**
	 * The state of the program.
	 */
	private State state;
	
	/**
	 * Creates a new MainGUI.
	 * @param state The state of the program.
	 */
	public MainGUI(State state) {
		this.state = state;
		
		setFont(loadFont());
		
		addToCollection(ProgramMode.NORMAL_MODE, new NormalModeMenuBar(this, state));
		addToCollection(ProgramMode.BUILDING_MODE, new BuildingModeMenuBar(this, state));
		addToCollection(ProgramMode.SIMULATION_MODE, new SimulationModeMenuBar(this, state));
		addToCollection(SETTINGS_MENU, new SettingsMenuBar(this, state));
		addToCollection(POSITION_MENU, new PositionMenuBar(this, state));
		addToCollection(ALIGNMENT_MENU, new AlignmentMenuBar(this, state));
		addToCollection(PRESENTATION_MENU, new PresentationMenuBar(this, state));
	}
	
	/**
	 * Updates the GUI to the current state.
	 * 
	 * This method should be called whenever something changes in the
	 * {@link State} that causes the GUI to need updating. For example,
	 * this includes setting another mode.
	 */
	public void updateItems() {
		setMenuBar(state.programMode);
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
		return sendEvent(new MouseScrollEvent(x, y, dWheel));
	}
}
