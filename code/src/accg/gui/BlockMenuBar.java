package accg.gui;

import accg.State;
import accg.gui.toolkit.Component;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.components.Button;
import accg.gui.toolkit.containers.MenuBar;
import accg.gui.toolkit.enums.ButtonType;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.objects.blocks.ConveyorBlock.ConveyorBlockType;

/**
 * Menu bar in which the user can choose the type of block to build.
 */
public class BlockMenuBar extends MenuBar {
	
	Button straightButton;
	Button ascendingButton;
	Button descendingButton;
	Button leftButton;
	Button rightButton;
	
	public BlockMenuBar(final MainStack stack, final State s) {
		
		straightButton = new Button("BuildingModeMenuBar.straight", null,
				s.textures.iconBeltFlat,
				ButtonType.CHECKABLE_UNIQUE);
		straightButton.setShortcutHint("C+F");
		straightButton.setChecked(true);
		straightButton.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.buildBar.removeItem.setChecked(false);
					s.removingBlocks = false;
					s.shadowBlock.setConveyorBlockType(ConveyorBlockType.FLAT);
				}
			}
		});
		add(straightButton);
		
		ascendingButton = new Button("BuildingModeMenuBar.up", null,
				s.textures.iconBeltAscending,
				ButtonType.CHECKABLE_UNIQUE);
		ascendingButton.setShortcutHint("C+A");
		ascendingButton.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.buildBar.removeItem.setChecked(false);
					s.removingBlocks = false;
					s.shadowBlock.setConveyorBlockType(ConveyorBlockType.ASCENDING);
				}
			}
		});
		add(ascendingButton);
		
		descendingButton = new Button("BuildingModeMenuBar.down", null,
				s.textures.iconBeltDescending,
				ButtonType.CHECKABLE_UNIQUE);
		descendingButton.setShortcutHint("C+D");
		descendingButton.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.buildBar.removeItem.setChecked(false);
					s.removingBlocks = false;
					s.shadowBlock.setConveyorBlockType(ConveyorBlockType.DESCENDING);
				}
			}
		});
		add(descendingButton);
		
		leftButton = new Button("BuildingModeMenuBar.left", null,
				s.textures.iconBeltLeft,
				ButtonType.CHECKABLE_UNIQUE);
		leftButton.setShortcutHint("C+B");
		leftButton.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.buildBar.removeItem.setChecked(false);
					s.removingBlocks = false;
					s.shadowBlock.setConveyorBlockType(ConveyorBlockType.BEND_LEFT);
				}
			}
		});
		add(leftButton);
		
		rightButton = new Button("BuildingModeMenuBar.right", null,
				s.textures.iconBeltRight,
				ButtonType.CHECKABLE_UNIQUE);
		rightButton.setShortcutHint("C+B,B");
		rightButton.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.buildBar.removeItem.setChecked(false);
					s.removingBlocks = false;
					s.shadowBlock.setConveyorBlockType(ConveyorBlockType.BEND_RIGHT);
				}
			}
		});
		add(rightButton);
	}
	
	public void setHighlightedItem(Button b) {
		for (Component c : getChildren()) {
			if (c instanceof Button) {
				((Button) c).setChecked(b == c);
			}
		}
	}
}
