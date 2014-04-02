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
 * Menu bar for the building mode.
 */
public class BuildingModeMenuBar extends MenuBar {
	
	public BuildingModeMenuBar(final MenuStack stack, final State s) {
		
		Button rotateItem = new Button(Messages.get("BuildingModeMenuBar.rotate"), s.textures.iconMouse); //$NON-NLS-1$
		rotateItem.setShortcutHint("R");
		rotateItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					System.err.println("Rotate!");
				}
			}
		});
		add(rotateItem);
		
		add(new Button(Messages.get("BuildingModeMenuBar.straight"), s.textures.iconExit)); //$NON-NLS-1$
		add(new Button(Messages.get("BuildingModeMenuBar.up"), s.textures.iconGoUp)); //$NON-NLS-1$
		add(new Button(Messages.get("BuildingModeMenuBar.down"), s.textures.iconGoDown)); //$NON-NLS-1$
		add(new Button(Messages.get("BuildingModeMenuBar.left"), s.textures.iconGoLeft)); //$NON-NLS-1$
		add(new Button(Messages.get("BuildingModeMenuBar.right"), s.textures.iconGoRight)); //$NON-NLS-1$
		
		Button backItem = new Button(Messages.get("BuildingModeMenuBar.back"), s.textures.iconExit); //$NON-NLS-1$
		backItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.programMode = ProgramMode.NORMAL_MODE;
					s.gui.updateItems();
					s.gui.setStatusBarVisible(false);
				}
			}
		});
		add(backItem);
	}
}
