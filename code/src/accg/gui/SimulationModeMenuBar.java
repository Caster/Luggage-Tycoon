package accg.gui;

import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.*;
import accg.gui.toolkit.components.Button;
import accg.gui.toolkit.containers.MenuBar;
import accg.gui.toolkit.containers.MenuStack;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.i18n.Messages;

/**
 * Menu bar for the simulation mode.
 */
public class SimulationModeMenuBar extends MenuBar {
	
	public SimulationModeMenuBar(final MenuStack stack, final State s) {

		Button simulateItem = new Button(Messages.get("SimulationModeMenuBar.stop"), s.textures.iconStop); //$NON-NLS-1$
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
