package accg.gui;

import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.containers.StatusBar;

/**
 * This class manages the status bar in the GUI.
 */
public class MainStatusBar extends StatusBar {

	/**
	 * Label indicating how many pieces of luggages reached their goal.
	 */
	protected LuggageReachedGoalLabel luggageGoalLabel;
	/**
	 * Label indicating how many blocks the user has placed.
	 */
	protected UsedBlocksLabel usedBlocksLabel;
	
	/**
	 * Creates a new MainStatusBar.
	 * @param state The state of the program.
	 */
	public MainStatusBar(State state) {
		luggageGoalLabel = new LuggageReachedGoalLabel(state);
		add(luggageGoalLabel);
		
		usedBlocksLabel = new UsedBlocksLabel(state);
		add(usedBlocksLabel);
		
		updateMode(null);
	}
	
	/**
	 * Update information of components shown in the status bar.
	 */
	public void updateInfo() {
		if (luggageGoalLabel.isVisible()) {
			luggageGoalLabel.updateInfo();
		}
		
		if (usedBlocksLabel.isVisible()) {
			usedBlocksLabel.updateInfo();
		}
	}
	
	/**
	 * Update what is shown in the status bar, depending on the program mode.
	 * 
	 * @param mode The new program mode of the program.
	 */
	public void updateMode(ProgramMode mode) {
		if (mode == ProgramMode.BUILDING_MODE) {
			luggageGoalLabel.setVisible(false);
			usedBlocksLabel.setVisible(true);
		} else if (mode == ProgramMode.SIMULATION_MODE) {
			luggageGoalLabel.setVisible(true);
			usedBlocksLabel.setVisible(false);
		} else {
			luggageGoalLabel.setVisible(false);
			usedBlocksLabel.setVisible(false);
		}
	}
}
