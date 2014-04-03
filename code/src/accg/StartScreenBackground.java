package accg;

import static accg.gui.toolkit.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.io.InputStream;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import accg.i18n.Messages;

public class StartScreenBackground {
	
	private static Font largeFont;
	
	/**
	 * Loads the large font for the start screen.
	 */
	static {
		try {
			InputStream russoOneFontStream =
					ResourceLoader.getResourceAsStream("res/fonts/RussoOne-Regular.ttf");
			java.awt.Font russoOneAwt = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,
					russoOneFontStream);
			russoOneAwt = russoOneAwt.deriveFont(50f);
			largeFont = new TrueTypeFont(russoOneAwt, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void draw(State s) {
		glClearColor(ACCGProgram.BACKGROUND_COLOR);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		//gluPerspective(45, (float) Display.getWidth() / Display.getHeight(), 0.1f, 1000f);

        glOrtho(0, Display.getWidth(), Display.getHeight(), 0, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		glColor4f(1, 1, 1, 1);
		
		glDisable(GL_DEPTH_TEST);
		
		glEnable(GL_TEXTURE_2D);
		
		// the floor
		s.textures.floorSimulationMode.bind();
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 0);
			glVertex3d(0, 0, 0);
			glTexCoord2f(0, Display.getHeight() / 200f);
			glVertex3d(0, Display.getHeight(), 0);
			glTexCoord2f(Display.getWidth() / 200f, Display.getHeight() / 200f);
			glVertex3d(Display.getWidth(), Display.getHeight(), 0);
			glTexCoord2f(Display.getWidth() / 200f, 0);
			glVertex3d(Display.getWidth(), 0, 0);
		}
		glEnd();

		glEnable(GL_BLEND);
		String text = "Luggage Tycoon";
		int textWidth = largeFont.getWidth(text);
		int textHeight = largeFont.getHeight(text);
		largeFont.drawString((Display.getWidth() - textWidth) / 2,
				(Display.getHeight() - textHeight) / 2, text, Color.black);
		
		glDisable(GL_TEXTURE_2D);
	}
}
