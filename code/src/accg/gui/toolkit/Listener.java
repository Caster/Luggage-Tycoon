package accg.gui.toolkit;

/**
 * A listener is an object that is notified of events.
 */
public interface Listener {
	
	/**
	 * Called whenever there is an event.
	 * @param event The event that occurred.
	 */
	public void event(Event event);
}
