package accg.gui;

import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.*;
import accg.gui.toolkit.event.MouseClickEvent;

/**
 * Menu bar for the building mode.
 */
public class BuildingModeMenuBar extends MenuBar {
	public BuildingModeMenuBar(final State s) {
		
		MenuBarItem backItem = new MenuBarItem("Back", s.textures.iconExit);
		backItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.programMode = ProgramMode.NORMAL_MODE;
					s.gui.updateItems();
				}
			}
		});
		add(backItem);
		
		add(new MenuBarItem("Rotate", s.textures.iconMouse));
		add(new MenuBarItem("Straight", s.textures.iconExit));
		add(new MenuBarItem("Up", s.textures.iconGoUp));
		add(new MenuBarItem("Down", s.textures.iconGoDown));
		add(new MenuBarItem("Left", s.textures.iconGoLeft));
		add(new MenuBarItem("Right", s.textures.iconGoRight));
	}
}
