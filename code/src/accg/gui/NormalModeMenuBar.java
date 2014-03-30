package accg.gui;

import accg.ACCGProgram;
import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.components.Button;
import accg.gui.toolkit.components.TextField;
import accg.gui.toolkit.containers.Dialog;
import accg.gui.toolkit.containers.MenuBar;
import accg.gui.toolkit.containers.MenuStack;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.i18n.Messages;
import accg.objects.Block;
import accg.objects.blocks.EnterBlock;

/**
 * Menu bar for the normal mode.
 */
public class NormalModeMenuBar extends MenuBar {
	
	public NormalModeMenuBar(final MenuStack stack, final State s) {
		
		Button simulateItem = new Button(Messages.get("NormalModeMenuBar.simulate"), s.textures.iconStart); //$NON-NLS-1$
		simulateItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.simulation.skipToTime(s.time);
					for (Block b : s.world.bc) {
						if (b instanceof EnterBlock) {
							((EnterBlock) b).resetGeneratedLuggageNum();
						}
					}
					s.programMode = ProgramMode.SIMULATION_MODE;
					s.gui.updateItems();
				}
			}
		});
		add(simulateItem);
		
		Button buildItem = new Button(Messages.get("NormalModeMenuBar.build"), s.textures.iconZoomIn); //$NON-NLS-1$
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
		
		Button saveItem = new Button(Messages.get("NormalModeMenuBar.save"), s.textures.iconSave); //$NON-NLS-1$
		saveItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					TextField tf = new TextField(40);
					tf.requestFocus();
					Dialog dialog = new Dialog(Messages.get("NormalModeMenuBar.saveDialogHeader"), tf, //$NON-NLS-1$
							new Button(Messages.get("NormalModeMenuBar.ok"), s.textures.iconOk), //$NON-NLS-1$
							new Button(Messages.get("NormalModeMenuBar.cancel"), s.textures.iconExit)); //$NON-NLS-1$
					s.gui.add(dialog);
				}
			}
		});
		add(saveItem);
		
		// TODO [ws] decide whether we want settings during normal mode
		Button settingsItem = new Button(Messages.get("NormalModeMenuBar.settings"), s.textures.iconConfigure); //$NON-NLS-1$
		settingsItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.addMenuBelowOrClose(NormalModeMenuBar.this, MainStack.SETTINGS_MENU);
				}
			}
		});
		add(settingsItem);
		
		Button backItem = new Button(Messages.get("NormalModeMenuBar.back"), s.textures.iconExit); //$NON-NLS-1$
		backItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.programMode = ProgramMode.START_MODE;
					s.gui.updateItems();
					ACCGProgram.setLoadedLevel(null);
				}
			}
		});
		add(backItem);
	}
}
