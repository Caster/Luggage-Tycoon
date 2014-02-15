package accg.objects;

import static org.lwjgl.opengl.GL11.*;
import accg.State;

public class Floor extends DrawableObject {

	@Override
	public void draw(State s) {
		glDepthMask(false);
		glColor4f(0.3f, 0.7f, 0.2f, 1f);
		glBegin(GL_QUADS);
		{
			for (int x = 0; x < s.fieldLength; x++) {
				for (int y = 0; y < s.fieldWidth; y++) {
					glNormal3d(0, 0, 1);
					
					glVertex3d(x, y, 0);
					glVertex3d(x, y + 1, 0);
					glVertex3d(x + 1, y + 1, 0);
					glVertex3d(x + 1, y, 0);
				}
			}
		}
		glEnd();
		glDepthMask(true);

		glColor4f(0.2f, 0.5f, 0.14f, 1f);
		glBegin(GL_QUADS);
		{
			for (int x = 0; x < s.fieldLength; x++) {
				for (int y = 0; y < s.fieldWidth; y++) {
					glNormal3d(0, 0, 1);
					
					glVertex3d(x, y, 0);
					glVertex3d(x, y + 1, 0);
					glVertex3d(x + 0.05f, y + 1, 0);
					glVertex3d(x + 0.05f, y, 0);
					
					glVertex3d(x, y, 0);
					glVertex3d(x, y + 0.05f, 0);
					glVertex3d(x + 1, y + 0.05f, 0);
					glVertex3d(x + 1, y, 0);
					
					glVertex3d(x + 0.95f, y, 0);
					glVertex3d(x + 0.95f, y + 1, 0);
					glVertex3d(x + 1, y + 1, 0);
					glVertex3d(x + 1, y, 0);
					
					glVertex3d(x, y + 0.95f, 0);
					glVertex3d(x, y + 1, 0);
					glVertex3d(x + 1, y + 1, 0);
					glVertex3d(x + 1, y + 0.95f, 0);
				}
			}
		}
		glEnd();
	}
}
