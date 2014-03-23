package accg.gui.toolkit;

import org.newdawn.slick.opengl.Texture;

import accg.gui.toolkit.event.MouseClickEvent;

/**
 * A ToggleMenuBarItem is a {@link Button} that toggles between two states
 * every time the user clicks the item. It has two possibly different icons and
 * texts that will toggle after every click to indicate the state.
 */
public class ToggleMenuBarItem extends Button {

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
	 * @param type {@link Button.Type} of the menu item.
	 */
	public ToggleMenuBarItem(String text1, Texture icon1, String text2,
			Texture icon2, Type type) {
		super(text1, icon1, type);
		this.text2 = (text2 == null ? text1 : text2);
		this.icon2 = (icon2 == null ? icon1 : icon2);
		
		// add a listener for calling onClick() when appropriate
		addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					onClick();
				}
			}
		});
	}
	
	@Override
	public String getComponentName() {
		return "ToggleMenuBarItem";
	}
	
	@Override
	public int getPreferredWidth() {
		
		// [ws] TODO the following comment was here:
		//
		// in case we have text next to the icon and position top or bottom,
		// it actually looks nicer to just return the preferred width with
		// the current text, I think [tca]
		//
		// I agree with that, but I don't see a neat way of doing that (i.e.,
		// finding the position) with the new GUI toolkit. We should think
		// about this :)
		
		int text1Width = super.getPreferredWidth();
		swapTexts();
		int text2Width = super.getPreferredWidth();
		swapTexts();
		
		return Math.max(text1Width, text2Width);
	}
	
	/**
	 * Handles clicks on this item by swapping the texts and icons.
	 */
	protected void onClick() {
		swapTexts();
		swapIcons();
	}

	/**
	 * Swap {@code this.icon} and {@code this.icon2}.
	 */
	protected void swapIcons() {
		Texture tmpIcon = this.icon;
		this.icon = this.icon2;
		this.icon2 = tmpIcon;
	}
	
	/**
	 * Swap {@code this.text} and {@code this.text2}.
	 */
	protected void swapTexts() {
		String tmpText = this.text;
		this.text = this.text2;
		this.text2 = tmpText;
	}
	
	/** Describing text of this menu item. */
	protected String text2;
	/** Icon of this menu item. */
	protected Texture icon2;
}
