package accg.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.vecmath.Vector3f;

import accg.State;

/**
 * A {@link DrawableObject} that just contains several objects
 * that are all drawn with the same transform.
 * 
 * @param <E> The type of objects to draw.
 */
public class Container<E extends DrawableObject> extends DrawableObject implements Iterable<E> {
	
	/**
	 * The objects to draw.
	 */
	private ArrayList<E> objects;
	
	/**
	 * Creates a new, empty container.
	 */
	public Container() {
		objects = new ArrayList<>();
	}
	
	/**
	 * Adds a new object to this container.
	 * 
	 * @param object The object to add.
	 * @throws NullPointerException If <code>object == null</code>.
	 */
	public void addObject(E object) {
		if (object == null) {
			throw new NullPointerException("Drawn objects cannot be null");
		}
		objects.add(object);
	}
	
	/**
	 * Removes all objects from this container.
	 */
	public void clear() {
		for (DrawableObject object : objects) {
			object.onDestroy();
		}
		objects.clear();
	}
	
	/**
	 * Removes an object from this container.
	 * 
	 * @param object The object to remove.
	 * @throws IllegalStateException If the container does not contain
	 * <code>object</code>.
	 */
	public void remove(E object) {
		if (!objects.remove(object)) {
			throw new IllegalStateException("This container does not contain the element to remove");
		}
	}
	
	@Override
	public void draw(State s) {
		for (E object : objects) {
			object.draw(s);
		}
	}

	@Override
	public void setPosition(Vector3f position) { /* ignored */ }
	
	@Override
	public Iterator<E> iterator() {
		return objects.iterator();
	}
}
