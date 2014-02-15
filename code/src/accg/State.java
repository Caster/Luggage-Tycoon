package accg;

/**
 * The state of the program. This for example stores the frame number.
 */
public class State {
	
	/**
	 * Length of the field, in number of grid squares.
	 * 
	 * <p>This is the number of squares along the X-axis.</p>
	 */
	public int fieldLength = 20;
	
	/**
	 * Width of the field, in number of grid squares.
	 * 
	 * <p>This is the number of squares along the Y-axis.</p>
	 */
	public int fieldWidth = 16;
	
	/**
	 * The current frame number.
	 */
	public int frame;
	
	/**
	 * The current simulation time in seconds.
	 */
	public double time;
	
	/**
	 * The {@link Textures} object that contains the textures for the
	 * application.
	 */
	public Textures textures;
}
