package accg.gui;

import accg.State;
import accg.gui.toolkit.components.Label;
import accg.i18n.Messages;

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
			setText(String.format(Messages.get("UsedBlocksLabel.blocksPlaced"), //$NON-NLS-1$
					this.state.world.getBlockCount(),
					(this.state.world.getBlockLimit() < 0 ? "∞" :
						String.valueOf(this.state.world.getBlockLimit()))));
		}
	}
}
