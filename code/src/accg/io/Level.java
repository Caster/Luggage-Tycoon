package accg.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import accg.State;
import accg.objects.Block;
import accg.objects.Luggage.LuggageColor;
import accg.objects.Orientation;
import accg.objects.World;
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
			ArrayList<EnterBlock> enterBlocks = new ArrayList<EnterBlock>();
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
					
					if (b instanceof EnterBlock) {
						enterBlocks.add((EnterBlock) b);
					}
				} else {
					throw new InputMismatchException("Line \"" + line + "\" does not properly describe a "
							+ "block. Expected format is \"[blockid] [x] [y] [z] [orientation] [nd]?\".");
				}
			}
			
			// read luggage number
			for (int i = 0; i < enterBlocks.size(); i++) {
				Pattern luggagePattern = Pattern.compile("luggage(?:\\s+(\\d+))?");
				Matcher luggageMatcher = luggagePattern.matcher(line);
				if (luggageMatcher.matches()) {
					enterBlocks.get(i).setLuggageNum(Integer.parseInt(luggageMatcher.group(1)));
				} else {
					enterBlocks.get(i).setLuggageNum(-1);
				}
				// read luggage pieces
				ArrayList<LuggageColor> luggageColors = new ArrayList<>();
				while (!(line = levelScanner.nextLine()).startsWith("blocklimit") &&
						!line.startsWith("luggage")) {
					lineNum++;
					LuggageColor lugCol = LuggageColor.parseLuggageColor(line);
					if (lugCol == null) {
						throw new InputMismatchException("Could not parse luggage \"" + line + "\" at line " + lineNum + ".");
					}
					luggageColors.add(lugCol);
				}
				if (luggageColors.size() == 0) {
					luggageColors = null;
				}
				enterBlocks.get(i).setLuggageColors(luggageColors);
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
	 * Load a level from the specified file.
	 * 
	 * @param file File to read the level from.
	 * @throws FileNotFoundException If the given file does not exist, is a
	 *             directory or can not be opened for reading.
	 * @throws InputMismatchException If the format of the file contains
	 *             something unexpected.
	 */
	public Level(File file) throws FileNotFoundException {
		this(new BufferedInputStream(new FileInputStream(file)));
	}
	
	/**
	 * Construct a level from the current state of the program. This basically
	 * means that all blocks that are currently placed in the scene are stored
	 * in this level, most other values are set to some default value.
	 * 
	 * @param s State of the program. Used to access {@link World}.
	 */
	public Level(State s) {
		this.levelNumber = -1;
		this.levelName = null;
		this.blocks = new ArrayList<>();
		this.fieldLength = s.fieldLength;
		this.fieldWidth = s.fieldWidth;
		this.fieldHeight = s.fieldHeight;
		this.blockLimit = -1;
		
		for (Block b : s.world.bc) {
			this.blocks.add(b);
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
	
	/**
	 * Change the limit on the number of blocks for this level.
	 * @param blockLimit The new block limit.
	 */
	public void setBlockLimit(int blockLimit) {
		this.blockLimit = blockLimit;
	}
	
	/**
	 * Change the name of this level.
	 * @param levelName The new name for this level.
	 */
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	
	/**
	 * Change the number of this level. Set to -1 to indicate that this level
	 * does not have a number.
	 * @param levelNumber New level number for this level.
	 */
	public void setLevelNumber(int levelNumber) {
		this.levelNumber = levelNumber;
	}
}
