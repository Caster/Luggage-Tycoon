package accg;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.Sphere;

import accg.camera.Camera;
import accg.objects.World;

public class ACCGProgram {
	
	private boolean escPressed = false;
	private DisplayMode windowedMode, fullScreenMode;
	private Camera camera = new Camera();
	
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
		World world = new World();
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
		glEnable(GL_DEPTH_TEST);
		
		while (!Display.isCloseRequested() && !escPressed) {
			
			// handle events
			handleKeyEvents();
			handlePressedKeys();
			
			// render stuff here
			s.frame++;
			
			glClearColor(0.8f, 0.8f, 0.77f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glViewport(0, 0, Display.getWidth(), Display.getHeight());
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			gluPerspective(45, (float) Display.getWidth() / Display.getHeight(), 0.1f, 1000f);
			
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			camera.setLookAt();

			world.draw(s);
			
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
				case Keyboard.KEY_F:
					try {
						if (Display.isFullscreen()) {
							Display.setDisplayMode(windowedMode);
							Display.setFullscreen(false);
						} else {
							Display.setDisplayMode(fullScreenMode);
							Display.setFullscreen(true);
						}
					} catch (LWJGLException e) {
						// TODO Auto-generated catch block
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
