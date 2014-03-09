package accg.objects;

import static org.lwjgl.opengl.GL11.*;
import accg.State;
import accg.State.ProgramMode;

public class Floor extends DrawableObject {

	@Override
	public void draw(State s) {
		
		glColor3d(1, 1, 1);
		glDepthMask(false);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		if (s.drawingShadows) {
			glColor4f(1, 1, 1, 0.85f);
		}
		glEnable(GL_TEXTURE_2D);
		
		if (s.programMode == ProgramMode.BUILDING_MODE) {
			s.textures.floorBuildMode.bind();
		} else {
			s.textures.floorSimulationMode.bind();
		}
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
		glColor4f(1, 1, 1, 1);
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
	}
}
