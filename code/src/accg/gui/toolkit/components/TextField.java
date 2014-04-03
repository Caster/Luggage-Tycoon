package accg.gui.toolkit.components;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import accg.gui.toolkit.Component;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.event.*;

/**
 * A component in which the user can type text.
 */
public class TextField extends Component {
	
	/**
	 * Amount of frames that the cursor visible/invisible loop takes.
	 * Half of this period, the cursor will be visible and the other half
	 * it will be invisible.
	 */
	public static final int CURSOR_PERIOD = 80;
	
	/**
	 * The text shown in this text field.
	 */
	private String text = "";
	
	/**
	 * The location of the cursor. This value is always between 0 and
	 * <code>text.length()</code>.
	 */
	private int cursorLocation = 0;
	
	/**
	 * Counter that determines whether the cursor should be shown or not.
	 * If this counter is smaller than CURSOR_PERIOD / 2, it will be shown.
	 * If it is larger than CURSOR_PERIOD, it will wrap-around to 0 again.
	 */
	private int cursorShowCounter = 0;
	
	/**
	 * The approximate amount of characters that this TextField should be
	 * able to contain.
	 */
	private int characters;

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
	 * TextField should be able to contain. This is not at all an absolute
	 * limit; it is only used for determining the size of the text field.
	 */
	public TextField(int characters) {
		this.characters = characters;
		
		addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent || e instanceof MouseDragEvent) {
					updateCursor((MouseEvent) e);
					requestFocus();
				}
				
				if (e instanceof KeyEvent) {
					
					cursorShowCounter = 0;
					
					// is this a readable character?
					if (((KeyEvent) e).getCharacter() >= ' ' && ((KeyEvent) e).getCharacter() != 127) {
						addCharacterAtCursor(((KeyEvent) e).getCharacter());
					}
					
					// perhaps backspace?
					if (((KeyEvent) e).getKeyCode() == Keyboard.KEY_BACK) {
						performBackspace();
					}
					
					// perhaps delete?
					if (((KeyEvent) e).getKeyCode() == Keyboard.KEY_DELETE) {
						performDelete();
					}
					
					// perhaps left-arrow?
					if (((KeyEvent) e).getKeyCode() == Keyboard.KEY_LEFT) {
						cursorLocation--;
						if (cursorLocation < 0) {
							cursorLocation = 0;
						}
					}
					
					// perhaps right-arrow?
					if (((KeyEvent) e).getKeyCode() == Keyboard.KEY_RIGHT) {
						cursorLocation++;
						if (cursorLocation > text.length()) {
							cursorLocation = text.length();
						}
					}
					
					// perhaps home?
					if (((KeyEvent) e).getKeyCode() == Keyboard.KEY_HOME) {
						cursorLocation = 0;
					}
					
					// perhaps end?
					if (((KeyEvent) e).getKeyCode() == Keyboard.KEY_END) {
						cursorLocation = text.length();
					}
				}
			}
		});
	}

	/**
	 * Updates the position of the cursor, based on the given event.
	 * @param e The mouse event that caused the update.
	 */
	protected void updateCursor(MouseEvent e) {
		
		// don't react to clicks on the border
		if (e.getX() < BORDER_WIDTH || e.getX() > getWidth() + BORDER_WIDTH ||
				e.getY() < BORDER_WIDTH || e.getY() > getHeight() + BORDER_WIDTH) {
			return;
		}
		
		int xInText = e.getX() - PADDING;
		
		// do a binary search to find where this position is located in the text
		int lower = 0;
		int upper = text.length();
		
		while (upper - lower > 1) {
			int middle = (lower + upper) / 2;
			int cursorX = getFont().getWidth(text.substring(0, middle));
			
			if (cursorX < xInText) {
				lower = middle;
			} else {
				upper = middle;
			}
		}
		
		// is upper or lower closer to the actual x-coordinate?
		int xLower = getFont().getWidth(text.substring(0, lower));
		int xUpper = getFont().getWidth(text.substring(0, upper));
		
		if (xInText - xLower < xUpper - xInText) {
			cursorLocation = lower;
		} else {
			cursorLocation = upper;
		}
		
		// show the cursor immediately
		cursorShowCounter = 0;
	}
	
	/**
	 * Inserts the given character in the text, at the position of the
	 * cursor. Furthermore, moves the cursor behind this character.
	 * 
	 * @param key The key to add.
	 */
	protected void addCharacterAtCursor(char key) {
		text = text.substring(0, cursorLocation) + key + text.substring(cursorLocation);
		cursorLocation++;
	}
	
	protected void performBackspace() {
		
		if (cursorLocation == 0) {
			return;
		}
		
		text = text.substring(0, cursorLocation - 1) + text.substring(cursorLocation);
		cursorLocation--;
	}
	protected void performDelete() {
		
		if (cursorLocation == text.length()) {
			return;
		}
		
		text = text.substring(0, cursorLocation) + text.substring(cursorLocation + 1);
	}

	@Override
	public int getPreferredWidth() {
		return characters * getFont().getWidth("n") + 2 * PADDING;
	}

	@Override
	public int getPreferredHeight() {
		return getFont().getLineHeight() + 2 * PADDING;
	}

	@Override
	public void draw() {
		
		cursorShowCounter++;
		if (cursorShowCounter > CURSOR_PERIOD) {
			cursorShowCounter -= CURSOR_PERIOD;
		}
		
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
		
		// draw the actual text
		glEnable(GL_TEXTURE_2D);
		getFont().drawString(PADDING, PADDING, text, Color.black);
		glDisable(GL_TEXTURE_2D);
		
		// draw the cursor
		if (hasFocus() && cursorShowCounter % CURSOR_PERIOD < CURSOR_PERIOD / 2) {
			int cursorX = PADDING + getFont().getWidth(text.substring(0, cursorLocation));
			glColor4f(0, 0, 0, 1);
			glBegin(GL_QUADS);
			{
				glVertex2d(cursorX, outline.getHeight() - PADDING);
				glVertex2d(cursorX + 2, outline.getHeight() - PADDING);
				glVertex2d(cursorX + 2, PADDING);
				glVertex2d(cursorX, PADDING);
			}
			glEnd();
		}
	}

	@Override
	public String getComponentName() {
		return "TextField";
	}
	
	/**
	 * Changes the text in this text field.
	 * @param text The new text.
	 */
	public void setText(String text) {
		this.text = text;
		
		// move the cursor location to the end of the text
		cursorLocation = text.length();
	}
	
	/**
	 * Returns the text shown in this text field.
	 * @return The text of this label.
	 */
	public String getText() {
		return text;
	}
}
