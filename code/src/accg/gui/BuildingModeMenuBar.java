package accg.gui;

import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.*;
import accg.gui.toolkit.event.MouseClickEvent;

/**
 * Menu bar for the building mode.
 */
public class BuildingModeMenuBar extends MenuBar {
	
	public BuildingModeMenuBar(final MenuStack stack, final State s) {
		
		Button backItem = new Button("Back", s.textures.iconExit);
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
		
		add(new Button("Rotate", s.textures.iconMouse));
		add(new Button("Straight", s.textures.iconExit));
		add(new Button("Up", s.textures.iconGoUp));
		add(new Button("Down", s.textures.iconGoDown));
		add(new Button("Left", s.textures.iconGoLeft));
		add(new Button("Right", s.textures.iconGoRight));
	}
}
