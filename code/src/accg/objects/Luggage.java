package accg.objects;

import static accg.utils.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.io.File;
import java.nio.FloatBuffer;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

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
	}
	
	/**
	 * Creates a new luggage item on the given position.
	 * 
	 * @param x The x-coordinate of this luggage item.
	 * @param y The y-coordinate of this luggage item.
	 * @param z The z-coordinate of this luggage item.
	 */
	public Luggage(float x, float y, float z) {
		
		// initialize the transform
		transform = new Matrix4f(new float[] {
				1, 0, 0, x,
				0, 1, 0, y,
				0, 0, 1, z,
				0, 0, 0, 1
		});
		
		this.color = LuggageColor.values()[(int) (Math.random() * LuggageColor.values().length)];
		
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
	
	@Override
	public void setPosition(Vector3f position) {
		transform.m03 = position.x;
		transform.m13 = position.y;
		transform.m23 = position.z;
	}
}
