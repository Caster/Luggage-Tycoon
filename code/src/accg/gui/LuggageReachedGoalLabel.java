package accg.gui;

import accg.State;
import accg.gui.toolkit.components.Label;
import accg.i18n.Messages;
import accg.objects.Block;
import accg.objects.blocks.EnterBlock;
import accg.objects.blocks.LeaveBlock;

/**
 * A LuggageReachedGoalLabel is a {@link Label} that can indicate how many
 * pieces of luggage have been "released" from the EnterBlocks in the scene and
 * how many reached their goal. This information is read from the {@link State}
 * automatically and is updated with a simple {@link #updateInfo()} call.
 */
public class LuggageReachedGoalLabel extends Label {

	/**
	 * State of the program.
	 */
	protected State state;
	
	/**
	 * Construct a new {@link Label} that will indicate how many blocks have
	 * been placed by the user and what is the limit, if there is one.
	 * @param state State of the program, used to read information from.
	 */
	public LuggageReachedGoalLabel(State state) {
		super("");
		
		this.state = state;
		updateInfo();
	}

	/**
	 * Update information that is shown in this label.
	 */
	public void updateInfo() {
		if (this.state.world != null) {
			int lugCount = 0;
			int lugGenCount = 0;
			int lugArrivedCount = 0;
			for (Block b : this.state.world.bc) {
				if (b instanceof EnterBlock) {
					EnterBlock eb = (EnterBlock) b;
					if (lugCount >= 0 && eb.getLuggageNum() >= 0) {
						lugCount += eb.getLuggageNum();
					} else {
						lugCount = -1;
					}
					// the generated count goes one beyond the limit, so we need to correct that
					// but only if there is a limit of course, otherwise we would always get -1
					if (eb.getLuggageNum() >= 0) {
						lugGenCount += Math.min(eb.getGeneratedLuggageNum(), eb.getLuggageNum());
					} else {
						lugGenCount += eb.getGeneratedLuggageNum();
					}
				}
				
				if (b instanceof LeaveBlock) {
					lugArrivedCount += ((LeaveBlock) b).getArrivedLuggageCount();
				}
			}
			
			setText(String.format(Messages.get("LuggageReachedGoalLabel."
					+ "information"), lugGenCount, lugCount, lugArrivedCount));
		}
	}
}
