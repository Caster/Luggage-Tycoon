package accg.objects.blocks;

import org.lwjgl.util.glu.Cylinder;

import accg.State;
import accg.objects.Block;
import static org.lwjgl.opengl.GL11.*;

public class BendLeftConveyorBlock extends Block {

	public BendLeftConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation);
	}

	@Override
	public void draw(State s) {
		
		glPushMatrix();
		glTranslated(x + 0.5, y + 0.5, z / 4.0);
		glRotated(orientation.angle, 0, 0, 1);
		
		Cylinder c1 = new Cylinder();

		glColor3d(0.7, 0.7, 0.65);
		glPushMatrix();
		glTranslated(-0.375, -0.375, 0.25);
		glRotated(90, 0, 1, 0);
		c1.draw(0.125f, 0.125f, 0.75f, 16, 1);
		glPopMatrix();
		glPushMatrix();
		glTranslated(-0.375, -0.375, 0.25);
		glRotated(-90, 1, 0, 0);
		c1.draw(0.125f, 0.125f, 0.75f, 16, 1);
		glPopMatrix();
		
		// the belt
		glColor3d(1, 1, 1);
		glEnable(GL_TEXTURE_2D);
		s.textures.conveyor.bind();
		glBegin(GL_QUAD_STRIP);
		{
			glNormal3d(0, 0, 1);
			for (int segment = 0; segment <= 32; segment++) {
				glTexCoord2d(segment / 16.0 - s.frame / 50.0, 0);
				glVertex3d(-0.375, -0.375, 0.375);
				glTexCoord2d(segment / 16.0 - s.frame / 50.0, 1);
				glVertex3d(-0.375 + 0.75 * Math.cos(segment * (Math.PI / 64)), -0.375 + 0.75 * Math.sin(segment * (Math.PI / 64)), 0.375);
			}
		}
		glEnd();
		glBegin(GL_QUAD_STRIP);
		{
			glNormal3d(0, 0, -1);
			for (int segment = 0; segment <= 32; segment++) {
				glTexCoord2d(segment / 16.0 - s.frame / 50.0, 0);
				glVertex3d(-0.375, -0.375, 0.125);
				glTexCoord2d(segment / 16.0 - s.frame / 50.0, 1);
				glVertex3d(-0.375 + 0.75 * Math.cos((32 - segment) * (Math.PI / 64)), -0.375 + 0.75 * Math.sin((32 - segment) * (Math.PI / 64)), 0.125);
			}
		}
		glEnd();
		glDisable(GL_TEXTURE_2D);
		
		glPopMatrix();
	}
}
