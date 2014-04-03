package accg.gui;

import accg.State;
import accg.gui.toolkit.components.Label;
import accg.i18n.Messages;

/**
 * A UsedBlocksLabel is a {@link Label} that can indicate how many blocks have
 * been placed by the user and what is the limit on how many blocks can be
 * placed. This information is read from the {@link State} automatically and is
 * updated with a simple {@link #updateInfo()} call.
 */
public class UsedBlocksLabel extends Label {

	/**
	 * State of the program.
	 */
	protected State state;
	
	/**
	 * Construct a new {@link Label} that will indicate how many blocks have
	 * been placed by the user and what is the limit, if there is one.
	 * @param state State of the program, used to read information from.
	 */
	public UsedBlocksLabel(State state) {
		super("");
		
		this.state = state;
		updateInfo();
	}

	/**
	 * Update information that is shown in this label.
	 */
	public void updateInfo() {
		if (this.state.world != null) {
			if (this.state.world.getBlockLimit() >= 0) {
				setText(String.format(Messages.get("UsedBlocksLabel.blocksPlaced"), //$NON-NLS-1$
						this.state.world.getBlockCount(),
						this.state.world.getBlockLimit()));
			} else {
				setText(String.format(Messages.get("UsedBlocksLabel.blocksPlacedInf"), //$NON-NLS-1$
						this.state.world.getBlockCount()));
			}
		}
	}
}
