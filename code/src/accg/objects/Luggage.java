package accg.objects;

import static accg.utils.GLUtils.*;
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
	 * The matrix containing the transformation (translation and rotation)
	 * of this luggage item.
	 */
	public Matrix4f transform;
	
	/**
	 * The OBJ model for the case.
	 */
	private static OBJModel caseModel;
	
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
		
		if (caseModel == null) {
			caseModel = new OBJModel(new File("res/suitcase.obj"));
		}
	}
	
	@Override
	public void draw(State s) {
		
		glColor4f(color.getColor());
		
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
		
		caseModel.draw();
		
		glPopMatrix();
	}
}
