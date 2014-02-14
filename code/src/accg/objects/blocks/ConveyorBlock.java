package accg.objects.blocks;

import org.lwjgl.util.glu.Cylinder;

import accg.State;
import accg.objects.Block;
import static org.lwjgl.opengl.GL11.*;

public class ConveyorBlock extends Block {

	public ConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation);
	}

	@Override
	public void draw(State s) {
		
		glPushMatrix();
		glTranslated(x, y, z);
		
		Cylinder c1 = new Cylinder();

		glColor3d(0.7, 0.7, 0.65);
		glPushMatrix();
		glTranslated(0.125, 0.125, 0.25);
		glRotated(90, 0, 1, 0);
		c1.draw(0.125f, 0.125f, 0.75f, 16, 1);
		glPopMatrix();
		glPushMatrix();
		glTranslated(0.125, 0.875, 0.25);
		glRotated(90, 0, 1, 0);
		c1.draw(0.125f, 0.125f, 0.75f, 16, 1);
		glPopMatrix();
		
		// gray plane (temporary)
		glColor3d(0.7, 0.7, 0.65);
		
		glBegin(GL_QUADS);
		{
			glNormal3d(0, 0, 1);
			glVertex3d(0, 0, z + 0.01);
			glVertex3d(0, 1, z + 0.01);
			glVertex3d(1, 1, z + 0.01);
			glVertex3d(1, 0, z + 0.01);
		}
		glEnd();
		
		// arrow (temporary)
		glColor3d(0.5, 0.5, 0.47);
		
		glPushMatrix();
		{
			glTranslatef(0, (s.frame / 100f) % 1 - 0.5f, 0);
			glBegin(GL_QUADS);
			{
				glNormal3d(0, 0, 1);
				glVertex3d(0.4, 0.1, 0.02);
				glVertex3d(0.4, 0.6, 0.02);
				glVertex3d(0.6, 0.6, 0.02);
				glVertex3d(0.6, 0.1, 0.02);
			}
			glEnd();
			
			glBegin(GL_TRIANGLES);
			{
				glNormal3d(0, 0, 1);
				glVertex3d(0.2, 0.6, 0.02);
				glVertex3d(0.5, 0.9, 0.02);
				glVertex3d(0.8, 0.6, 0.02);
			}
			glEnd();
		}
		glPopMatrix();
		
		glPopMatrix();
	}
}
