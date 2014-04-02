package accg.gui;

import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.Component;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.components.Button;
import accg.gui.toolkit.containers.MenuBar;
import accg.gui.toolkit.containers.MenuStack;
import accg.gui.toolkit.enums.ButtonType;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.i18n.Messages;
import accg.objects.blocks.ConveyorBlock.ConveyorBlockType;

/**
 * Menu bar for the building mode.
 */
public class BuildingModeMenuBar extends MenuBar {
	
	Button straightButton;
	Button ascendingButton;
	Button descendingButton;
	Button leftButton;
	Button rightButton;
	
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
		
		straightButton = new Button(Messages.get("BuildingModeMenuBar.straight"),
				s.textures.iconBeltFlat,
				ButtonType.CHECKABLE_UNIQUE);
		straightButton.setShortcutHint("C+F");
		straightButton.setChecked(true);
		straightButton.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.shadowBlock.setConveyorBlockType(ConveyorBlockType.FLAT);
				}
			}
		});
		add(straightButton);
		
		ascendingButton = new Button(Messages.get("BuildingModeMenuBar.up"),
				s.textures.iconBeltAscending,
				ButtonType.CHECKABLE_UNIQUE);
		ascendingButton.setShortcutHint("C+A");
		ascendingButton.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.shadowBlock.setConveyorBlockType(ConveyorBlockType.ASCENDING);
				}
			}
		});
		add(ascendingButton);
		
		descendingButton = new Button(Messages.get("BuildingModeMenuBar.down"),
				s.textures.iconBeltDescending,
				ButtonType.CHECKABLE_UNIQUE);
		descendingButton.setShortcutHint("C+D");
		descendingButton.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.shadowBlock.setConveyorBlockType(ConveyorBlockType.DESCENDING);
				}
			}
		});
		add(descendingButton);
		
		leftButton = new Button(Messages.get("BuildingModeMenuBar.left"),
				s.textures.iconBeltLeft,
				ButtonType.CHECKABLE_UNIQUE);
		leftButton.setShortcutHint("C+B");
		leftButton.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.shadowBlock.setConveyorBlockType(ConveyorBlockType.BEND_LEFT);
				}
			}
		});
		add(leftButton);
		
		rightButton = new Button(Messages.get("BuildingModeMenuBar.right"),
				s.textures.iconBeltRight,
				ButtonType.CHECKABLE_UNIQUE);
		rightButton.setShortcutHint("C+B,B");
		rightButton.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.shadowBlock.setConveyorBlockType(ConveyorBlockType.BEND_RIGHT);
				}
			}
		});
		add(rightButton);
		
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
	
	public void setHighlightedItem(Button b) {
		for (Component c : getChildren()) {
			if (c instanceof Button) {
				((Button) c).setChecked(false);
			}
		}
		
		b.setChecked(true);
	}
}
