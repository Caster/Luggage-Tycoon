package accg.gui;

import accg.State;
import accg.gui.toolkit.MenuBar;
import accg.gui.toolkit.MenuBarItem;

/**
 * Menu bar for the simulation mode.
 */
public class SimulationModeMenuBar extends MenuBar {
	public SimulationModeMenuBar(State s) {
		add(new MenuBarItem("Stop", s.textures.iconStop));
	}
}
