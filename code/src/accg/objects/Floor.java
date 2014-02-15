package accg.objects;

import static org.lwjgl.opengl.GL11.*;
import accg.State;

public class Floor extends DrawableObject {

	@Override
	public void draw(State s) {
		glColor3d(1, 1, 1);
		glEnable(GL_TEXTURE_2D);
		s.textures.floorBuildMode.bind();
		glBegin(GL_QUADS);
		{
			for (int x = 0; x < s.fieldLength; x++) {
				for (int y = 0; y < s.fieldWidth; y++) {
					glNormal3d(0, 0, 1);
					
					glTexCoord2d(0, 0);
					glVertex3d(x - 0.5, y - 0.5, 0);
					glTexCoord2d(1, 0);
					glVertex3d(x - 0.5, y + 0.5, 0);
					glTexCoord2d(1, 1);
					glVertex3d(x + 0.5, y + 0.5, 0);
					glTexCoord2d(0, 1);
					glVertex3d(x + 0.5, y - 0.5, 0);
				}
			}
		}
		glEnd();
		glDisable(GL_TEXTURE_2D);
	}
}
