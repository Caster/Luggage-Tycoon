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

	public Button rotateLeftItem;
	public Button rotateRightItem;
	public Button removeItem;
	
	public BuildingModeMenuBar(final MainStack stack, final State s) {
		
		rotateLeftItem = new Button(Messages.get("BuildingModeMenuBar.rotateLeft"),
				s.textures.iconLeft);
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
		
		rotateRightItem = new Button(Messages.get("BuildingModeMenuBar.rotateRight"),
				s.textures.iconRight);
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
		
		Button chooseBlockItem = new Button(Messages.get("BuildingModeMenuBar.chooseBlock"), s.textures.iconConfigure); //$NON-NLS-1$
		chooseBlockItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.addMenuBelowOrClose(BuildingModeMenuBar.this, MainStack.BLOCK_MENU);
				}
			}
		});
		add(chooseBlockItem);
		
		removeItem = new Button(Messages.get("BuildingModeMenuBar.remove"),
				s.textures.iconBomb, ButtonType.CHECKABLE);
		removeItem.setShortcutHint("Del");
		removeItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					// de-select the selected block to build
					stack.blockBar.setHighlightedItem(null);
					System.err.println("Enter remove mode...");
				}
			}
		});
		add(removeItem);
		
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
				((Button) c).setChecked(b == c);
			}
		}
	}
}
