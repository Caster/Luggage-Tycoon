package accg.gui;

import accg.State;
import accg.gui.toolkit.*;
import accg.gui.toolkit.event.MouseClickEvent;

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
		
		MenuBarItem quitItem = new MenuBarItem("Quit", s.textures.iconExit);
		quitItem.addListener(new Listener() {
			
			@Override
			public boolean event(Event e) {
				if (e instanceof MouseClickEvent) {
					System.err.println("QUIT!");
				}
				return true;
			}
		});
		add(quitItem);
		
		add(new SliderMenuBarItem("Test!", s.textures.iconExit, 0, 10, 5, 1));
	}
}
