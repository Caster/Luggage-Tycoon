package accg.objects;

import java.util.ArrayList;
import java.util.Iterator;

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
	 * @param object The object to add.
	 */
	public void addObject(E object) {
		if (object == null) {
			throw new RuntimeException("Drawn objects cannot be null");
		}
		objects.add(object);
	}
	
	@Override
	public void draw(State s) {
		for (E object : objects) {
			object.draw(s);
		}
	}

	@Override
	public Iterator<E> iterator() {
		return objects.iterator();
	}
}
