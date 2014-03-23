package accg.gui;

import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.Button;
import accg.gui.toolkit.MenuStack;

/**
 * This class manages the menu stack in the GUI.
 */
public class MainStack extends MenuStack {
	
	public static final String SETTINGS_MENU = "settings";
	public static final String POSITION_MENU = "position";
	public static final String ALIGNMENT_MENU = "alignment";
	public static final String PRESENTATION_MENU = "presentation";
	
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
		
		addToCollection(ProgramMode.NORMAL_MODE, new NormalModeMenuBar(this, state));
		addToCollection(ProgramMode.BUILDING_MODE, new BuildingModeMenuBar(this, state));
		addToCollection(ProgramMode.SIMULATION_MODE, new SimulationModeMenuBar(this, state));
		addToCollection(SETTINGS_MENU, new SettingsMenuBar(this, state));
		addToCollection(POSITION_MENU, new PositionMenuBar(this, state));
		addToCollection(ALIGNMENT_MENU, new AlignmentMenuBar(this, state));
		addToCollection(PRESENTATION_MENU, new PresentationMenuBar(this, state));
		
		int alignmentId = state.prefs.getInt("menu.alignment", State.DEF_MENU_ALIGNMENT);
		setAlignment(MenuStack.Alignment.values()[alignmentId]);
		
		int positionId = state.prefs.getInt("menu.position", State.DEF_MENU_POSITION);
		setPosition(MenuStack.Position.values()[positionId]);
		
		int presentationId = state.prefs.getInt("menu.presentation", State.DEF_MENU_PRESENTATION);
		setPresentation(Button.Presentation.values()[presentationId]);
		
		updateItems();
	}
	
	/**
	 * Updates the menu stack to the current state.
	 */
	public void updateItems() {
		addMenuOnPosition(0, state.programMode);
	}
}
