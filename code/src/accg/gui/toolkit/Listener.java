package accg.gui.toolkit;

/**
 * A listener is an object that is notified of events.
 */
public interface Listener {
	
	/**
	 * Called whenever there is an event.
	 * 
	 * @param event The event that occurred.
	 * @return Whether the event has been consumed.
	 */
	public boolean event(Event event);
}
