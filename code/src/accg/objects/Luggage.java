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
	public double x;
	
	/**
	 * The y-coordinate of this luggage item.
	 */
	public double y;
	
	/**
	 * The z-coordinate of this luggage item.
	 */
	public double z;
	
	/**
	 * The x-component of the speed of this luggage item.
	 * This speed is relative to the world if <code>supportingBlock == null</code>,
	 * and relative to the <code>supportingBlock</code> otherwise.
	 */
	public double vx;
	
	/**
	 * The y-component of the speed of this luggage item.
	 * This speed is relative to the world if <code>supportingBlock == null</code>,
	 * and relative to the <code>supportingBlock</code> otherwise.
	 */
	public double vy;
	
	/**
	 * The z-component of the speed of this luggage item.
	 * This speed is relative to the world if <code>supportingBlock == null</code>,
	 * and relative to the <code>supportingBlock</code> otherwise.
	 */
	public double vz;
	
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
		RED(220, 130, 120),
		
		/**
		 * Blue luggage.
		 */
		BLUE(100, 150, 220);
		
		/**
		 * A {@link java.awt.Color} object that represents this color.
		 */
		private Color color;
		
		private Color getColor() {
			return color;
		}
		
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
	public Luggage(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.vx = 0;
		this.vy = 0;
		this.vz = 0;
		
		this.color = LuggageColor.RED;
		
		if (caseModel == null) {
			this.caseModel = new OBJModel(new File("res/suitcase.obj"));
		}
	}
	
	@Override
	public void draw(State s) {
		
		glColor4f(color.color);
		
		glPushMatrix();
		glTranslated(x, y, z);
		
		caseModel.draw();
		
		glPopMatrix();
	}
}
