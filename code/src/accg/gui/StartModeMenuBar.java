package accg.gui;

import org.newdawn.slick.util.ResourceLoader;

import accg.ACCGProgram;
import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.components.Button;
import accg.gui.toolkit.components.Label;
import accg.gui.toolkit.components.List;
import accg.gui.toolkit.containers.Dialog;
import accg.gui.toolkit.containers.MenuBar;
import accg.gui.toolkit.containers.MenuStack;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.i18n.Messages;
import accg.io.Level;
import accg.io.SavedGameManager;
import accg.objects.Orientation;
import accg.objects.World;
import accg.objects.blocks.EnterBlock;
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

					// switch to normal mode
					ProgramMode oldMode = s.programMode;
					s.programMode = ProgramMode.NORMAL_MODE;
					s.gui.updateItems();
					
					try {
						Level level = new Level(ResourceLoader.getResourceAsStream("/res/levels/sandbox.lt"));
						level.loadInState(s);
						s.loadedBuiltinLevel = false;
						ACCGProgram.setLoadedLevel("sandbox.lt");
					} catch (Exception e2) {
						// something went wrong; go back to the old mode
						s.programMode = oldMode;
						s.gui.updateItems();
						throw new RuntimeException("Something went severely wrong; I couldn't open the sandbox file", e2);
					}
				}
			}
		});
		add(sandBoxItem);
		
		Button settingsItem = new Button(Messages.get("StartModeMenuBar.settings"), s.textures.iconConfigure); //$NON-NLS-1$
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
