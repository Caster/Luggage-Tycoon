package accg.gui.toolkit.components;

import org.newdawn.slick.opengl.Texture;

import accg.gui.toolkit.Event;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.enums.ButtonType;
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
	 * @param messageKey1 Key of the text to display in this button in the
	 *            ResourceBundle from which text may be loaded using the
	 *            Messages class.
	 * @param defaultText1 Text that is displayed next to the icon in case
	 *            loading a text through the given key fails.
	 * @param icon1 Icon that is displayed.
	 * @param messageKey2 Key of the text to display in this button in the
	 *              ResourceBundle from which text may be loaded using the
	 *              Messages class. This is for the alternate state.
	 * @param defaultText2 Text that is displayed next to the icon in case
	 *              loading a text through the given key fails. This is for
	 *              the alternate state.
	 *              In case this is {@code null}, {@code text1} is used.
	 * @param icon2 Icon that is displayed in alternate state.
	 *              In case this is {@code null}, {@code icon1} is used.
	 */
	public ToggleMenuBarItem(String messageKey1, String defaultText1,
			Texture icon1, String messageKey2, String defaultText2, Texture icon2) {
		this(messageKey1, defaultText1, icon1, messageKey2, defaultText2, icon2,
				ButtonType.NORMAL);
	}
	
	/**
	 * Construct a new {@link ToggleMenuBarItem} with given texts and icons.
	 * The texts and icons should not be {@code null}.
	 * 
	 * @param messageKey1 Key of the text to display in this button in the
	 *            ResourceBundle from which text may be loaded using the
	 *            Messages class.
	 * @param defaultText1 Text that is displayed next to the icon in case
	 *            loading a text through the given key fails.
	 * @param icon1 Icon that is displayed.
	 * @param messageKey2 Key of the text to display in this button in the
	 *              ResourceBundle from which text may be loaded using the
	 *              Messages class. This is for the alternate state.
	 * @param defaultText2 Text that is displayed next to the icon in case
	 *              loading a text through the given key fails. This is for
	 *              the alternate state.
	 *              In case this is {@code null}, {@code text1} is used.
	 * @param icon2 Icon that is displayed in alternate state.
	 *              In case this is {@code null}, {@code icon1} is used.
	 * @param type {@link ButtonType} of the menu item.
	 */
	public ToggleMenuBarItem(String messageKey1, String defaultText1,
			Texture icon1, String messageKey2, String defaultText2,
			Texture icon2, ButtonType type) {
		super(messageKey1, defaultText1, icon1, type);
		this.text2 = (text2 == null ? text : text2);
		this.messageKey2 = messageKey2;
		this.defaultText2 = defaultText2;
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
		
		tmpText = this.messageKey;
		this.messageKey = this.messageKey2;
		this.messageKey2 = tmpText;
		
		tmpText = this.defaultText;
		this.defaultText = this.defaultText2;
		this.defaultText2 = tmpText;
	}
	
	/** Describing text of this menu item. */
	protected String text2;
	/** Alternate default text. */
	protected String defaultText2;
	/** Alternate message key. */
	protected String messageKey2;
	/** Icon of this menu item. */
	protected Texture icon2;
}
