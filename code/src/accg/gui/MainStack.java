package accg.gui;

import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.containers.MenuStack;
import accg.gui.toolkit.enums.Alignment;
import accg.gui.toolkit.enums.Position;
import accg.gui.toolkit.enums.Presentation;

/**
 * This class manages the menu stack in the GUI.
 */
public class MainStack extends MenuStack {
	
	public static final String SETTINGS_MENU = "settings";
	public static final String POSITION_MENU = "position";
	public static final String ALIGNMENT_MENU = "alignment";
	public static final String PRESENTATION_MENU = "presentation";
	public static final String BLOCK_MENU = "block";

	public BuildingModeMenuBar buildBar;
	public BlockMenuBar blockBar;
	
	/**
	 * The state of the program.
	 */
	private State state;
	
	/**
	 * Creates a new MainStack.
	 * @param state The state of the program.
	 */
	public MainStack(State state) {
		this.state = state;

		addToCollection(ProgramMode.START_MODE, new StartModeMenuBar(this, state));
		addToCollection(ProgramMode.NORMAL_MODE, new NormalModeMenuBar(this, state));
		addToCollection(ProgramMode.BUILDING_MODE, buildBar = new BuildingModeMenuBar(this, state));
		addToCollection(ProgramMode.SIMULATION_MODE, new SimulationModeMenuBar(this, state));
		addToCollection(SETTINGS_MENU, new SettingsMenuBar(this, state));
		addToCollection(POSITION_MENU, new PositionMenuBar(this, state));
		addToCollection(ALIGNMENT_MENU, new AlignmentMenuBar(this, state));
		addToCollection(PRESENTATION_MENU, new PresentationMenuBar(this, state));
		addToCollection(BLOCK_MENU, blockBar = new BlockMenuBar(this, state));
		
		int alignmentId = state.prefs.getInt("menu.alignment", State.DEF_MENU_ALIGNMENT);
		setAlignment(Alignment.values()[alignmentId]);
		
		int positionId = state.prefs.getInt("menu.position", State.DEF_MENU_POSITION);
		setPosition(Position.values()[positionId]);
		
		int presentationId = state.prefs.getInt("menu.presentation", State.DEF_MENU_PRESENTATION);
		setPresentation(Presentation.values()[presentationId]);
		
		updateItems();
	}
	
	/**
	 * Updates the menu stack to the current state.
	 */
	public void updateItems() {
		addMenuOnPosition(0, state.programMode);
		
		if (state.shadowBlock.getConveyorBlockType() == null) {
			blockBar.setHighlightedItem(null);
			return;
		}
		
		switch (state.shadowBlock.getConveyorBlockType()) {
		case FLAT:
			blockBar.setHighlightedItem(blockBar.straightButton);
			break;
		case ASCENDING:
			blockBar.setHighlightedItem(blockBar.ascendingButton);
			break;
		case DESCENDING:
			blockBar.setHighlightedItem(blockBar.descendingButton);
			break;
		case BEND_LEFT:
			blockBar.setHighlightedItem(blockBar.leftButton);
			break;
		case BEND_RIGHT:
			blockBar.setHighlightedItem(blockBar.rightButton);
			break;
		case ENTER:
		case LEAVE:
		default:
			break;
		}
	}
}
