package accg.gui;

import accg.State;
import accg.gui.toolkit.MenuBar;
import accg.gui.toolkit.MenuBarItem;

/**
 * Menu bar for the building mode.
 */
public class BuildingModeMenuBar extends MenuBar {
	public BuildingModeMenuBar(State s) {
		add(new MenuBarItem("Back", s.textures.iconExit));
		add(new MenuBarItem("Rotate", s.textures.iconMouse));
		add(new MenuBarItem("Straight", s.textures.iconExit));
		add(new MenuBarItem("Up", s.textures.iconExit));
		add(new MenuBarItem("Down", s.textures.iconExit));
		add(new MenuBarItem("Left", s.textures.iconExit));
		add(new MenuBarItem("Right", s.textures.iconExit));
	}
}
