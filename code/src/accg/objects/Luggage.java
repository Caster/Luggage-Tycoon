package accg.objects;

import static accg.gui.toolkit.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.io.File;
import java.nio.FloatBuffer;

import javax.vecmath.Matrix4f;

import org.lwjgl.BufferUtils;

import accg.State;
import accg.utils.OBJModel;

/**
 * A luggage item.
 */
public class Luggage extends DrawableObject {

	/**
	 * The weight of a piece of luggage, can be used in the simulation.
	 */
	public static final float WEIGHT = 20f;
	
	/**
	 * The matrix containing the transformation (translation and rotation)
	 * of this luggage item.
	 */
	public Matrix4f transform;
	
	/**
	 * The OBJ model for the colored parts of the case.
	 */
	private static OBJModel caseModelColor;
	
	/**
	 * The OBJ model for the black parts of the case.
	 */
	private static OBJModel caseModelBlack;
	
	/**
	 * The color of this luggage.
	 */
	private LuggageColor color;
	
	/**
	 * Colors a luggage item can have.
	 */
	public enum LuggageColor {
		
		/**
		 * Red luggage.
		 */
		RED(240, 120, 130),
		
		/**
		 * Orange luggage.
		 */
		ORANGE(230, 160, 80),
		
		/**
		 * Green luggage.
		 */
		GREEN(100, 220, 130),
		
		/**
		 * Blue luggage.
		 */
		BLUE(100, 150, 220);
		
		/**
		 * A {@link java.awt.Color} object that represents this color.
		 */
		private Color color;
		
		/**
		 * Returns the color as a {@link java.awt.Color} object.
		 * @return The color.
		 */
		private Color getColor() {
			return color;
		}
		
		/**
		 * Creates a new color for the luggage.
		 * 
		 * @param red The red component.
		 * @param green The green component.
		 * @param blue The blue component.
		 */
		LuggageColor(int red, int green, int blue) {
			this.color = new Color(red, green, blue);
		}
		
		/**
		 * Parse a LuggageColor from a string.
		 * 
		 * @param s String to parse.
		 * @return Parse LuggageColor, or {@code null} if no match was found for
		 *         the given string.
		 */
		public static LuggageColor parseLuggageColor(String s) {
			for (LuggageColor col : values()) {
				if (col.name().equals(s.toUpperCase())) {
					return col;
				}
			}
			return null;
		}
	}
	
	/**
	 * Creates a new luggage item on the given position, with a randomly chosen
	 * color (all possible colors can be chosen).
	 * 
	 * @param x The x-coordinate of this luggage item.
	 * @param y The y-coordinate of this luggage item.
	 * @param z The z-coordinate of this luggage item.
	 */
	public Luggage(float x, float y, float z) {
		this(x, y, z, LuggageColor.values()[(int) (Math.random() *
				LuggageColor.values().length)]);
	}
	
	/**
	 * Creates a new luggage item on the given position, with the given color.
	 * 
	 * @param x The x-coordinate of this luggage item.
	 * @param y The y-coordinate of this luggage item.
	 * @param z The z-coordinate of this luggage item.
	 * @param color Color for the luggage.
	 */
	public Luggage(float x, float y, float z, LuggageColor color) {
		
		// initialize the transform
		transform = new Matrix4f(new float[] {
				1, 0, 0, x,
				0, 1, 0, y,
				0, 0, 1, z,
				0, 0, 0, 1
		});
		
		this.color = color;
		
		if (caseModelColor == null) {
			caseModelColor = new OBJModel(new File("res/suitcase-color.obj"));
		}
		if (caseModelBlack == null) {
			caseModelBlack = new OBJModel(new File("res/suitcase-black.obj"));
		}
	}
	
	@Override
	public void draw(State s) {
		
		glPushMatrix();
		float[] values = new float[] {
				transform.m00, transform.m10, transform.m20, transform.m30,
				transform.m01, transform.m11, transform.m21, transform.m31,
				transform.m02, transform.m12, transform.m22, transform.m32,
				transform.m03, transform.m13, transform.m23, transform.m33
				};
		FloatBuffer fb = BufferUtils.createFloatBuffer(16);
		fb.put(values);
		fb.flip();
		glMultMatrix(fb);
		
		glColor4f(color.getColor());
		caseModelColor.draw();
		
		glColor4f(0.1f, 0.1f, 0.1f, 1);
		caseModelBlack.draw();
		
		glPopMatrix();
	}
	
	/**
	 * Returns the color of this piece of luggage.
	 * @return The color of this piece of luggage.
	 */
	public LuggageColor getColor() {
		return color;
	}
}
