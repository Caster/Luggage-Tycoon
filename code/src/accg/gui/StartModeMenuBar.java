package accg.gui;

import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.Button;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.MenuBar;
import accg.gui.toolkit.MenuStack;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.i18n.Messages;
import accg.objects.World;
import accg.simulation.Simulation;

/**
 * Menu bar for the start mode (that is, the main menu).
 */
public class StartModeMenuBar extends MenuBar {
	
	public StartModeMenuBar(final MenuStack stack, final State s) {
		
		Button chooseLevelItem = new Button(Messages.get("StartModeMenuBar.chooseLevel"), s.textures.iconStart); //$NON-NLS-1$
		chooseLevelItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.gui.add(new ChooseLevelDialog(s));
				}
			}
		});
		add(chooseLevelItem);
		
		Button openItem = new Button(Messages.get("StartModeMenuBar.open"), s.textures.iconOpen); //$NON-NLS-1$
		openItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.gui.add(new OpenDialog(s));
				}
			}
		});
		add(openItem);
		
		Button sandBoxItem = new Button(Messages.get("StartModeMenuBar.sandBox"), s.textures.iconOpen); //$NON-NLS-1$
		sandBoxItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.simulation = new Simulation(s);
					s.world = new World(s);
					s.programMode = ProgramMode.NORMAL_MODE;
					s.gui.updateItems();
				}
			}
		});
		add(sandBoxItem);
		
		Button settingsItem = new Button(Messages.get("NormalModeMenuBar.settings"), s.textures.iconConfigure); //$NON-NLS-1$
		settingsItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.addMenuBelowOrClose(StartModeMenuBar.this, MainStack.SETTINGS_MENU);
				}
			}
		});
		add(settingsItem);
		
		Button quitItem = new Button(Messages.get("StartModeMenuBar.quit"), s.textures.iconExit); //$NON-NLS-1$
		quitItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.escPressed = true;
				}
			}
		});
		add(quitItem);
	}
}
