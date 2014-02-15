package accg.objects;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;

import accg.State;
import accg.utils.OBJModel;

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
	 * Only applicable when <code>supportingBlock == null</code>.
	 */
	public double vx;
	
	/**
	 * The y-component of the speed of this luggage item.
	 * Only applicable when <code>supportingBlock == null</code>.
	 */
	public double vy;
	
	/**
	 * The z-component of the speed of this luggage item.
	 * Only applicable when <code>supportingBlock == null</code>.
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
	private OBJModel caseModel;
	
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
		
		this.caseModel = new OBJModel(new File("res/suitcase.obj"));
	}
	
	@Override
	public void draw(State s) {
		
		glColor3f(0.3f, 0.6f, 0.8f);
		
		glPushMatrix();
		glTranslated(x, y, z);
		
		caseModel.draw();
		
		glPopMatrix();
	}
}
