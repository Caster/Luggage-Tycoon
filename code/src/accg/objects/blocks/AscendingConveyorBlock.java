package accg.objects.blocks;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.glu.Cylinder;

import accg.State;
import accg.objects.Block;

public class AscendingConveyorBlock extends Block {

	public AscendingConveyorBlock(int x, int y, int z, Orientation orientation) {
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
		glTranslated(-0.375,-0.375, 0.25);
		glRotated(90, 0, 1, 0);
		c1.draw(0.125f, 0.125f, 0.75f, 16, 1);
		glPopMatrix();
		glPushMatrix();
		glTranslated(-0.375, 0.375, 0.5);
		glRotated(90, 0, 1, 0);
		c1.draw(0.125f, 0.125f, 0.75f, 16, 1);
		glPopMatrix();
		
		// the belt
		glColor3d(1, 1, 1);
		glEnable(GL_TEXTURE_2D);
		s.textures.conveyor.bind();
		glBegin(GL_QUADS);
		{
			glNormal3d(0, 0, 1);
			glTexCoord2d(-s.frame / 50.0, 0);
			glVertex3d(-0.375, -0.375, 0.375);
			glTexCoord2d(2 - s.frame / 50.0, 0);
			glVertex3d(-0.375, 0.375, 0.625);
			glTexCoord2d(2 - s.frame / 50.0, 1);
			glVertex3d(0.375, 0.375, 0.625);
			glTexCoord2d(-s.frame / 50.0, 1);
			glVertex3d(0.375, -0.375, 0.375);
			
			glNormal3d(0, 0, -1);
			glTexCoord2d(2 - s.frame / 50.0, 1);
			glVertex3d(-0.375, -0.375, 0.125);
			glTexCoord2d(-s.frame / 50.0, 1);
			glVertex3d(-0.375, 0.375, 0.375);
			glTexCoord2d(-s.frame / 50.0, 0);
			glVertex3d(0.375, 0.375, 0.375);
			glTexCoord2d(2 - s.frame / 50.0, 0);
			glVertex3d(0.375, -0.375, 0.125);
		}
		glEnd();
		glDisable(GL_TEXTURE_2D);
		
		glPopMatrix();
	}
}
