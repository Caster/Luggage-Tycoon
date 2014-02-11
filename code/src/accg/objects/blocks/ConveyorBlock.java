package accg.objects.blocks;

import accg.State;
import accg.objects.Block;

import static org.lwjgl.opengl.GL11.*;

public class ConveyorBlock extends Block {

	public ConveyorBlock(int x, int y, int z, Orientation orientation) {
		super(x, y, z, orientation);
	}

	@Override
	public void draw(State s) {
		
		// gray plane (temporary)
		glColor3d(0.7, 0.7, 0.65);
		
		glBegin(GL_QUADS);
		{
			glNormal3d(0, 0, 1);
			glVertex3d(x, y, z + 0.01);
			glVertex3d(x, y + 1, z + 0.01);
			glVertex3d(x + 1, y + 1, z + 0.01);
			glVertex3d(x + 1, y, z + 0.01);
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
				glVertex3d(x + 0.4, y + 0.1, z + 0.02);
				glVertex3d(x + 0.4, y + 0.6, z + 0.02);
				glVertex3d(x + 0.6, y + 0.6, z + 0.02);
				glVertex3d(x + 0.6, y + 0.1, z + 0.02);
			}
			glEnd();
			
			glBegin(GL_TRIANGLES);
			{
				glNormal3d(0, 0, 1);
				glVertex3d(x + 0.2, y + 0.6, z + 0.02);
				glVertex3d(x + 0.5, y + 0.9, z + 0.02);
				glVertex3d(x + 0.8, y + 0.6, z + 0.02);
			}
			glEnd();
		}
		glPopMatrix();
	}
}
