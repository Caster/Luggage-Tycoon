package accg.gui;

import accg.State;
import accg.gui.toolkit.MenuBar;
import accg.gui.toolkit.MenuBarItem;
import accg.gui.toolkit.SliderMenuBarItem;

/**
 * Menu bar for the normal mode.
 */
public class NormalModeMenuBar extends MenuBar {
	public NormalModeMenuBar(State s) {
		add(new MenuBarItem("Simulate", s.textures.iconStart));
		add(new MenuBarItem("Build", s.textures.iconZoomIn));
		add(new MenuBarItem("Open", s.textures.iconOpen));
		add(new MenuBarItem("Save", s.textures.iconSave));
		add(new MenuBarItem("Settings", s.textures.iconConfigure));
		add(new MenuBarItem("Quit", s.textures.iconExit));
		add(new SliderMenuBarItem("Test!", s.textures.iconExit, 0, 10, 5, 1));
	}
}
