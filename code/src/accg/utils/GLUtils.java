package accg.utils;

import org.lwjgl.util.vector.Vector3f;

/**
 * This class contains several static utility methods for working with OpenGL.
 */
public class GLUtils {
	
	/**
	 * Issues a glVertex3f(...) call based on a Vector3f.
	 * @param v The vector to draw.
	 */
	public static void glVertex3f(Vector3f v) {
		org.lwjgl.opengl.GL11.glVertex3f(v.x, v.y, v.z);
	}
	
	/**
	 * Issues a glNormal3f(...) call based on a Vector3f.
	 * @param v The vector to draw.
	 */
	public static void glNormal3f(Vector3f v) {
		org.lwjgl.opengl.GL11.glNormal3f(v.x, v.y, v.z);
	}
}
