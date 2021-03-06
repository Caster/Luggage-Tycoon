package accg.objects.blocks;

import static accg.gui.toolkit.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.EnumSet;

import javax.vecmath.Vector3f;

import accg.State;
import accg.objects.Block;
import accg.objects.Luggage;
import accg.objects.Luggage.LuggageColor;
import accg.objects.Orientation;

/**
 * A block in which the luggage exits the scene (hopefully).
 */
public class LeaveBlock extends FlatConveyorBlock {
	
	/**
	 * Color of the hull surrounding the straight conveyor block.
	 */
	public static final Color HULL_COLOR = new Color(186, 189, 182);
	
	/**
	 * Series of points that define the hull around this block.
	 */
	public static final Vector3f[] HULL_POINTS = new Vector3f[EnterBlock.HULL_POINTS.length];
	/**
	 * Quad that is used while drawing luggage colors.
	 */
	protected static final Vector3f[] LUG_COL_QUAD = new Vector3f[4];
	static {
		for (int i = 0; i < HULL_POINTS.length; i++) {
			HULL_POINTS[i] = new Vector3f(-EnterBlock.HULL_POINTS[i].x,
					-EnterBlock.HULL_POINTS[i].y, EnterBlock.HULL_POINTS[i].z);
		}
		for (int i = 0; i < 4; i++) {
			LUG_COL_QUAD[i] = new Vector3f(-EnterBlock.LUG_COL_QUAD[i].x,
					-EnterBlock.LUG_COL_QUAD[i].y, EnterBlock.LUG_COL_QUAD[i].z);
		}
	}
	
	/**
	 * The shutter open factor. 0 means completely open, 1 means completely
	 * closed.
	 */
	public float sof = 1;
	
	/**
	 * Colors of luggage that are accepted by this block.
	 */
	private ArrayList<LuggageColor> acceptColors;
	
	/**
	 * Count of how many pieces of luggage have arrived at this block.
	 * (And have been accepted, so have the correct color.)
	 */
	private int arrivedLuggageCount;
	
	/**
	 * Creates a new LeaveBlock on the specified position.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @param orientation The orientation of this block.
	 */
	public LeaveBlock(int x, int y, int z, Orientation orientation) {
		this(x, y, z, orientation, true);
	}
	
	/**
	 * Creates a new LeaveBlock on the specified position.
	 * 
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @param orientation The orientation of this block.
	 * @param deletable If this block may be deleted by a user or not.
	 */
	public LeaveBlock(int x, int y, int z, Orientation orientation,
			boolean deletable) {
		super(x, y, z, orientation, deletable);
		
		this.type = ConveyorBlockType.LEAVE;
		this.acceptColors = null;
		this.arrivedLuggageCount = 0;
	}
	
	/**
	 * Creates a new LeaveBlock by copying an existing one.
	 * @param block The block to copy from.
	 */
	public LeaveBlock(LeaveBlock block) {
		this(block.x, block.y, block.z, block.orientation);
	}

	@Override
	public void draw(State s) {
		
		super.draw(s);
		
		// update the shutter
		boolean shouldOpen = isLuggageNear(s);
		if (shouldOpen) {
			sof = Math.max(0, sof - 0.025f);
		} else {
			sof = Math.min(1, sof + 0.025f);
		}
		
		// draw the hull and shutter
		glPushMatrix();
		glTranslated(x, y, z / 4.0);
		glRotated(-orientation.angle, 0, 0, 1);
		
		drawLuggageColorQuads();
		
		glEnable(GL_TEXTURE_2D);
		s.textures.shutterExit.bind();
		glBegin(GL_QUADS);
		{
			glNormal3f(0, -1, 0);
			// completely open is at 0.85, not 0.875, therefore not 1 but 0.947
			glTexCoord2f(1, 0.947f - sof);
			glVertex3f(-0.375f, -0.375f, 0.875f);
			glTexCoord2f(0, 0.947f - sof);
			glVertex3f(0.375f, -0.375f, 0.875f);
			glTexCoord2f(0, 1);
			glVertex3f(0.375f, -0.375f, 0.85f - sof * 0.45f);
			glTexCoord2f(1, 1);
			glVertex3f(-0.375f, -0.375f, 0.85f - sof * 0.45f);
		}
		glEnd();
		glDisable(GL_TEXTURE_2D);
		
		glPopMatrix();
	}

