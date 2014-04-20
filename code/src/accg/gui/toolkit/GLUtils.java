package accg.gui.toolkit;

import java.awt.Color;

import javax.vecmath.Vector3f;

/**
 * This class contains several static utility methods for working with OpenGL.
 */
public class GLUtils {
	
	/**
	 * The last colors that were fed to {@link #glColor4f(Color)}. The first
	 * index contains the most recent color, later indices contain earlier
	 * colors. Every call shifts the colors.
	 */
	private static Color[] lastColors = new Color[2];
	/**
	 * A vector that may be used in methods in this class. Spares object
	 * allocations for frequent calls.
	 */
	private static Vector3f d1 = new Vector3f();
	/**
	 * A vector that may be used in methods in this class. Spares object
	 * allocations for frequent calls.
	 */
	private static Vector3f d2 = new Vector3f();
	/**
	 * A vector that may be used in methods in this class. Spares object
	 * allocations for frequent calls.
	 */
	private static Vector3f normal = new Vector3f();
	/**
	 * An array of vectors that may be used in methods in this class. Spares
	 * object allocations for frequent calls.
	 */
	private static Vector3f[] quadArray = new Vector3f[4];
	
	/**
	 * Clamp a given value between 0.1 and PI - 0.1.
	 * 
	 * @param value The value to be clamped.
	 * @return A clamped value.
	 * @see #clamp(float, float, float)
	 */
	public static float clamp(float value) {
		return clamp(value, 0.1f, (float) (Math.PI - 0.1));
	}
	
	/**
	 * Clamp a given value between a given minimum and maximum value.
	 * Clamping means that when the given value is less than the given
	 * minimum, this minimum is returned. If the value is greater than
	 * the maximum, this maximum is returned. Otherwise, the value is
	 * unchanged.
	 * 
	 * <p>This function does not change any of its parameters.</p>
	 * 
	 * @param value The value to be clamped.
	 * @param min The minimum value for clamping.
	 * @param max The maximum value for clamping.
	 * @return A value between {@code min} and {@code max}.
	 */
	public static float clamp(float value, float min, float max) {
		if (value <= min)  return min;
		if (value >= max)  return max;
		return value;
	}
	
	/**
	 * Put glVertex3d commands that form a quad. Note that this function should
	 * be embedded in a glBegin(GL_QUADS)/glEnd() block for proper use. The quad
	 * is drawn at Z = 0.
	 * 
	 * @param xMin Minimum X-coordinate of quad to draw.
	 * @param xMax Maximum X-coordinate of quad to draw.
	 * @param yMin Minimum Y-coordinate of quad to draw.
	 * @param yMax Maximum Y-coordinate of quad to draw.
	 */
	public static void drawQuad(double xMin, double xMax, double yMin,
			double yMax) {
		org.lwjgl.opengl.GL11.glVertex3d(xMin, yMin, 0);
		org.lwjgl.opengl.GL11.glVertex3d(xMin, yMax, 0);
		org.lwjgl.opengl.GL11.glVertex3d(xMax, yMax, 0);
		org.lwjgl.opengl.GL11.glVertex3d(xMax, yMin, 0);
	}
	
	/**
	 * Given a quad as a series of points in counter-clockwise order, put
	 * glVertex3f commands that form that quad and also include glNormal3f
	 * commands automatically.
	 * 
	 * <p>The normal is being calculated as follows: consider the points as
	 * follows: <code>v1, v2, v3, v4</code>. We now first calculate
	 * <code>d1 = v2 - v1</code> and <code>d2 = v4 - v1</code>. The normal is
	 * now calculated as follows: <code>n = d1 x d2</code>.
	 * 
	 * @param quad The quad to put, given as a series of 4 points in counter-
	 *             clockwise order.
	 * @throws IllegalArgumentException In case {@code quad == null} or when
	 *             {@code quad.length != 4}.
	 */
	public static void drawQuadAndNormals(Vector3f[] quad) {
		if (quad == null || quad.length != 4) {
			throw new IllegalArgumentException("Cannot draw a quad that does "
					+ "not have exactly 4 points.");
		}
		
		d1.sub(quad[1], quad[0]);
		d2.sub(quad[3], quad[0]);
		normal.cross(d1, d2);
		normal.normalize();
		
		glNormal3f(normal);
		for (int i = 0; i < 4; i++) {
			glVertex3f(quad[i]);
		}
	}
	
	/**
	 * Given a quad as a series of points in counter-clockwise order, put
	 * glVertex3f commands that form that quad and also include glNormal3f
	 * commands automatically.
	 * 
	 * <p>The normal is being calculated as follows: consider the points as
	 * follows: <code>v1, v2, v3, v4</code>. We now first calculate
	 * <code>d1 = v2 - v1</code> and <code>d2 = v4 - v1</code>. The normal is
	 * now calculated as follows: <code>n = d1 x d2</code>.
	 * 
	 * @param quad The quad to put, given as a series of 4 points in counter-
	 *             clockwise order.
	 * @param scaleFactor Factor to multiply each and every coordinate with.
	 * @throws IllegalArgumentException In case {@code quad == null} or when
	 *             {@code quad.length != 4}.
	 */
	public static void drawQuadAndNormals(Vector3f[] quad, float scaleFactor) {
		if (quad == null || quad.length != 4) {
			throw new IllegalArgumentException("Cannot draw a quad that does "
					+ "not have exactly 4 points.");
		}
		
		d1.sub(quad[1], quad[0]);
		d2.sub(quad[3], quad[0]);
		normal.cross(d1, d2);
		normal.normalize();
		
		glNormal3f(normal);
		for (int i = 0; i < 4; i++) {
			glVertex3f(quad[i], scaleFactor);
		}
	}
	
