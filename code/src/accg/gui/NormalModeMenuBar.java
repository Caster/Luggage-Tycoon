package accg.gui;

import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.*;
import accg.gui.toolkit.event.MouseClickEvent;

/**
 * Menu bar for the normal mode.
 */
public class NormalModeMenuBar extends MenuBar {
	public NormalModeMenuBar(final State s) {
		add(new MenuBarItem("Simulate", s.textures.iconStart));
		
		MenuBarItem buildItem = new MenuBarItem("Build", s.textures.iconZoomIn);
		buildItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.programMode = ProgramMode.BUILDING_MODE;
					s.gui.updateItems();
				}
			}
		});
		add(buildItem);
		
		add(new MenuBarItem("Open", s.textures.iconOpen));
		add(new MenuBarItem("Save", s.textures.iconSave));
		add(new MenuBarItem("Settings", s.textures.iconConfigure));
		
		MenuBarItem quitItem = new MenuBarItem("Quit", s.textures.iconExit);
		quitItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					System.err.println("QUIT!");
				}
			}
		});
		add(quitItem);
	}
}
