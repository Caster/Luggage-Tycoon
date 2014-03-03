package accg.objects;

import static accg.utils.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.io.File;

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
	 * The x-component of the speed of this luggage item.
	 * This speed is relative to the world if <code>supportingBlock == null</code>,
	 * and relative to the <code>supportingBlock</code> otherwise.
	 */
	public float vx;
	
	/**
	 * The y-component of the speed of this luggage item.
	 * This speed is relative to the world if <code>supportingBlock == null</code>,
	 * and relative to the <code>supportingBlock</code> otherwise.
	 */
	public float vy;
	
	/**
	 * The z-component of the speed of this luggage item.
	 * This speed is relative to the world if <code>supportingBlock == null</code>,
	 * and relative to the <code>supportingBlock</code> otherwise.
	 */
	public float vz;
	
	/**
	 * The z-angle rotation.
	 */
	public float anglez;
	
	/**
	 * The supporting block of this luggage item. This indicates the
	 * block on which this luggage lies, and this block is also responsible
	 * for handling the animation.
	 * 
	 * If this is <code>null</code>, there is no supporting block, i.e. the
	 * luggage is floating / falling.
	 */
	public Block supportingBlock;
	
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
		
		this.vx = 0;
		this.vy = 0;
		this.vz = 0;

		this.anglez = (float) (Math.random() * 2 * Math.PI);
		
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
		glRotated(Math.toDegrees(anglez), 0, 0, 1);
		
		caseModel.draw();
		
		glPopMatrix();
	}
}
