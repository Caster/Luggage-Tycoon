package accg.objects;

import static org.lwjgl.opengl.GL11.*;
import accg.State;

/**
 * The walls of the building.
 */
public class Walls extends DrawableObject {

	@Override
	public void draw(State s) {
		
		glEnable(GL_CULL_FACE);
		glCullFace(GL_FRONT); // cull away only front faces
		
		glEnable(GL_TEXTURE_2D);
		s.textures.wall.bind();
		
		glBegin(GL_QUADS);
		{
			glNormal3d(1, 0, 0);
			glTexCoord2d(0, s.fieldHeight / 4.0);
			glVertex3d(-0.5, -0.5, 0);
			glTexCoord2d(0, 0);
			glVertex3d(-0.5, -0.5, s.fieldHeight / 4.0 + 1);
			glTexCoord2d(s.fieldWidth, 0);
			glVertex3d(-0.5, s.fieldWidth - 0.5, s.fieldHeight / 4.0 + 1);
			glTexCoord2d(s.fieldWidth, s.fieldHeight / 4.0);
			glVertex3d(-0.5, s.fieldWidth - 0.5, 0);
			
			glNormal3d(0, -1, 0);
			glTexCoord2d(s.fieldWidth, s.fieldHeight / 4.0);
			glVertex3d(-0.5, s.fieldWidth - 0.5, 0);
			glTexCoord2d(s.fieldWidth, 0);
			glVertex3d(-0.5, s.fieldWidth - 0.5, s.fieldHeight / 4.0 + 1);
			glTexCoord2d(s.fieldWidth + s.fieldLength, 0);
			glVertex3d(s.fieldLength - 0.5, s.fieldWidth - 0.5, s.fieldHeight / 4.0 + 1);
			glTexCoord2d(s.fieldWidth + s.fieldLength, s.fieldHeight / 4.0);
			glVertex3d(s.fieldLength - 0.5, s.fieldWidth - 0.5, 0);
			
			glNormal3d(-1, 0, 0);
			glTexCoord2d(0, s.fieldHeight / 4.0);
			glVertex3d(s.fieldLength - 0.5, s.fieldWidth - 0.5, 0);
			glTexCoord2d(0, 0);
			glVertex3d(s.fieldLength - 0.5, s.fieldWidth - 0.5, s.fieldHeight / 4.0 + 1);
			glTexCoord2d(s.fieldWidth, 0);
			glVertex3d(s.fieldLength - 0.5, -0.5, s.fieldHeight / 4.0 + 1);
			glTexCoord2d(s.fieldWidth, s.fieldHeight / 4.0);
			glVertex3d(s.fieldLength - 0.5, -0.5, 0);
			
			glNormal3d(0, 1, 0);
			glTexCoord2d(s.fieldWidth, s.fieldHeight / 4.0);
			glVertex3d(s.fieldLength - 0.5, -0.5, 0);
			glTexCoord2d(s.fieldWidth, 0);
			glVertex3d(s.fieldLength - 0.5, -0.5, s.fieldHeight / 4.0 + 1);
			glTexCoord2d(s.fieldWidth + s.fieldLength, 0);
			glVertex3d(-0.5, -0.5, s.fieldHeight / 4.0 + 1);
			glTexCoord2d(s.fieldWidth + s.fieldLength, s.fieldHeight / 4.0);
			glVertex3d(-0.5, -0.5, 0);
		}
		glEnd();
		
		glDisable(GL_TEXTURE_2D);
		
		glDisable(GL_CULL_FACE);
	}
}
