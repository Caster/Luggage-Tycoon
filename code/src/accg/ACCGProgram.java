package accg;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class ACCGProgram {
	
	public static void main(String[] args) {
		ACCGProgram p = new ACCGProgram();
		p.start();
	}
	
	public void start() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setTitle("Luggage Tycoon");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		// initialize stuff here
		
		while (!Display.isCloseRequested()) {
			
			// render stuff here

			glColor3f(0.3f, 0.7f, 0.2f);
			
			glBegin(GL_QUADS);
			{
				for (int x = 0; x < 10; x++) {
					for (int y = 0; y < 10; y++) {
						glVertex3d(x, y, 0);
						glVertex3d(x, y + 1, 0);
						glVertex3d(x + 1, y + 1, 0);
						glVertex3d(x + 1, y, 0);
					}
				}
			}
			glEnd();
			
			Display.update();
		}
		
		Display.destroy();
	}
}
