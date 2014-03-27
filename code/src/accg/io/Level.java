package accg.io;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import accg.objects.Block;
import accg.objects.Luggage.LuggageColor;
import accg.objects.Orientation;
import accg.objects.blocks.EnterBlock;

/**
 * A Level is a collection of blocks, dimensions of a field, an actual level
 * number (optional), a name, a set of luggage that will enter the level through
 * the {@link EnterBlock} in the scene (optional, can be 'random') and an
 * optional maximum number of blocks that can be used in the scene.
 * 
 * <p>A Level is something that can be saved to a file and loaded from a file.
 * It is also possible to load some other Level inside a Level, to 'extend' it.
 * This is useful for example to have a predefined level and load any
 * modifications a user made later. To this end, some blocks may not be
 * "deletable". For this, the isDeletable property of a {@link Block} is used.
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class Level {

	/**
	 * Number of this level. A value of {@code -1} indicates that there is no
	 * associated number for this level.
	 */
	protected int levelNumber;
	/**
	 * Name of this level. Can be {@code null} to indicate that no specific
	 * name was given to this level.
	 */
	protected String levelName;
	/**
	 * A list of blocks in this Level. Position, orientation and deletability
	 * are stored in the blocks.
	 */
	protected ArrayList<Block> blocks;
	/**
	 * The length of this Level.
	 */
	protected int fieldLength;
	/**
	 * The width of this Level.
	 */
	protected int fieldWidth;
	/**
	 * The height of this Level.
	 */
	protected int fieldHeight;
	/**
	 * A list of luggage that will enter the scene. In case this is {@code null}
	 * luggage may be created randomly.
	 */
	protected ArrayList<LuggageColor> levelLuggage;
	/**
	 * Number of pieces of luggage that should enter the scene in total. In case
	 * {@link #levelLuggage} is not {@code null}, then it must hold that this
	 * number is equal to the number of objects in that list.
	 */
	protected int levelLuggageNum;
	/**
	 * Maximum number of blocks that a user can build in this level. If this
	 * value is {@code -1}, there is no limit.
	 */
	protected int blockLimit;
	
	/**
	 * Load a level from the specified stream.
	 * 
	 * @param levelStream Stream to read the level from.
	 * @throws InputMismatchException If the format of the file contains
	 *             something unexpected.
	 */
	public Level(InputStream levelStream) {
		try (Scanner levelScanner = new Scanner(levelStream)) {
			// read identification of file
			String id = levelScanner.nextLine();
			if (!id.equals("Luggage Tycoon level file.")) {
				throw new InputMismatchException("File does not appear to be"
						+ " a Luggage Tycoon level file.");
			}
			
			// read level name and number
			this.levelName = levelScanner.findInLine("\"[\\w.: -]*\"");
			if (this.levelName == null) {
				throw new InputMismatchException("Expected level name at line 2.");
			}
			this.levelName = this.levelName.substring(1, this.levelName.length() - 1);
			if (this.levelName.trim().isEmpty()) {
				this.levelName = null;
			}
			if (levelScanner.hasNextInt()) {
				this.levelNumber = levelScanner.nextInt();
			} else {
				this.levelNumber = -1;
			}
			levelScanner.nextLine();
			
			// read dimensions: length x width x height
			String dimLine = levelScanner.nextLine();
			Pattern dimPattern = Pattern.compile("(\\d+)x(\\d+)x(\\d+)");
			Matcher dimMatcher = dimPattern.matcher(dimLine);
			if (dimMatcher.matches()) {
				this.fieldLength = Integer.parseInt(dimMatcher.group(1));
				this.fieldWidth = Integer.parseInt(dimMatcher.group(2));
				this.fieldHeight = Integer.parseInt(dimMatcher.group(3));
			} else {
				throw new InputMismatchException("Expected dimensions at line 3.");
			}
			
			// read blocks
			if (!"blocks".equals(levelScanner.nextLine())) {
				throw new InputMismatchException("Expected 'blocks' identifier at line 4.");
			}
			this.blocks = new ArrayList<>();
			String line;
			int lineNum = 5;
			Pattern blockPattern = Pattern.compile("([A-Za-z]{2}) (\\d+) (\\d+) (\\d+) ([lurd]) (nd)?");
			while (!(line = levelScanner.nextLine()).startsWith("luggage")) {
				lineNum++;
				
				Matcher blockMatcher = blockPattern.matcher(line);
				if (blockMatcher.matches()) {
					Block b = Block.getBlock(blockMatcher.group(1),
							Integer.parseInt(blockMatcher.group(2)),
							Integer.parseInt(blockMatcher.group(3)),
							Integer.parseInt(blockMatcher.group(4)),
							Orientation.parseOrientation(blockMatcher.group(5)),
							(blockMatcher.group(6) == null));
					if (b == null) {
						throw new InputMismatchException("Could not parse block at line " + lineNum + ".");
					}
					if (b.getX() < 0 || fieldLength <= b.getX()) {
						throw new InputMismatchException("Block at line " + lineNum + ": X-coordinate is not in bounds.");
					}
					if (b.getY() < 0 || fieldWidth <= b.getY()) {
						throw new InputMismatchException("Block at line " + lineNum + ": Y-coordinate is not in bounds.");
					}
					if (b.getZ() < 0 || fieldHeight <= b.getZ()) {
						throw new InputMismatchException("Block at line " + lineNum + ": Z-coordinate is not in bounds.");
					}
					this.blocks.add(b);
				} else {
					throw new InputMismatchException("Line \"" + line + "\" does not properly describe a "
							+ "block. Expected format is \"[blockid] [x] [y] [z] [orientation] [nd]?\".");
				}
			}
			
			// read luggage number
			Pattern luggagePattern = Pattern.compile("luggage(?:\\s+(\\d+))?");
			Matcher luggageMatcher = luggagePattern.matcher(line);
			if (luggageMatcher.matches()) {
				this.levelLuggageNum = Integer.parseInt(luggageMatcher.group(1));
			} else {
				this.levelLuggageNum = -1;
			}
			// read luggage pieces
			this.levelLuggage = new ArrayList<>();
			while (!(line = levelScanner.nextLine()).startsWith("blocklimit")) {
				lineNum++;
				LuggageColor lugCol = LuggageColor.parseLuggageColor(line);
				if (lugCol == null) {
					throw new InputMismatchException("Could not parse luggage \"" + line + "\" at line " + lineNum + ".");
				}
				this.levelLuggage.add(lugCol);
			}
			if (this.levelLuggageNum < 0) {
				this.levelLuggageNum = this.levelLuggage.size();
			}
			if (this.levelLuggage.size() == 0) {
				this.levelLuggage = null;
			}
			if (this.levelLuggage != null && this.levelLuggage.size() != this.levelLuggageNum) {
				throw new InputMismatchException("Number of luggage color items does not match with actual given number.");
			}
			
			// read block limit (if any)
			Pattern blockLimitPattern = Pattern.compile("blocklimit(?:\\s+(\\d+))?");
			Matcher blockLimitMatcher = blockLimitPattern.matcher(line);
			if (blockLimitMatcher.matches()) {
				this.blockLimit = Integer.parseInt(blockLimitMatcher.group(1));
			} else {
				this.blockLimit = -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new InputMismatchException("Unexpected level file format.");
		}
	}
	
	/**
	 * Returns the name of this level. In case no name was given, but a number
	 * was specified, "Level X" where {@code X == getLevelNumber()} will be
	 * returned. If {@code X < 0} then {@code null} is returned.
	 * @return The name of this level, using some fancy default values.
	 */
	public String getLevelName() {
		if (levelName != null) {
			return levelName;
		}
		if (levelNumber >= 0) {
			return "Level " + levelNumber;
		}
		return null;
	}
	
	/**
	 * Returns the level number of this level.
	 * @return The level number of this level.
	 */
	public int getLevelNumber() {
		return levelNumber;
	}
}
