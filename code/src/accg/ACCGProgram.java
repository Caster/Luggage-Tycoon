package accg;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.awt.Font;
import java.io.InputStream;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.Util;
import org.lwjgl.util.Point;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import accg.camera.Camera;
import accg.gui.GUI;
import accg.gui.MenuBar;
import accg.gui.MenuBar.Alignment;
import accg.gui.MenuBar.Position;
import accg.gui.MenuBarItem;
import accg.objects.Luggage;
import accg.objects.World;

public class ACCGProgram {
	
	private boolean escPressed = false;
	private DisplayMode windowedMode, fullScreenMode;
	private Camera camera;
	/**
	 * Menu bars used in the program.
	 * 
	 *  0: main menu.
	 *  1: settings menu           (child of 0).
	 *  2: menu position menu      (child of 1).
	 *  3: menu alignment menu     (child of 1).
	 */
	private MenuBar[] menuBars;
	private Point clickedPoint;
	
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
		s.world = new World();
		s.textures = new Textures();
		s.simulation = new Simulation();
		s.startTime = Sys.getTime() / Sys.getTimerResolution();
		camera = new Camera(s);
		clickedPoint = null;
		
		// intialise GUI stuff
		menuBars = new MenuBar[4];
		menuBars[0] = new MenuBar(s, Position.TOP, Alignment.CENTER);
		menuBars[1] = new MenuBar(s, menuBars[0]);
		menuBars[2] = new MenuBar(s, menuBars[1]);
		menuBars[3] = new MenuBar(s, menuBars[1]);
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
			
			// render stuff here
			s.frame++;
			s.time = (double) Sys.getTime() / Sys.getTimerResolution() - s.startTime;
			s.simulation.update(s);
			
			// TODO temporary: add some luggage
			if (s.frame % 25 == 0) {
				s.world.luggage.addObject(new Luggage(Math.random() * 10, Math.random() * 10, 2));
			}
			
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
			s.world.draw(s);
			
			// draw the menu bar
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
		if (dWheel < 0) {
			camera.moveUp();
		} else if (dWheel > 0) {
			camera.moveDown();
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
						menuBars[i].handleMouseMoveEvent(
								Mouse.getX(), Mouse.getY());
					}
					
					handledMouseMove = true;
				}
				
				// handle left mouse button: mouse button 0
				if (!handledButton[0] && Mouse.isButtonDown(0)) {
					camera.moveByMouse(dx, dy);
					
					handledButton[0] = true;
				}
				
				// handle middle mouse button: mouse button 2
				if (!handledButton[2] && Mouse.isButtonDown(2)) {					
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
	private void initialiseMenus(MenuBar[] menus, State s) {
		// main menu
		menus[0].addMenuBarItem(new MenuBarItem("Simulate", s.textures.iconStart) {
			@Override
			public void onClick() {
				System.out.println("Start simulation mode!");
			}
		});
		
		menus[0].addMenuBarItem(new MenuBarItem("Open", s.textures.iconOpen) {
			@Override
			public void onClick() {
				System.out.println("Open!");
			}
		});
		
		menus[0].addMenuBarItem(new MenuBarItem("Save", s.textures.iconSave) {
			@Override
			public void onClick() {
				System.out.println("Save!");
			}
		});
		
		menus[0].addMenuBarItem(new MenuBarItem("Settings", s.textures.iconConfigure) {
			@Override
			public void onClick() {
				menuBars[1].toggleVisible();
			}
		});
		
		menus[0].addMenuBarItem(new MenuBarItem("Exit", s.textures.iconExit) {
			@Override
			public void onClick() {
				escPressed = true;
			}
		});
		
		// settings menu
		menus[1].addMenuBarItem(new MenuBarItem("Menu position", s.textures.iconConfigure) {
			@Override
			public void onClick() {
				menuBars[2].toggleVisible();
				if (menuBars[2].isVisible()) {
					menuBars[3].setVisible(false);
				}
			}
		});
		
		menus[1].addMenuBarItem(new MenuBarItem("Menu alignment", s.textures.iconConfigure) {
			@Override
			public void onClick() {
				menuBars[3].toggleVisible();
				if (menuBars[3].isVisible()) {
					menuBars[2].setVisible(false);
				}
			}
		});
		
		menus[1].addMenuBarItem(new MenuBarItem("Mouse sensitivity", s.textures.iconConfigure) {
			@Override
			public void onClick() {
				System.out.println("Mouse sensitivity?");
			}
		});
		
		// menu position menu
		menus[2].addMenuBarItem(new MenuBarItem("Top", s.textures.iconGoUp) {
			@Override
			public void onClick() {
				setMenuPositions(Position.TOP);
			}
		});
		
		menus[2].addMenuBarItem(new MenuBarItem("Bottom", s.textures.iconGoDown) {
			@Override
			public void onClick() {
				setMenuPositions(Position.BOTTOM);
			}
		});
		
		menus[2].addMenuBarItem(new MenuBarItem("Left", s.textures.iconGoLeft) {
			@Override
			public void onClick() {
				setMenuPositions(Position.LEFT);
			}
		});
		
		menus[2].addMenuBarItem(new MenuBarItem("Right", s.textures.iconGoRight) {
			@Override
			public void onClick() {
				setMenuPositions(Position.RIGHT);
			}
		});
		
		// menu alignment menu
		menus[3].addMenuBarItem(new MenuBarItem("Left / top", s.textures.iconJustifyLeft) {
			@Override
			public void onClick() {
				setMenuAlignments(Alignment.BEGIN);
			}
		});
		
		menus[3].addMenuBarItem(new MenuBarItem("Center", s.textures.iconJustifyCenter) {
			@Override
			public void onClick() {
				setMenuAlignments(Alignment.CENTER);
			}
		});
		
		menus[3].addMenuBarItem(new MenuBarItem("Right / bottom", s.textures.iconJustifyRight) {
			@Override
			public void onClick() {
				setMenuAlignments(Alignment.END);
			}
		});
	}
	
	/**
	 * Change alignment of all menu bars.
	 * 
	 * @param alignment New alignment for all menu bars.
	 */
	private void setMenuAlignments(Alignment alignment) {
		for (int i = 0; i < menuBars.length; i++) {
			menuBars[i].setAlignment(alignment);
		}
	}
	
	/**
	 * Change position of all menu bars.
	 * 
	 * @param pos New position for all menu bars.
	 */
	private void setMenuPositions(Position pos) {
		for (int i = 0; i < menuBars.length; i++) {
			menuBars[i].setPosition(pos);
		}
	}
}
