package accg.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class manages string lookups in the messages file for the correct
 * language.
 */
public class Messages {
	
	private static final String BUNDLE_NAME = "accg.i18n.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private Messages() {
		// cannot instantiate this class
	}
	
	/**
	 * Retrieves the string with the given key.
	 * 
	 * @param key The key to look up the string for.
	 * @return The string, if found, or a string of the form <code>!key!</code> if
	 * there is no string in the messages file for this key.
	 */
	public static String get(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