	/**
	 * Draw some quads that indicate which colors of luggage can be generated.
	 */
	protected void drawLuggageColorQuads() {
		LUG_COL_QUAD[0].z = 0.65f; LUG_COL_QUAD[1].z = 0.725f;
		LUG_COL_QUAD[2].z = 0.725f; LUG_COL_QUAD[3].z = 0.65f;
		
		for (LuggageColor lc : (acceptColors == null ? EnumSet.allOf(LuggageColor.class) : acceptColors)) {
			glColor4f(lc.getColor());
			glBegin(GL_QUADS);
			drawQuadAndNormals(LUG_COL_QUAD);
			glEnd();
			glColor4fReset();
			
			for (Vector3f v : LUG_COL_QUAD) {
				v.z += 0.085f; // 0.075 height and some padding
			}
		}
	}
	
	@Override
	public Block clone() {
		return new LeaveBlock(this);
	}
	
	/**
	 * Returns a list of colors that are accepted by this block.
	 * @return A list of colors that are accepted by this block.
	 *         If any color is accepted, {@code null} is returned.
	 */
	public ArrayList<LuggageColor> getAcceptColors() {
		return acceptColors;
	}
	
	/**
	 * Returns the number of pieces of luggage that have arrived at this block
	 * and have been accepted, that is, had the right color.
	 * @return The number of pieces of luggage that has been accepted.
	 */
	public int getArrivedLuggageCount() {
		return arrivedLuggageCount;
	}
	
	@Override
	public String getBlockID() {
		return "lb";
	}
	
	@Override
	public ConveyorBlockType getConveyorBlockType() {
		return ConveyorBlockType.LEAVE;
	}
	
	@Override
	public int getHeight() {
		return 4;
	}
	
	@Override
	public Vector3f[] getHullPoints() {
		return HULL_POINTS;
	}
	
	@Override
	public Vector3f[] getHullRoofPoints() {
		return null;
	}
	
	/**
	 * Increments the number of pieces of luggage that have arrived at this
	 * block and have been accepted by one.
	 */
	public void incrementArrivedLuggageCount() {
		arrivedLuggageCount++;
	}
	
	/**
	 * Resets the number of pieces of luggage that have arrived at this
	 * block and have been accepted to zero.
	 */
	public void resetArrivedLuggageCount() {
		arrivedLuggageCount = 0;
	}
	
	/**
	 * Change the list of colors that is accepted by this block.
	 * @param acceptColors The new list of accepted colors, or {@code null} if
	 *            any block may be accepted.
	 */
	public void setAcceptColors(ArrayList<LuggageColor> acceptColors) {
		this.acceptColors = acceptColors;
	}
	
	/**
	 * Returns whether there is luggage near the shutter.
	 * 
	 * @param s The state (used to read the luggage from).
	 * @return <code>true</code> if there is luggage near the shutter,
	 * <code>false</code> otherwise.
	 */
	private boolean isLuggageNear(State s) {
		
		Vector3f shutterPosition = new Vector3f();
		shutterPosition.x = x;
		shutterPosition.y = y;
		shutterPosition.z = z / 4.0f;
		shutterPosition = orientation.moveFrom(shutterPosition, -0.6f);
		
		for (Luggage l : s.world.luggage) {
			Vector3f position = new Vector3f();
			l.transform.get(position);
			Vector3f difference = new Vector3f();
			difference.sub(position, shutterPosition);
			if (difference.lengthSquared() < 0.5f) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void drawArrow(State s) {
		// do not draw an arrow for LeaveBlocks
	}
}
