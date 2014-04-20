package accg.gui;

import accg.ACCGProgram;
import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.components.Button;
import accg.gui.toolkit.containers.MenuBar;
import accg.gui.toolkit.containers.MenuStack;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.i18n.Messages;
import accg.objects.Block;
import accg.objects.blocks.EnterBlock;
import accg.objects.blocks.LeaveBlock;

/**
 * Menu bar for the normal mode.
 */
public class NormalModeMenuBar extends MenuBar {
	
	/**
	 * Construct a new menu bar for the normal mode. In this mode, the building
	 * mode and simulation mode can be selected. Also, the current world can be
	 * saved for later use.
	 * 
	 * @param stack Stack in which this menu is put.
	 * @param s State of the program.
	 */
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
						if (b instanceof LeaveBlock) {
							((LeaveBlock) b).resetArrivedLuggageCount();
						}
					}
					s.world.resetLostLuggageCount();
					s.programMode = ProgramMode.SIMULATION_MODE;
					s.gui.updateItems();
					
					s.gui.updateStatusBarMode(s.programMode);
					MainGUI.updateStatusBarInfo();
					s.gui.setStatusBarVisible(true);
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
					
					s.gui.updateStatusBarMode(s.programMode);
					MainGUI.updateStatusBarInfo();
					s.gui.setStatusBarVisible(true);
				}
			}
		});
		add(buildItem);
		
		Button saveItem = new Button(Messages.get("NormalModeMenuBar.save"), s.textures.iconSave); //$NON-NLS-1$
		saveItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.gui.add(new SaveDialog(s));
				}
			}
		});
		add(saveItem);
		
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
