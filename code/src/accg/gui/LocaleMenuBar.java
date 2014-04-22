package accg.gui;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import org.newdawn.slick.opengl.Texture;

import accg.State;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.components.Button;
import accg.gui.toolkit.containers.MenuBar;
import accg.gui.toolkit.enums.ButtonType;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.i18n.Messages;

/**
 * The LocaleMenuBar presents the user with locales to choose from, to change the program language.
 */
public class LocaleMenuBar extends MenuBar {

	/**
	 * Supported locales. This list should be updated if a new language file is added.
	 */
	public static final Locale[] SUPPORTED_LOCALES = new Locale[] {
		new Locale("nl", "NL"),
		Locale.UK
	};
	
	/**
	 * Construct a new LocaleMenuBar.
	 * 
	 * @param s State of program, used to look up icons.
	 */
	public LocaleMenuBar(State s) {
		Collections.sort(Arrays.asList(SUPPORTED_LOCALES), new Comparator<Locale>() {

			@Override
			public int compare(Locale o1, Locale o2) {
				return o1.getDisplayLanguage(o1).compareTo(o2.getDisplayLanguage(o2));
			}
		});
		
		for (Locale l : SUPPORTED_LOCALES) {
			add(generateLocaleItem(l, s));
		}
	}
	
	/**
	 * Generate a menu item to select the given locale.
	 * 
	 * @param l Locale to select.
	 * @param s State of program, used to look up icons.
	 * @return A button to be used as menu item.
	 */
	private Button generateLocaleItem(final Locale l, final State s) {
		// create the item
		Button mbi = new Button("Locale.locale" + l.getLanguage(), l.getDisplayLanguage(l),
				getLocaleIcon(l, s), ButtonType.CHECKABLE_UNIQUE);
		mbi.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					Locale.setDefault(l);
					Messages.setLocale(l);
					s.gui.handleLocaleChanged();
				}
			}
		});
		
		// check the item if needed
		if (Locale.getDefault().equals(l)) {
			mbi.setChecked(true);
		}
		// return the result
		return mbi;
	}
	
	/**
	 * Return the icon that belongs to the given locale.
	 * 
	 * @param l Locale for icon to retrieve.
	 * @param s State, used to look up icons in.
	 * @return An icon, or {@code null} if no icon could be found.
	 */
	private Texture getLocaleIcon(Locale l, State s) {		
		switch (l.getCountry()) {
		case "" :
		case "GB" :
		case "US" :
			return s.textures.iconFlagUK;
		case "NL" :
			return s.textures.iconFlagNL;
		default :
			System.err.println("Could not find an icon for country \"" +
					l.getCountry() + "\".");
			return null;
		}
	}
}