	/**
	 * Given a series of quads, draw them using
	 * {@link #drawQuadAndNormals(Vector3f[])}.
	 * 
	 * @param quads An array of points spanning quads.
	 * @throws IllegalArgumentException In case {@code quads == null} or when
	 *             {@code quads.length == 0} or when
	 *             {@code quads.length % 4 != 0}.
	 */
	public static void drawQuadsAndNormals(Vector3f[] quads) {
		if (quads == null || quads.length == 0 || quads.length % 4 != 0) {
			throw new IllegalArgumentException("Parameter `quads` should hold "
					+ "a non-zero, multiple of 4 number of points.");
		}
		
		for (int i = 0; i < quads.length; i += 4) {
			System.arraycopy(quads, i, quadArray, 0, 4);
			drawQuadAndNormals(quadArray);
		}
	}
	
	/**
	 * Given a series of quads, draw them using
	 * {@link #drawQuadAndNormals(Vector3f[])}.
	 * 
	 * @param quads An array of points spanning quads.
	 * @param scaleFactor Factor to multiply each and every coordinate of every
	 *            vertex of every quad with.
	 * @throws IllegalArgumentException In case {@code quads == null} or when
	 *             {@code quads.length == 0} or when
	 *             {@code quads.length % 4 != 0}.
	 */
	public static void drawQuadsAndNormals(Vector3f[] quads, float scaleFactor) {
		if (quads == null || quads.length == 0 || quads.length % 4 != 0) {
			throw new IllegalArgumentException("Parameter `quads` should hold "
					+ "a non-zero, multiple of 4 number of points.");
		}
		
		for (int i = 0; i < quads.length; i += 4) {
			System.arraycopy(quads, i, quadArray, 0, 4);
			drawQuadAndNormals(quadArray, scaleFactor);
		}
	}
	
	/**
	 * Return the given value modulo 2*PI, where the value is guaranteed
	 * to be between 0 and 2*PI (so it is not negative).
	 * 
	 * @param value Value of which modulo should be taken.
	 * @return Value modulo 2*PI.
	 * @see #modulo(float, float, float)
	 */
	public static float modulo(float value) {
		return modulo(value, 0.0f, (float) (2 * Math.PI));
	}
	
	/**
	 * Return the given value modulo (max - min) in the range between min
	 * and max. This is guaranteed. Using min = 0, the "normal" modulo operator
	 * is obtained, but different from the built-in Java one in the sense that
	 * a returned value cannot be negative. If you want to use this, refer to
	 * {@link #modulo(float)} or just fill in 0f for min.
	 * 
	 * @param value Value of which modulo should be taken.
	 * @param min Minimum of range.
	 * @param max Maximum of range.
	 * @return The value modulo (max - min) in range min to max.
	 * @see #modulo(float)
	 */
	public static float modulo(float value, float min, float max) {
		return min + ((((value - min) % (max - min)) + (max - min)) % (max - min));
	}
	
	/**
	 * Issues a glClearColor(...) call based on a Color.
	 * @param c The color to use.
	 */
	public static void glClearColor(Color c) {
		org.lwjgl.opengl.GL11.glClearColor(c.getRed() / 255f,
				c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
	}
	
	/**
	 * Issues a glColor4f(...) call based on a Color.
	 * @param c The color to use.
	 */
	public static void glColor4f(Color c) {
		lastColors[1] = lastColors[0];
		lastColors[0] = c;
		org.lwjgl.opengl.GL11.glColor4f(c.getRed() / 255f,
				c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
	}
	
	/**
	 * Issues a glColor4f(...) call with the second-to-last color that was fed
	 * to {@link #glColor4f(Color)}.
	 */
	public static void glColor4fReset() {
		if (lastColors[1] != null) {
			glColor4f(lastColors[1]);
		}
	}
	
	/**
	 * Returns the last color set through the {@link #glColor4f(Color)} method.
	 * @return The last color set through the {@link #glColor4f(Color)} method.
	 */
	public static Color glGetLastColor() {
		return lastColors[0];
	}
	
	/**
	 * Issues a glVertex3f(...) call based on a Vector3f.
	 * @param v The vector to draw.
	 */
	public static void glVertex3f(Vector3f v) {
		org.lwjgl.opengl.GL11.glVertex3f(v.x, v.y, v.z);
	}
	
	/**
	 * Issues a glVertex3f(...) call based on a Vector3f.
	 * @param v The vector to draw.
	 * @param scaleFactor Factor to multiply each and every coordinate with.
	 */
	public static void glVertex3f(Vector3f v, float scaleFactor) {
		org.lwjgl.opengl.GL11.glVertex3f(v.x * scaleFactor,
				v.y * scaleFactor, v.z * scaleFactor);
	}
	
	/**
	 * Issues a glNormal3f(...) call based on a Vector3f.
	 * @param v The vector to draw.
	 */
	public static void glNormal3f(Vector3f v) {
		org.lwjgl.opengl.GL11.glNormal3f(v.x, v.y, v.z);
	}
}
