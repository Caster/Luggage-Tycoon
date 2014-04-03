package accg;

import static accg.gui.toolkit.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.opengl.Display;

public class StartScreenBackground {

	public static void draw(State s) {
		glClearColor(ACCGProgram.BACKGROUND_COLOR);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(45, (float) Display.getWidth() / Display.getHeight(), 0.1f, 1000f);
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		gluLookAt(0, 0, 10,
				0, 0, 0,
				0, 1, 0);
		
		glColor4f(1, 1, 1, 1);
		
		// the floor
		glEnable(GL_TEXTURE_2D);
		s.textures.floorSimulationMode.bind();
		glBegin(GL_QUADS);
		{
			glTexCoord2f(-40, -30);
			glVertex3d(-100, -100, 0);
			glTexCoord2f(-40, 30);
			glVertex3d(-100, 100, 0);
			glTexCoord2f(40, 30);
			glVertex3d(100, 100, 0);
			glTexCoord2f(40, -30);
			glVertex3d(100, -100, 0);
		}
		glEnd();
		glDisable(GL_TEXTURE_2D);
	}
}
