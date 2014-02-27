package accg.gui;

import org.newdawn.slick.Font;
import org.newdawn.slick.opengl.Texture;

import accg.gui.MenuBarItem.Type;

/**
 * A ToggleMenuBarItem is a {@link MenuBarItem} that toggles between two states
 * every time the user clicks the item. It has two possibly different icons and
 * texts that will toggle after every click to indicate the state.
 */
public class ToggleMenuBarItem extends MenuBarItem {

	/**
	 * Construct a new {@link ToggleMenuBarItem} with given texts and icons.
	 * The texts and icons should not be {@code null}.
	 * 
	 * @param text1 Text that is displayed next to the icon.
	 * @param icon1 Icon that is displayed.
	 * @param text2 Text that is displayed in alternate state.
	 *              In case this is {@code null}, {@code text1} is used.
	 * @param icon2 Icon that is displayed in alternate state.
	 *              In case this is {@code null}, {@code icon1} is used.
	 */
	public ToggleMenuBarItem(String text1, Texture icon1, String text2,
			Texture icon2) {
		this(text1, icon1, text2, icon2, Type.NORMAL);
	}
	
	/**
	 * Construct a new {@link ToggleMenuBarItem} with given texts and icons.
	 * The texts and icons should not be {@code null}.
	 * 
	 * @param text1 Text that is displayed next to the icon.
	 * @param icon1 Icon that is displayed.
	 * @param text2 Text that is displayed in alternate state.
	 *              In case this is {@code null}, {@code text1} is used.
	 * @param icon2 Icon that is displayed in alternate state.
	 *              In case this is {@code null}, {@code icon1} is used.
	 * @param type {@link Type} of the menu item.
	 */
	public ToggleMenuBarItem(String text1, Texture icon1, String text2,
			Texture icon2, Type type) {
		super(text1, icon1, type);
		this.text2 = (text2 == null ? text1 : text2);
		this.icon2 = (icon2 == null ? icon1 : icon2);
	}
	
	@Override
	public int getPreferredWidth(Font renderFont) {
		return Math.max(renderFont.getWidth(text),
				renderFont.getWidth(text2)) + 2 * PADDING;
	}
	
	@Override
	public void onClick(int x, int y) {
		super.onClick(x, y);
		
		// swap texts
		String tmpText = this.text;
		this.text = this.text2;
		this.text2 = tmpText;
		
		// swap icons
		Texture tmpIcon = this.icon;
		this.icon = this.icon2;
		this.icon2 = tmpIcon;
		
		// notify parent of (possible) resizing
		this.parent.onChildResize();
	}
	
	@Override
	public void onDrag(int x, int y) { /* ignored */ }

	@Override
	public void onScroll(int dWheel) { /* ignored */ }

	/** Describing text of this menu item. */
	protected String text2;
	/** Icon of this menu item. */
	protected Texture icon2;
}
