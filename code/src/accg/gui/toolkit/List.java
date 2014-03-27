package accg.gui.toolkit;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import accg.gui.toolkit.event.MouseClickEvent;
import accg.gui.toolkit.event.MouseDragEvent;
import accg.gui.toolkit.event.MouseEvent;

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
		glColor4f(0.5f, 0.5f, 0.5f, 1);
		glBegin(GL_QUADS);
		{
			glVertex2d(2, PADDING + (selectedIndex + 1) * getFont().getLineHeight());
			glVertex2d(outline.getWidth() - 2, PADDING + (selectedIndex + 1) * getFont().getLineHeight());
			glVertex2d(outline.getWidth() - 2, PADDING + selectedIndex * getFont().getLineHeight());
			glVertex2d(2, PADDING + selectedIndex * getFont().getLineHeight());
		}
		glEnd();
		
		// draw the actual elements
		glEnable(GL_TEXTURE_2D);
		for (int i = 0; i < elements.size(); i++) {
			getFont().drawString(PADDING, PADDING + i * getFont().getLineHeight(),
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
}
