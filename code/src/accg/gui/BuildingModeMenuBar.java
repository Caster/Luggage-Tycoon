package accg.gui;

import org.lwjgl.input.Keyboard;

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
		
		Button rotateLeftItem = new Button(Messages.get("BuildingModeMenuBar.rotateLeft"), s.textures.iconMouse); //$NON-NLS-1$
		rotateLeftItem.setShortcutHint("Shift+R");
		rotateLeftItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					if (s.programMode == ProgramMode.BUILDING_MODE &&
							s.shadowBlock != null) {
						s.shadowBlock.setOrientation(
								s.shadowBlock.getOrientation().rotateLeft());
					}
				}
			}
		});
		add(rotateLeftItem);
		Button rotateRightItem = new Button(Messages.get("BuildingModeMenuBar.rotateRight"), s.textures.iconMouse); //$NON-NLS-1$
		rotateRightItem.setShortcutHint("R");
		rotateRightItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					if (s.programMode == ProgramMode.BUILDING_MODE &&
							s.shadowBlock != null) {
						s.shadowBlock.setOrientation(
								s.shadowBlock.getOrientation().rotateRight());
					}
				}
			}
		});
		add(rotateRightItem);
		
		add(new Button(Messages.get("BuildingModeMenuBar.straight"), s.textures.iconBeltFlat)); //$NON-NLS-1$
		add(new Button(Messages.get("BuildingModeMenuBar.up"), s.textures.iconBeltAscending)); //$NON-NLS-1$
		add(new Button(Messages.get("BuildingModeMenuBar.down"), s.textures.iconBeltDescending)); //$NON-NLS-1$
		add(new Button(Messages.get("BuildingModeMenuBar.left"), s.textures.iconBeltLeft)); //$NON-NLS-1$
		add(new Button(Messages.get("BuildingModeMenuBar.right"), s.textures.iconBeltRight)); //$NON-NLS-1$
		
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
