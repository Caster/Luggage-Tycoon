package accg.objects;

/**
 * A DrawableObjectListener is a listener that can be attached to any
 * {@link DrawableObject} and will then be notified of events such as the object
 * being destroyed.
 */
public interface DrawableObjectListener {

	/**
	 * Called when (one of) the {@link DrawableObject}(s) to which this listener
	 * is attached is destroyed. 
	 */
	public void onDestroy();
	
}
