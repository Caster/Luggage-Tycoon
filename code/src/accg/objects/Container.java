package accg.objects;

import java.util.ArrayList;

import accg.State;

/**
 * A {@link DrawableObject} that just contains several objects
 * that are all drawn with the same transform.
 */
public abstract class Container extends DrawableObject {
	
	private ArrayList<DrawableObject> objects;
	
	public Container() {
		objects = new ArrayList<>();
	}
	
	public void addObject(DrawableObject object) {
		if (object == null) {
			throw new RuntimeException("Drawn objects cannot be null");
		}
		objects.add(object);
	}
	
	@Override
	public void draw(State s) {
		for (DrawableObject object : objects) {
			object.draw(s);
		}
	}
}
