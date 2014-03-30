package accg.gui;

import accg.State;
import accg.gui.toolkit.containers.StatusBar;

/**
 * This class manages the status bar in the GUI.
 */
public class MainStatusBar extends StatusBar {

	/**
	 * Label indicating how many blocks the user has placed.
	 */
	protected UsedBlocksLabel usedBlocksLabel;
	
	/**
	 * Creates a new MainStatusBar.
	 * @param state The state of the program.
	 */
	public MainStatusBar(State state) {
		usedBlocksLabel = new UsedBlocksLabel(state);
		add(usedBlocksLabel);
	}
	
	/**
	 * Update information of components shown in the status bar.
	 */
	public void updateInfo() {
		usedBlocksLabel.updateInfo();
	}
}
