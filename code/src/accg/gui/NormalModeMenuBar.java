package accg.gui;

import accg.State;
import accg.gui.toolkit.MenuBar;
import accg.gui.toolkit.MenuBarItem;

/**
 * Menu bar for the normal mode.
 */
public class NormalModeMenuBar extends MenuBar {
	public NormalModeMenuBar(State s) {
		add(new MenuBarItem("Quit", s.textures.iconExit));
	}
}
