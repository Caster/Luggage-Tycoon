package accg.objects;

import static org.lwjgl.opengl.GL11.*;

import accg.State;

public class Walls extends DrawableObject {

	@Override
	public void draw(State s) {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_FRONT); // cull away only front faces
		
		glColor3f(0.6f, 0.4f, 0.1f);
		
		glBegin(GL_QUADS);
		{
			glNormal3d(1, 0, 0);
			glVertex3d(-0.5, -0.5, 0);
			glVertex3d(-0.5, -0.5, s.fieldHeight / 4.0);
			glVertex3d(-0.5, s.fieldWidth - 0.5, s.fieldHeight / 4.0);
			glVertex3d(-0.5, s.fieldWidth - 0.5, 0);
			
			glNormal3d(0, -1, 0);
			glVertex3d(-0.5, s.fieldWidth - 0.5, 0);
			glVertex3d(-0.5, s.fieldWidth - 0.5, s.fieldHeight / 4.0);
			glVertex3d(s.fieldLength - 0.5, s.fieldWidth - 0.5, s.fieldHeight / 4.0);
			glVertex3d(s.fieldLength - 0.5, s.fieldWidth - 0.5, 0);
			
			glNormal3d(-1, 0, 0);
			glVertex3d(s.fieldLength - 0.5, s.fieldWidth - 0.5, 0);
			glVertex3d(s.fieldLength - 0.5, s.fieldWidth - 0.5, s.fieldHeight / 4.0);
			glVertex3d(s.fieldLength - 0.5, -0.5, s.fieldHeight / 4.0);
			glVertex3d(s.fieldLength - 0.5, -0.5, 0);
			
			glNormal3d(0, 1, 0);
			glVertex3d(s.fieldLength - 0.5, -0.5, 0);
			glVertex3d(s.fieldLength - 0.5, -0.5, s.fieldHeight / 4.0);
			glVertex3d(-0.5, -0.5, s.fieldHeight / 4.0);
			glVertex3d(-0.5, -0.5, 0);
		}
		glEnd();
		
		glDisable(GL_CULL_FACE);
	}
}
