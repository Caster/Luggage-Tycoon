package accg.io;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.components.Button;
import accg.gui.toolkit.components.Label;
import accg.gui.toolkit.containers.Dialog;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.objects.Block;
import accg.objects.Luggage.LuggageColor;
import accg.objects.Orientation;
import accg.objects.World;
import accg.objects.blocks.EnterBlock;
import accg.objects.blocks.LeaveBlock;
import accg.simulation.Simulation;

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
	 * A short text that is displayed when the level is started. This can be
	 * <code>null</code> if no hint is to be displayed.
	 */
	protected String levelHint;
	
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
			String hintOrBlocksline = levelScanner.nextLine();
			if (!"blocks".equals(hintOrBlocksline)) {
				this.levelHint = hintOrBlocksline;
				hintOrBlocksline = levelScanner.nextLine();
			}
			if (!"blocks".equals(hintOrBlocksline)) {
				throw new InputMismatchException("Expected 'blocks' identifier.");
			}
			this.blocks = new ArrayList<>();
			String line = "";
			int lineNum = 5;
			Pattern blockPattern = Pattern.compile("([A-Za-z]{2,3})\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+([lurd])(?:\\s+(nd)?)?"
					+ "(?:\\s+\\[(?:(-?\\d+)\\s+)?(\\w+[\\s+\\w+]*)?\\])?");
			while (levelScanner.hasNextLine() && !(line = levelScanner.nextLine()).startsWith("blocklimit")) {
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
						EnterBlock eb = (EnterBlock) b;
						if (blockMatcher.groupCount() > 6 && blockMatcher.group(7) != null) {
							eb.setLuggageNum(Integer.parseInt(blockMatcher.group(7)));
						}
						if (blockMatcher.groupCount() > 7 && blockMatcher.group(8) != null) {
							if (blockMatcher.group(7) == null) {
								eb.setLuggageNum(Integer.parseInt(blockMatcher.group(8)));
							} else {
								String[] colors = blockMatcher.group(8).split("\\s+");
								ArrayList<LuggageColor> lugCols = new ArrayList<>();
								for (String color : colors) {
									LuggageColor lugCol = LuggageColor.parseLuggageColor(color);
									if (lugCol != null) {
										lugCols.add(lugCol);
									}
								}
								eb.setLuggageColors(lugCols.size() == 0 ? null : lugCols);
							}
						}
					}
					if (b instanceof LeaveBlock && blockMatcher.groupCount() > 7 &&
							blockMatcher.group(8) != null) {
						String[] colors = blockMatcher.group(8).split("\\s+");
						ArrayList<LuggageColor> acceptCols = new ArrayList<>();
						for (String color : colors) {
							LuggageColor lugCol = LuggageColor.parseLuggageColor(color);
							if (lugCol != null) {
								acceptCols.add(lugCol);
							}
						}
						((LeaveBlock) b).setAcceptColors(acceptCols.size() == 0 ? null : acceptCols);
					}
				} else {
					throw new InputMismatchException("Line \"" + line + "\" does not properly describe a "
							+ "block. Expected format is \"[blockid] [x] [y] [z] [orientation] [nd]? "
							+ "[[[luggageNum]? [color] [color] ...]]?\".");
				}
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
	 * Construct a level from the current state of the program. Assumes that
	 * {@link State#world} is not {@code null}.
	 * 
	 * @param s State of the program. Used to access {@link World}.
	 * @throws NullPointerException In case {@code s.world == null}.
	 */
	public Level(State s) {
		this.levelNumber = s.levelNumber;
		this.levelName = s.levelName;
		this.blocks = new ArrayList<>();
		this.fieldLength = s.fieldLength;
		this.fieldWidth = s.fieldWidth;
		this.fieldHeight = s.fieldHeight;
		this.blockLimit = s.world.getBlockLimit();
		
		for (Block b : s.world.bc) {
			this.blocks.add(b);
		}
	}
	
	/**
	 * Returns the level hint; a short text that is displayed when the level is
	 * started. This can be <code>null</code> if no hint is to be displayed.
	 * @return The level hint.
	 */
	public String getLevelHint() {
		return levelHint;
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
	 * Load the Level in the given state. This means that the {@link World} and
	 * {@link Simulation} in the state are being modified.
	 * @param s State to load Level in.
	 * @throws IllegalStateException If the given State does not have a program
	 *             mode of {@link ProgramMode#NORMAL_MODE} or a mode of
	 *             {@link ProgramMode#SIMULATION_MODE} and the state has a value
	 *             of true for {@link State#loadedBuiltinLevel}.
	 */
	public void loadInState(State s) {
		if (s.programMode != ProgramMode.NORMAL_MODE &&
				!(s.programMode == ProgramMode.SIMULATION_MODE &&
					s.loadedBuiltinLevel)) {
			throw new IllegalStateException("A level can only be loaded when "
					+ "the program is in the \"normal mode\" or \"simulation mode\".");
		}
		
		s.fieldLength = fieldLength;
		s.fieldWidth = fieldWidth;
		s.fieldHeight = fieldHeight;
		
		s.levelName = getLevelName();
		s.levelNumber = levelNumber;
		
		s.simulation = new Simulation(s);
		s.world = new World(s);
		s.world.setBlockLimit(blockLimit);
		for (Block b : blocks) {
			s.world.addBlock(s, b);
		}
		
		if (getLevelHint() != null) {
			final Dialog levelHintDialog = new Dialog(getLevelName(), new Label(getLevelHint()));
			Button okButton = new Button("LevelHintDialog.ok", null, s.textures.iconOk);
			okButton.addListener(new Listener() {
				
				@Override
				public void event(Event e) {
					if (e instanceof MouseClickEvent) {
						levelHintDialog.setVisible(false);
					}
				}
			});
			levelHintDialog.addButton(okButton);
			s.gui.add(levelHintDialog);
		}
	}
	
	/**
	 * Change the limit on the number of blocks for this level.
	 * @param blockLimit The new block limit.
	 */
	public void setBlockLimit(int blockLimit) {
		this.blockLimit = blockLimit;
	}
	
	/**
	 * Changes the level hint; a short text that is displayed when the level is
	 * started.
	 * @param levelHint The new level hint. This can be <code>null</code> if no
	 * hint is to be displayed.
	 */
	public void setLevelHint(String levelHint) {
		this.levelHint = levelHint;
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
	
	/**
	 * Write this {@link Level} to a file, which can be read with for example
	 * {@link #Level(File)} or {@link #Level(InputStream)} again.
	 * @param output File to write to.
	 * @throws IOException If creating a {@link FileWriter} on the given file
	 *             does throw this exception.
	 */
	public void writeToFile(File output) throws IOException {
		// try-with-resources to close file
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(output)))) {
			pw.println("Luggage Tycoon level file.");
			pw.println("\"" + (levelName == null ? "" : levelName) + "\" " + levelNumber);
			pw.println(fieldLength + "x" + fieldWidth + "x" + fieldHeight);
			
			pw.println("blocks");
			for (Block b : blocks) {
				pw.print(b.getBlockID() + " " + b.getX() + " " + b.getY() + " " +
						b.getZ() + " " + b.getOrientation().getOrientationID());
				if (!b.isDeletable()) {
					pw.print(" nd");
				}
				
				if (b instanceof EnterBlock) {
					EnterBlock eb = (EnterBlock) b;
					if (eb.getLuggageNum() >= 0 || (eb.getLuggageColors() != null &&
							eb.getLuggageColors().size() > 0)) {
						pw.print(" [");
						if (eb.getLuggageNum() >= 0) {
							pw.print(eb.getLuggageNum());
						}
						
						if (eb.getLuggageColors() != null) {
							for (LuggageColor lc : eb.getLuggageColors()) {
								pw.print(" " + lc.name().toLowerCase());
							}
						}
						pw.print("]");
					}
				}
				
				if (b instanceof LeaveBlock) {
					LeaveBlock lb = (LeaveBlock) b;
					if (lb.getAcceptColors() != null && lb.getAcceptColors().size() > 0) {
						pw.print(" [");
						boolean first = true;
						for (LuggageColor lc : lb.getAcceptColors()) {
							pw.print((first ? "" : " ") + lc.name().toLowerCase());
							first = false;
						}
						pw.print("]");
					}
				}
				
				pw.println();
			}
			
			if (blockLimit >= 0) {
				pw.println("blocklimit " + blockLimit);
			}
		}
	}
}
