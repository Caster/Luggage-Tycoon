package accg.objects;

import static accg.utils.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.io.File;
import java.nio.FloatBuffer;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;

import org.lwjgl.BufferUtils;

import accg.State;
import accg.utils.OBJModel;

/**
 * A luggage item.
 */
public class Luggage extends DrawableObject {

	/**
	 * The x-coordinate of this luggage item.
	 */
	public float x;
	
	/**
	 * The y-coordinate of this luggage item.
	 */
	public float y;
	
	/**
	 * The z-coordinate of this luggage item.
	 */
	public float z;
	
	/**
	 * The rotation of this luggage.
	 */
	public Quat4f orient;
	
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
	 * Creates a new, free-floating luggage item.
	 * 
	 * @param x The x-coordinate of this luggage item.
	 * @param y The y-coordinate of this luggage item.
	 * @param z The z-coordinate of this luggage item.
	 */
	public Luggage(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.orient = new Quat4f();
		
		this.color = LuggageColor.values()[(int) (Math.random() * LuggageColor.values().length)];
		
		if (caseModel == null) {
			caseModel = new OBJModel(new File("res/suitcase.obj"));
		}
	}
	
	@Override
	public void draw(State s) {
		
		glColor4f(color.getColor());
		
		glPushMatrix();
		glTranslated(x, y, z);
		
		Matrix4f matrix = new Matrix4f();
		matrix.set(orient);
		float[] values = new float[] {
				matrix.m00, matrix.m01, matrix.m02, matrix.m03,
				matrix.m10, matrix.m11, matrix.m12, matrix.m13,
				matrix.m20, matrix.m21, matrix.m22, matrix.m23,
				matrix.m30, matrix.m31, matrix.m32, matrix.m33
				};
		FloatBuffer fb = BufferUtils.createFloatBuffer(16);
		fb.put(values);
		fb.flip();
		glMultMatrix(fb);
		
		caseModel.draw();
		
		glPopMatrix();
	}
}
