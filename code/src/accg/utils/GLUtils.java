package accg.utils;

import java.awt.Color;

import org.lwjgl.util.vector.Vector3f;

/**
 * This class contains several static utility methods for working with OpenGL.
 */
public class GLUtils {
	
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
	 * Issues a glColor4f(...) call based on a Color.
	 * @param c The color to use.
	 */
	public static void glColor4f(Color c) {
		org.lwjgl.opengl.GL11.glColor4f(c.getRed() / 255f,
				c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
	}
	
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
