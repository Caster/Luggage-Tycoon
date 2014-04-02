package accg.gui;

import accg.gui.toolkit.components.Button;
import accg.objects.Block;

/**
 * Subclass of {@link Button} that shows a {@link Block} instead of an icon.
 */
public class BlockButton extends Button {
	
	/**
	 * The block to use as an icon.
	 */
	private Block block;
	
	/**
	 * Creates a new {@link BlockButton} with the given text and block.
	 * @param text The text to show.
	 * @param block The block to use as an icon.
	 */
	public BlockButton(String text, Block block) {
		super(text, null);
		
		this.block = block;
	}
}
