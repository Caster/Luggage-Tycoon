package accg.objects;

import static accg.utils.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import accg.State;
import accg.State.ProgramMode;

public class Floor extends DrawableObject {
	
	/**
	 * Transparency of floor when drawing with shadows.
	 */
	private static final float SHADOW_TRANSPARENCY = 0.6f;
	
	/**
	 * Background color, used to draw a small non-textured margin around the
	 * floor when drawing shadows. 
	 */
	private Color backgroundColor;
	
	@Override
	public void draw(State s) {
		glColor3d(1, 1, 1);
		glDepthMask(false);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		if (s.drawingShadows) {
			glColor4f(1, 1, 1, SHADOW_TRANSPARENCY);
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
		// possibly draw a margin around the floor for shadows
		if (s.drawingShadows) {
			glDisable(GL_LIGHTING);
			int marginSize = 30;
			glColor4f(backgroundColor);
			glBegin(GL_QUADS);
			{
				drawQuad(-marginSize - 0.5f, -0.5f, -marginSize - 0.5f,
						s.fieldWidth + marginSize - 0.5f);
				drawQuad(s.fieldLength - 0.5f, s.fieldLength + marginSize - 0.5f,
						-marginSize - 0.5f, s.fieldWidth + marginSize - 0.5f);
				drawQuad(-0.5f, s.fieldLength - 0.5f, -marginSize - 0.5f, -0.5f);
				drawQuad(-0.5f, s.fieldLength - 0.5f, s.fieldWidth - 0.5f,
						s.fieldWidth + marginSize - 0.5f);
			}
			glEnd();
			glEnable(GL_LIGHTING);
		}
		glColor4f(1, 1, 1, 1);
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
	}
	
	/**
	 * Change the color that is used when drawing a semi-transparent margin
	 * around the floor in case shadows are drawn.
	 * 
	 * @param bgCol Color to use for background drawing.
	 */
	public void setBackgroundColor(Color bgCol) {
		this.backgroundColor = new Color(bgCol.getRed() / 255f,
				bgCol.getGreen() / 255f, bgCol.getBlue() / 255f,
				SHADOW_TRANSPARENCY);
	}
}
