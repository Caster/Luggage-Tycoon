package accg.gui.toolkit.enums;

import accg.gui.toolkit.containers.MenuStack;

/**
 * Possible alignments for menu bars inside the {@link MenuStack}.
 * 
 * Picking an alignment will move the menu bar to the according position
 * on the screen.
 */
public enum Alignment {
	/** Left or top. */
	BEGIN("Alignment.begin"),
	/** Center. */
	CENTER("Alignment.center"),
	/** Right or bottom. */
	END("Alignment.end");
	
	private Alignment(String name) {
		this.name = name;
	}
	
	private String name;
	
	/**
	 * Returns a human-readable description of the alignment type.
	 * 
	 * @return A human-readable, short description.
	 */
	public String getName() {
		return name;
	}
}
