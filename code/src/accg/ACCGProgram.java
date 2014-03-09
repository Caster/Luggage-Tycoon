package accg;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.awt.Font;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.prefs.Preferences;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.Point;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.ResourceLoader;

import accg.State.ProgramMode;
import accg.camera.Camera;
import accg.gui.*;
import accg.gui.MenuBar.Alignment;
import accg.gui.MenuBar.Position;
import accg.gui.MenuBar.Presentation;
import accg.gui.MenuBarItem.Type;
import accg.objects.Floor;
import accg.objects.World;
import accg.simulation.Simulation;

/**
 * The main class for the ACCG program.
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class ACCGProgram {
	
	private static final int DEF_MENU_ALIGNMENT = 1;
	private static final int DEF_MENU_POSITION = 0;
	private static final int DEF_MENU_PRESENTATION = 1;
	
	/** If the escape key has been pressed. */
	private boolean escPressed = false;
	
	/** Possible {@link DisplayMode} which the program can use. */
	private DisplayMode windowedMode, fullScreenMode;
	
	/** Camera that provides an easy-to-use API for changing viewpoint and such. */
	private Camera camera;
	
	/**
	 * Menu bars used in the program.
	 * 
	 *  0: main menu.
	 *  1: settings menu           (child of 0).
	 *  2: menu position menu      (child of 1).
	 *  3: menu alignment menu     (child of 1).
	 *  4: menu presentation menu  (child of 1).
	 */
	private MenuBar[] menuBars;
	
	/**
	 * Point where mouse was pressed. Is compared to point where mouse
	 * is released to see if it was a click or not.
	 */
	private Point clickedPoint;
	
	/**
	 * Preferences object. Used to store user preferences persistently.
	 */
	private Preferences prefs;
	
	public static void main(String[] args) {
		ACCGProgram p = new ACCGProgram();
		p.start();
	}
	
	public void start() {
		
		windowedMode = new DisplayMode(1024, 576);
		fullScreenMode = Display.getDesktopDisplayMode();
		
		try {
			Display.setDisplayMode(windowedMode);
			Display.setFullscreen(false);
			Display.setResizable(true);
			Display.setTitle("Luggage Tycoon");
			Display.setVSyncEnabled(true);
			Display.create(new PixelFormat().withSamples(8));
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		// save window size to be able to react to resize events
		int displayWidth = Display.getWidth();
		int displayHeight = Display.getHeight();
		
		// pre-initialize stuff
		State s = new State();
		initialiseFonts(s);
		
		// show a loading message, loading textures takes some time
		glClearColor(0.8f, 0.8f, 0.77f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		GUI.make2D();
		String loadingText = "Loading...";
		int loadingTextWidth = s.fontMenu.getWidth(loadingText);
		int loadingTextHeight = s.fontMenu.getHeight(loadingText);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		s.fontMenu.drawString((displayWidth - loadingTextWidth) / 2,
				(displayHeight - loadingTextHeight) / 2, loadingText, Color.black);
		glDisable(GL_BLEND);
		Display.update();
		
		// initialize stuff here
		s.simulation = new Simulation(s);
		s.world = new World(s);
		s.floor = new Floor(s);
		s.textures = new Textures();
		s.startTime = (float) Sys.getTime() / Sys.getTimerResolution();
		camera = new Camera(s);
		clickedPoint = null;
		
		// intialise GUI stuff
		menuBars = new MenuBar[5];
		menuBars[0] = new MenuBar(s, Position.values()[DEF_MENU_POSITION],
				Alignment.values()[DEF_MENU_ALIGNMENT],
				Presentation.values()[DEF_MENU_PRESENTATION]);
		menuBars[1] = new MenuBar(s, menuBars[0]);
		menuBars[2] = new MenuBar(s, menuBars[1]);
		menuBars[3] = new MenuBar(s, menuBars[1]);
		menuBars[4] = new MenuBar(s, menuBars[1]);
		loadPreferences(s);
		initialiseMenus(menuBars, s);
		
		// enable some GL stuff
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
		glEnable(GL_DEPTH_TEST);
		
		while (!Display.isCloseRequested() && !escPressed) {
			
			// handle events
			handleKeyEvents();
			handlePressedKeys();
			handleScrollEvents();
			handleMouseEvents();
			
			// handle resize events
			if (displayWidth != Display.getWidth() || displayHeight != Display.getHeight()) {
				displayWidth = Display.getWidth();
				displayHeight = Display.getHeight();
				menuBars[0].handleResizeEvent(displayWidth, displayHeight);
			}
			
			// update time
			s.prevTime = s.time;
			s.time = (float) ((double) Sys.getTime() / Sys.getTimerResolution() - s.startTime);
			
			// update simulation, if applicable
			if (s.programMode == ProgramMode.SIMULATION_MODE) {
				s.simulation.update(s);
				s.simulation.addObjects(s);
			}
			
			// start rendering stuff
			glClearColor(0.8f, 0.8f, 0.77f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glViewport(0, 0, Display.getWidth(), Display.getHeight());
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			gluPerspective(45, (float) Display.getWidth() / Display.getHeight(), 0.1f, 1000f);
			
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			camera.setLookAt();

			// draw the scene
			
			// step 1: draw floor
			s.drawingShadows = false;
			s.floor.draw(s);
			
			// step 2: draw shadows
			glDisable(GL_COLOR_MATERIAL);
			FloatBuffer shadowColor = BufferUtils.createFloatBuffer(4);
			shadowColor.put(new float[] {0, 0, 0, 1});
			shadowColor.flip();
			glMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, shadowColor);
			glPushMatrix();
			glScalef(1, 1, 0);
			s.world.draw(s);
			glPopMatrix();
			glEnable(GL_COLOR_MATERIAL);
			
			// step 3: draw floor
			s.drawingShadows = true;
			s.floor.draw(s);
			
			// step 4: draw objects
			s.world.draw(s);
			
			// draw the menu bars
			for (int i = 0; i < menuBars.length; i++) {
				menuBars[i].draw(s);
			}
			
			// check for errors
			Util.checkGLError();
			
			glFlush(); // this is needed, else some stuff is not drawn

			Display.update();
		}
		
		Display.destroy();
	}
	
	/**
	 * Handles pressed / released key events.
	 */
	public void handleKeyEvents() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				switch (Keyboard.getEventKey()) {
				case Keyboard.KEY_RETURN:
					// only respond to Alt+Enter, not just Enter
					if (Keyboard.getEventKey() == Keyboard.KEY_RETURN &&
							!(Keyboard.isKeyDown(Keyboard.KEY_LMENU) ||
							  Keyboard.isKeyDown(Keyboard.KEY_RMENU))) {
						return;
					}
					
					try {
						if (Display.isFullscreen()) {
							Display.setDisplayMode(windowedMode);
							Display.setFullscreen(false);
						} else {
							Display.setDisplayMode(fullScreenMode);
							Display.setFullscreen(true);
						}
					} catch (LWJGLException e) {
						e.printStackTrace();
					}
					break;
				case Keyboard.KEY_ESCAPE:
					escPressed = true;
					break;
				}
			}
		}
	}
	
	/**
	 * Handles keys that are pressed.
	 */
	public void handlePressedKeys() {
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)) {
				camera.turnRight();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)) {
				camera.turnLeft();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)) {
				camera.turnDown();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)) {
				camera.turnUp();
			}
		} else {
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)) {
				camera.moveLeft();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)) {
				camera.moveRight();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)) {
				camera.moveForward();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)) {
				camera.moveBackward();
			}
		}
	}
	
	/**
	 * Handles scrollwheel events from the mouse.
	 */
	public void handleScrollEvents() {
		int dWheel = Mouse.getDWheel();
		if (dWheel == 0) {
			return;
		}
		
		// first see if the menu wants to handle this
		boolean handledByMenu = false;
		for (int i = 0; i < menuBars.length; i++) {
			handledByMenu = (menuBars[i].handleMouseWheelEvent(
					Mouse.getX(), Mouse.getY(), dWheel) || handledByMenu);
		}
		
		// otherwise, let the camera handle it
		if (!handledByMenu) {
			if (dWheel < 0) {
				camera.moveUp();
			} else if (dWheel > 0) {
				camera.moveDown();
			}
		}
	}
	
	/**
	 * Handles mouse move events and such.
	 */
	public void handleMouseEvents() {
		// Variable handledButton[i] holds if an event for mouse button i
		// was handled or not. This is needed because LWJGL's API for the
		// mouse is slightly weird. Or I just don't get it.
		boolean[] handledButton = new boolean[] {false, false, false};
		boolean handledMouseMove = false;
		boolean handledMouseMoveByMenu = false;
		
		while (Mouse.next()) {
			// handle click
			int eventButton;
			if ((eventButton = Mouse.getEventButton()) != -1) {
				if (eventButton == 0) {
					if (Mouse.getEventButtonState()) {
						clickedPoint = new Point(Mouse.getX(), Mouse.getY());
					} else {
						if (clickedPoint != null) {
							if (Math.abs(clickedPoint.getX() -
										Mouse.getX()) < 3 &&
									Math.abs(clickedPoint.getY() -
											Mouse.getY()) < 3) {
								for (int i = 0; i < menuBars.length; i++) {
									menuBars[i].handleMouseClickEvent(
											Mouse.getX(), Mouse.getY());
								}
							}
							
							clickedPoint = null;
						}
					}
				}
			}
			
			if (!Mouse.getEventButtonState()) {
				int dx = Mouse.getEventDX();
				int dy = Mouse.getEventDY();
				
				// handle general mouse move
				if (!handledMouseMove) {
					for (int i = 0; i < menuBars.length; i++) {
						handledMouseMoveByMenu = (menuBars[i].handleMouseMoveEvent(
								Mouse.getX(), Mouse.getY()) || handledMouseMoveByMenu);
					}
					
					handledMouseMove = true;
				}
				
				// handle left mouse button: mouse button 0
				if (!handledButton[0] && !handledMouseMoveByMenu && Mouse.isButtonDown(0)) {
					camera.moveByMouse(dx, dy);
					
					handledButton[0] = true;
				}
				
				// handle middle mouse button: mouse button 2
				if (!handledButton[2] && !handledMouseMoveByMenu && Mouse.isButtonDown(2)) {					
					camera.turnByMouse(dx, dy);
					
					handledButton[2] = true;
				}
			}
		}
	}
	
	/**
	 * Load fonts and set them in the given state.
	 * 
	 * @param s State to set fonts in.
	 */
	private void initialiseFonts(State s) {
		try {
			InputStream russoOneFontStream =
					ResourceLoader.getResourceAsStream("res/fonts/RussoOne-Regular.ttf");
			Font russoOneAwt = Font.createFont(Font.TRUETYPE_FONT, russoOneFontStream);
			russoOneAwt = russoOneAwt.deriveFont(14f);
			s.fontMenu = new TrueTypeFont(russoOneAwt, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create some menu items and add those to the given menu bar.
	 * 
	 * @param menus Array of menus to initialise.
	 * @param s State to read icon textures from.
	 */
	private void initialiseMenus(MenuBar[] menus, final State s) {
		// main menu
		menus[0].addMenuBarItem(new ToggleMenuBarItem("Simulate",
				s.textures.iconStart, "Stop simulation", s.textures.iconStop) {
			@Override
			public void onClick(int x, int y) {
				super.onClick(x, y);
				
				if (s.programMode == ProgramMode.BUILDING_MODE) {
					s.simulation.skipToTime(s.time);
					s.programMode = ProgramMode.SIMULATION_MODE;
				} else {
					s.programMode = ProgramMode.BUILDING_MODE;
					s.simulation.clearObjects(s);
				}
			}
		});
		
		menus[0].addMenuBarItem(new MenuBarItem("Open", s.textures.iconOpen) {
			@Override
			public void onClick(int x, int y) {
				super.onClick(x, y);
				System.out.println("Open!");
			}
			
			@Override
			public void onDrag(int x, int y) { /* ignored */ }
			
			@Override
			public void onScroll(int dWheel) { /* ignored */ }
		});
		
		menus[0].addMenuBarItem(new MenuBarItem("Save", s.textures.iconSave) {
			@Override
			public void onClick(int x, int y) {
				super.onClick(x, y);
				System.out.println("Save!");
			}
			
			@Override
			public void onDrag(int x, int y) { /* ignored */ }
			
			@Override
			public void onScroll(int dWheel) { /* ignored */ }
		});
		
		menus[0].addMenuBarItem(new MenuBarItem("Settings", s.textures.iconConfigure) {
			@Override
			public void onClick(int x, int y) {
				super.onClick(x, y);
				menuBars[1].toggleVisible();
			}
			
			@Override
			public void onDrag(int x, int y) { /* ignored */ }
			
			@Override
			public void onScroll(int dWheel) { /* ignored */ }
		});
		
		menus[0].addMenuBarItem(new MenuBarItem("Exit", s.textures.iconExit) {
			@Override
			public void onClick(int x, int y) {
				super.onClick(x, y);
				escPressed = true;
			}
			
			@Override
			public void onDrag(int x, int y) { /* ignored */ }
			
			@Override
			public void onScroll(int dWheel) { /* ignored */ }
		});
		
		// settings menu
		menus[1].addMenuBarItem(new MenuBarItem("Menu position", s.textures.iconConfigure) {
			@Override
			public void onClick(int x, int y) {
				super.onClick(x, y);
				menuBars[2].toggleVisible();
			}
			
			@Override
			public void onDrag(int x, int y) { /* ignored */ }
			
			@Override
			public void onScroll(int dWheel) { /* ignored */ }
		});
		
		menus[1].addMenuBarItem(new MenuBarItem("Menu alignment", s.textures.iconConfigure) {
			@Override
			public void onClick(int x, int y) {
				super.onClick(x, y);
				menuBars[3].toggleVisible();
			}
			
			@Override
			public void onDrag(int x, int y) { /* ignored */ }
			
			@Override
			public void onScroll(int dWheel) { /* ignored */ }
		});
		
		menus[1].addMenuBarItem(new MenuBarItem("Menu presentation", s.textures.iconConfigure) {
			@Override
			public void onClick(int x, int y) {
				super.onClick(x, y);
				menuBars[4].toggleVisible();
			}
			
			@Override
			public void onDrag(int x, int y) { /* ignored */ }
			
			@Override
			public void onScroll(int dWheel) { /* ignored */ }
		});
		
		menus[1].addMenuBarItem(new SliderMenuBarItem("Mouse sensitivity",
				s.textures.iconMouse, 0.1f, 2.0f, s.mouseSensitivityFactor) {
			@Override
			public void onClick(int x, int y) {
				super.onClick(x, y);
				updateStateVal();
			}
			
			@Override
			public void onDrag(int x, int y) {
				super.onDrag(x, y);
				updateStateVal();
			}
			
			@Override
			public void onScroll(int dWheel) {
				super.onScroll(dWheel);
				updateStateVal();
			}
			
			private void updateStateVal() {
				s.mouseSensitivityFactor = this.val;
				prefs.putFloat("mouse.sensitivity", this.val);
			}
		});
		
		// menu position menu
		for (int i = 0; i < Position.values().length; i++) {
			menus[2].addMenuBarItem(generatePositionItem(i, s));
		}
		
		// menu alignment menu
		for (int i = 0; i < Alignment.values().length; i++) {
			menus[3].addMenuBarItem(generateAlignmentItem(i, s));
		}
		
		// menu presentation menu
		for (int i = 0; i < Presentation.values().length; i++) {
			menus[4].addMenuBarItem(generatePresentationItem(i, s));
		}
	}
	
	/**
	 * Generate a {@link MenuBarItem} that represents a menu alignment.
	 * 
	 * @param index Index of the alignment to generate an item for.
	 * @param s State, used to look up icons in.
	 * @return A newly created {@link MenuBarItem}.
	 */
	private MenuBarItem generateAlignmentItem(final int index, State s) {
		final Alignment alignment = MenuBar.Alignment.values()[index];
		// create the item
		MenuBarItem mbi = new MenuBarItem(alignment.getName(),
				getAlignmentIcon(index, s), Type.CHECKABLE_UNIQUE) {
			@Override
			public void onClick(int x, int y) {
				super.onClick(x, y);
				setMenuAlignments(alignment);
			}
			
			@Override
			public void onDrag(int x, int y) { /* ignored */ }
			
			@Override
			public void onScroll(int dWheel) { /* ignored */ }
		};
		// check the item if needed
		if (index == prefs.getInt("menu.alignment", DEF_MENU_ALIGNMENT)) {
			mbi.setChecked(true);
		}
		// return the result
		return mbi;
	}
	
	/**
	 * Generate a {@link MenuBarItem} that represents a menu position.
	 * 
	 * @param index Index of the position to generate an item for.
	 * @param s State, used to look up icons in.
	 * @return A newly created {@link MenuBarItem}.
	 */
	private MenuBarItem generatePositionItem(final int index, State s) {
		final Position pos = MenuBar.Position.values()[index];
		// create the item
		MenuBarItem mbi = new MenuBarItem(pos.getName(),
				getPositionIcon(index, s), Type.CHECKABLE_UNIQUE) {
			@Override
			public void onClick(int x, int y) {
				super.onClick(x, y);
				setMenuPositions(pos);
			}
			
			@Override
			public void onDrag(int x, int y) { /* ignored */ }
			
			@Override
			public void onScroll(int dWheel) { /* ignored */ }
		};
		// check the item if needed
		if (index == prefs.getInt("menu.position", DEF_MENU_POSITION)) {
			mbi.setChecked(true);
		}
		// return the result
		return mbi;
	}
	
	/**
	 * Generate a {@link MenuBarItem} that represents a menu presentation.
	 * 
	 * @param index Index of the presentation to generate an item for.
	 * @param s State, used to look up icons in.
	 * @return A newly created {@link MenuBarItem}.
	 */
	private MenuBarItem generatePresentationItem(final int index, State s) {
		final Presentation pres = MenuBar.Presentation.values()[index];
		// create the item
		MenuBarItem mbi = new MenuBarItem(pres.getName(),
				getPresentationIcon(index, s), Type.CHECKABLE_UNIQUE) {
			@Override
			public void onClick(int x, int y) {
				super.onClick(x, y);
				setMenuPresentations(pres);
			}
			
			@Override
			public void onDrag(int x, int y) { /* ignored */ }
			
			@Override
			public void onScroll(int dWheel) { /* ignored */ }
		};
		// check the item if needed
		if (index == prefs.getInt("menu.presentation", DEF_MENU_PRESENTATION)) {
			mbi.setChecked(true);
		}
		// return the result
		return mbi;
	}
	
	/**
	 * Return the icon that belongs to the {@link Alignment} with given ordinal index.
	 * 
	 * @param index Ordinal index of an {@link Alignment}.
	 * @param s State, used to look up icons in.
	 * @return An icon, or {@code null} if index is invalid.
	 */
	private Texture getAlignmentIcon(final int index, State s) {
		if (index < 0 || index >= MenuBar.Alignment.values().length) {
			return null;
		}
		
		switch (index) {
		case 0 :
			return s.textures.iconJustifyLeft;
		case 1 :
			return s.textures.iconJustifyCenter;
		case 2 :
			return s.textures.iconJustifyRight;
		default :
			return null;
		}
	}
	
	/**
	 * Return the icon that belongs to the {@link Position} with given ordinal index.
	 * 
	 * @param index Ordinal index of a {@link Position}.
	 * @param s State, used to look up icons in.
	 * @return An icon, or {@code null} if index is invalid.
	 */
	private Texture getPositionIcon(final int index, State s) {
		if (index < 0 || index >= MenuBar.Position.values().length) {
			return null;
		}
		
		switch (index) {
		case 0 :
			return s.textures.iconGoUp;
		case 1 :
			return s.textures.iconGoRight;
		case 2 :
			return s.textures.iconGoDown;
		case 3 :
			return s.textures.iconGoLeft;
		default :
			return null;
		}
	}
	
	/**
	 * Return the icon that belongs to the {@link Presentation} with given ordinal index.
	 * 
	 * @param index Ordinal index of a {@link Presentation}.
	 * @param s State, used to look up icons in.
	 * @return An icon, or {@code null} if index is invalid.
	 */
	private Texture getPresentationIcon(final int index, State s) {
		if (index < 0 || index >= MenuBar.Presentation.values().length) {
			return null;
		}
		
		switch (index) {
		case 0 :
			return s.textures.iconZoomOut;
		case 1 :
			return s.textures.iconZoomIn;
		default :
			return null;
		}
	}
	
	/**
	 * Try to load preferences of the user from last time.
	 * 
	 * @param s
	 */
	private void loadPreferences(State s) {
		if (prefs == null) {
			prefs = Preferences.userNodeForPackage(ACCGProgram.class);
		}
		
		s.mouseSensitivityFactor = prefs.getFloat("mouse.sensitivity", 1.0f);
		
		int alignment = prefs.getInt("menu.alignment", DEF_MENU_ALIGNMENT);
		setMenuAlignments(MenuBar.Alignment.values()[alignment]);
		int position = prefs.getInt("menu.position", DEF_MENU_POSITION);
		setMenuPositions(MenuBar.Position.values()[position]);
		int presentation = prefs.getInt("menu.presentation", DEF_MENU_PRESENTATION);
		setMenuPresentations(MenuBar.Presentation.values()[presentation]);
	}
	
	/**
	 * Change alignment of all menu bars.
	 * 
	 * @param alignment New alignment for all menu bars.
	 */
	private void setMenuAlignments(Alignment alignment) {
		// only change the main menu alignment, child menu
		// bars will follow the lead automatically
		menuBars[0].setAlignment(alignment);
		
		prefs.putInt("menu.alignment", alignment.ordinal());
	}
	
	/**
	 * Change position of all menu bars.
	 * 
	 * @param pos New position for all menu bars.
	 */
	private void setMenuPositions(Position pos) {
		// only change the main menu position, child menu
		// bars will follow the lead automatically
		menuBars[0].setPosition(pos);
		
		prefs.putInt("menu.position", pos.ordinal());
	}
	
	/**
	 * Change position of all menu bars.
	 * 
	 * @param pos New position for all menu bars.
	 */
	private void setMenuPresentations(Presentation pres) {
		// only change the main menu position, child menu
		// bars will follow the lead automatically
		menuBars[0].setPresentation(pres);
		
		prefs.putInt("menu.presentation", pres.ordinal());
	}
}
