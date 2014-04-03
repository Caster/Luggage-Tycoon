package accg.gui.toolkit.components;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.newdawn.slick.Color;

import accg.gui.toolkit.Component;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.event.*;

/**
 * A component in which the user can select one of several (textual)
 * elements.
 */
public class List extends Component {
	
	/**
	 * The elements shown in this list.
	 */
	private ArrayList<String> elements;
	
	/**
	 * The index of the selected element.
	 */
	private int selectedIndex = 0;
	
	/**
	 * The element that is the topmost visible one in the list.
	 */
	private int firstVisibleIndex = 0;
	
	/**
	 * The approximate amount of characters that this List should be
	 * able to contain.
	 */
	private int characters;
	
	/**
	 * The approximate amount of elements that this List should be
	 * able to contain.
	 */
	private int lines;

	/**
	 * Distance between the border of the text field and the contents.
	 */
	public static final int PADDING = 4;
	
	/**
	 * Width of the border.
	 */
	public static final int BORDER_WIDTH = 2;
	
	/**
	 * Creates a new label without any text.
	 * 
	 * @param characters The approximate amount of characters that this
	 * List should be able to contain. This is not at all an absolute
	 * limit; it is only used for determining the size of the list.
	 * @param lines The approximate amount of elements that this
	 * List should be able to contain. This is not at all an absolute
	 * limit; it is only used for determining the size of the list.
	 */
	public List(int characters, int lines) {
		this.characters = characters;
		this.lines = lines;
		
		this.elements = new ArrayList<>();
		
		addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent || e instanceof MouseDragEvent) {
					updateSelection((MouseEvent) e);
					requestFocus();
				}
				if (e instanceof MouseScrollEvent) {
					if (((MouseScrollEvent) e).getdWheel() > 0) {
						firstVisibleIndex -= 3;
					} else {
						firstVisibleIndex += 3;
					}
					clampVisibleIndex();
				}
			}
		});
	}

	/**
	 * Updates the selection, based on the given event.
	 * @param e The mouse event that caused the update.
	 */
	protected void updateSelection(MouseEvent e) {
		
		// don't react to clicks on the border
		if (e.getX() < BORDER_WIDTH || e.getX() > getWidth() + BORDER_WIDTH ||
				e.getY() < BORDER_WIDTH || e.getY() > getHeight() + BORDER_WIDTH) {
			return;
		}
		
		int yInList = e.getY() - PADDING;
		selectedIndex = yInList / getFont().getLineHeight();
		selectedIndex += firstVisibleIndex;
		
		if (selectedIndex >= elements.size()) {
			selectedIndex = elements.size() - 1;
		}
	}

	@Override
	public int getPreferredWidth() {
		// note: instead of this, we could also use the width of the widest
		// element; however, this could make the list very large with large
		// elements in it
		return characters * getFont().getWidth("n") + 2 * PADDING;
	}

	@Override
	public int getPreferredHeight() {
		return lines * getFont().getLineHeight() + 2 * PADDING;
	}

	@Override
	public void draw() {
		
		// border
		glColor4f(0, 0, 0, 1);
		glBegin(GL_QUADS);
		{
			glVertex2d(0, outline.getHeight());
			glVertex2d(outline.getWidth(), outline.getHeight());
			glVertex2d(outline.getWidth(), 0);
			glVertex2d(0, 0);
		}
		glEnd();
		
		// background
		glColor4f(1, 1, 1, 1);
		glBegin(GL_QUADS);
		{
			glVertex2d(2, outline.getHeight() - 2);
			glVertex2d(outline.getWidth() - 2, outline.getHeight() - 2);
			glVertex2d(outline.getWidth() - 2, 2);
			glVertex2d(2, 2);
		}
		glEnd();
		
		// background of selected item
		if (isElementVisible(selectedIndex)) {
			
			int selectedY = PADDING + (selectedIndex - firstVisibleIndex) * getFont().getLineHeight();
			
			glColor4f(0.5f, 0.5f, 0.5f, 1);
			glBegin(GL_QUADS);
			{
				glVertex2d(2, selectedY + getFont().getLineHeight());
				glVertex2d(outline.getWidth() - 2, selectedY + getFont().getLineHeight());
				glVertex2d(outline.getWidth() - 2, selectedY);
				glVertex2d(2, selectedY);
			}
			glEnd();
		}
		
		// draw the actual elements
		glEnable(GL_TEXTURE_2D);
		for (int i = firstVisibleIndex;
				isElementVisible(i) && i < elements.size();
				i++) {
			getFont().drawString(PADDING,
					PADDING + (i - firstVisibleIndex) * getFont().getLineHeight(),
					elements.get(i), Color.black);
		}
		glDisable(GL_TEXTURE_2D);
	}

	@Override
	public String getComponentName() {
		return "List";
	}

	/**
	 * Adds an element to this list.
	 * @param element The element to add.
	 */
	public void addElement(String element) {
		elements.add(element);
	}
	
	/**
	 * Adds all elements in the given array to this list.
	 * @param newElements The elements to add.
	 */
	public void addElements(String[] newElements) {
		this.elements.addAll(Arrays.asList(newElements));
	}
	
	/**
	 * Returns the number of elements currently in this list.
	 * @return The number of elements currently in this list.
	 */
	public int getElementCount() {
		return elements.size();
	}
	
	/**
	 * Returns the value of the selected element.
	 * @return The value of the selected element.
	 */
	public String getSelectedElement() {
		return elements.get(selectedIndex);
	}
	
	/**
	 * Returns the index of the selected element.
	 * @return The index of the selected element.
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}
	
	/**
	 * Remove the currently selected element from the list and select the
	 * element before that one.
	 */
	public void removeSelectedElement() {
		if (selectedIndex >= 0 && selectedIndex < elements.size()) {
			elements.remove(selectedIndex);
			selectedIndex--;
		}
		if (selectedIndex < 0) {
			selectedIndex = 0;
		}
	}
	
	/**
	 * Returns whether the element on the given index is visible.
	 * 
	 * @param index The index of the element to check.
	 * @return <code>true</code> if the element is visible; <code>false</code>
	 * otherwise.
	 */
	protected boolean isElementVisible(int index) {
		return index >= firstVisibleIndex && index < firstVisibleIndex + lines;
	}
	
	/**
	 * Clamps the first visible index within its bounds.
	 */
	protected void clampVisibleIndex() {
		
		// first check if there is empty space on the bottom
		if (firstVisibleIndex > elements.size() - lines) {
			firstVisibleIndex = elements.size() - lines;
		}
		
		// now remove empty space on the top
		// (this may introduce empty space on the bottom again; this
		// is of course unavoidable)
		if (firstVisibleIndex < 0) {
			firstVisibleIndex = 0;
		}
	}
}
