package accg;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

import accg.camera.Camera;
import accg.objects.Luggage;
import accg.objects.World;

public class ACCGProgram {
	
	private boolean escPressed = false;
	private DisplayMode windowedMode, fullScreenMode;
	private Camera camera;
	
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
		
		// initialize stuff here
		State s = new State();
		s.textures = new Textures();
		s.world = new World();
		s.simulation = new Simulation();
		s.startTime = Sys.getTime() / Sys.getTimerResolution();
		camera = new Camera(s);
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

			s.world.draw(s);
			
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
		
		while (Mouse.next()) {
			if (!Mouse.getEventButtonState()) {
				int dx = Mouse.getEventDX();
				int dy = Mouse.getEventDY();
				
				// handle left mouse button: mouse button 1
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
}
