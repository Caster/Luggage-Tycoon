package accg.gui;

import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.*;
import accg.gui.toolkit.event.MouseClickEvent;

/**
 * Menu bar for the simulation mode.
 */
public class SimulationModeMenuBar extends MenuBar {
	
	public SimulationModeMenuBar(final MenuStack stack, final State s) {

		Button simulateItem = new Button("Stop", s.textures.iconStop);
		simulateItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.simulation.clearObjects(s);
					s.programMode = ProgramMode.NORMAL_MODE;
					s.gui.updateItems();
				}
			}
		});
		add(simulateItem);
	}
}
