package accg.gui.toolkit;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;

/**
 * This class provides some useful static functions for GUI widgets.
 */
public class GUIUtils {
	
	/**
	 * This function switches from 3D rendering to 2D rendering.
	 * 
	 * It was taken from http://lwjgl.org/forum/index.php/topic,4431.msg23872.html#msg23872.
	 */
    public static void make2D() {
        glDisable(GL_LIGHTING);
        glDisable(GL_DEPTH_TEST);
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, Display.getWidth(), Display.getHeight(), 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();
    }

    /**
     * This function switches from 2D rendering back to 3D rendering.
     * 
     * It was taken from http://lwjgl.org/forum/index.php/topic,4431.msg23872.html#msg23872.
     */
    public static void make3D() {
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
    }
}
