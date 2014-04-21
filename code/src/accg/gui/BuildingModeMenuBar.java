package accg.gui;

import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.components.Button;
import accg.gui.toolkit.containers.MenuBar;
import accg.gui.toolkit.enums.ButtonType;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.i18n.Messages;
import accg.objects.Block;

/**
 * Menu bar for the building mode.
 */
public class BuildingModeMenuBar extends MenuBar {

	/**
	 * Button in MenuBar that will trigger an action in which the ShadowBlock's
	 * orientation is rotated to the left.
	 */
	public Button rotateLeftItem;
	/**
	 * Button in MenuBar that will trigger an action in which the ShadowBlock's
	 * orientation is rotated to the right.
	 */
	public Button rotateRightItem;
	/**
	 * Button in MenuBar that will switch the mode to "deleting blocks".
	 */
	public Button removeItem;
	/**
	 * Button in MenuBar that when clicked will delete all deletable blocks in the scene.
	 */
	public Button removeAllItem;
	
	/**
	 * Last created instance of this menu bar.
	 */
	protected static BuildingModeMenuBar instance;
	/**
	 * State of the program.
	 */
	protected State state;
	
	/**
	 * A MenuBar that is shown when the program mode is {@link ProgramMode#BUILDING_MODE}.
	 * 
	 * @param stack Stack in which menu is put.
	 * @param s State of the program.
	 */
	public BuildingModeMenuBar(final MainStack stack, final State s) {
		
		if (instance == null) {
			instance = this;
		}
		
		this.state = s;
		
		rotateLeftItem = new Button(Messages.get("BuildingModeMenuBar.rotateLeft"),
				s.textures.iconLeft);
		rotateLeftItem.setShortcutHint("Shift+R");
		rotateLeftItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					if (s.programMode == ProgramMode.BUILDING_MODE &&
							s.shadowBlock != null && s.shadowBlock.getOrientation() != null) {
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
					
					// we are now going to remove blocks
					s.removingBlocks = true;
					
					// hide the shadow block
					s.shadowBlock.setConveyorBlockType(null);
				}
			}
		});
		add(removeItem);
		
		removeAllItem = new Button(Messages.get("BuildingModeMenuBar.removeAll"),
				s.textures.iconBomb, ButtonType.NORMAL);
		removeAllItem.setShortcutHint("Shift+Del");
		removeAllItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					BuildingModeMenuBar.handleRemoveAllEvent();
				}
			}
		});
		add(removeAllItem);
		
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
	
	/**
	 * Handle click on the "remove all" menu item.
	 */
	public static void handleRemoveAllEvent() {
		if (instance == null) {
			return;
		}
		
		for (Block b : instance.state.world.bc) {
			if (b.isDeletable()) {
				b.onDestroy();
				instance.state.world.removeBlock(b.getX(), b.getY(), b.getZ());
			}
		}
	}
}
