package accg.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Properties;

import org.newdawn.slick.util.ResourceLoader;

import accg.ACCGProgram;
import accg.State;

/**
 * The SavedGameManager provides a static interface to the local filesystem for
 * storing and retrieving saved games. This includes bundled saved games that
 * store levels that the user can play.
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class SavedGameManager {

	/**
	 * A properties object that holds information about how many levels there
	 * are, in which file they are stored, et cetera.
	 */
	private static final Properties PROPERTIES;
	static {
		PROPERTIES = new Properties();
		try {
			PROPERTIES.load(ResourceLoader.getResourceAsStream(
						"res/levels/levels.properties"));
		} catch (IOException e) {
			throw new RuntimeException("Could not load internal level "
					+ "description file.");
		}
	}
	/**
	 * Directory where saved games are stored. This is platform dependent.
	 */
	private static final File SAVED_GAMES_DIR;
	static {
		String argDir = ACCGProgram.getArgSavedGamesDir();
		if (argDir == null) {
			String os = System.getProperty("os.name").toLowerCase();
			String home = System.getProperty("user.home");
			if (os.contains("linux")) {
				SAVED_GAMES_DIR = new File(home + "/.local/share/luggage-tycoon/"
						+ "saved-games/");
			} else if (os.contains("windows")) {
				SAVED_GAMES_DIR = new File(System.getenv("APPDATA") +
						"\\luggage-tycoon\\saved-games\\");
			} else if (os.contains("mac")) {
				SAVED_GAMES_DIR = new File(home + "/Library/Application Support/"
						+ "luggage-tycoon/saved-games/");
			} else {
				SAVED_GAMES_DIR = new File(home + "/.luggage-tycoon/saved-games/");
			}
			
			// ensure that the directory actually exists
			if (!(SAVED_GAMES_DIR.exists() || SAVED_GAMES_DIR.mkdirs())) {
				throw new RuntimeException("Could not create directory for storing "
						+ "saved games \"" + SAVED_GAMES_DIR.getAbsolutePath() + "\"."
						+ " Please ensure that the program can write to this "
						+ "location or start the program with the \"--saved-games-"
						+ "dir [dir]\" parameter.");
			}
		} else {
			if (argDir.startsWith("~")) {
				argDir = System.getProperty("user.home") + argDir.substring(1);
			}
			
			SAVED_GAMES_DIR = new File(argDir);
			if (!SAVED_GAMES_DIR.exists()) {
				throw new RuntimeException("The specified directory for saving "
						+ "games, \"" + SAVED_GAMES_DIR.getAbsolutePath() + "\", "
						+ "does not exist.");
			}
			try {
				File tmp = File.createTempFile(".empty-", ".lt", SAVED_GAMES_DIR);
				tmp.delete();
			} catch (Exception e) {
				throw new RuntimeException("The specified directory for saving "
						+ "games, \"" + SAVED_GAMES_DIR.getAbsolutePath() + "\", "
						+ "appears to not be writeable (no files can be created"
						+ " in that directory). Please check permissions.");
			}
		}
	}
	/**
	 * Filter using file extension, used to filter any files in the saved games
	 * directory. This is probably not needed, but we do this just in case some
	 * user decides to put random crap in the saved games folder, or there are
	 * some platform dependent weird standard files there.
	 */
	private static final FilenameFilter ltFilter = new FilenameFilter() {	
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".lt");
		}
	};
	
	/**
	 * Returns the level after the one with the given number, or {@code null}
	 * if that was the last level. The current level is read from the state.
	 * 
	 * @param s The state of the program, used to access the shared persistent
	 *          preferences object.
	 * @return Level after given one, or {@code null} if no such level exists.
	 * @throws FileNotFoundException If the name of the next game was not found.
	 *             This means that the code should be updated probably.
	 */
	public static Level getNextLevel(State s) throws FileNotFoundException {
		int maxUnlocked = s.prefs.getInt("level.unlocked", 0);
		if (s.levelNumber > maxUnlocked) {
			s.prefs.putInt("level.unlocked", s.levelNumber);
		}
		
		String[] unlockedLevels = getUnlockedLevels(s);
		if (unlockedLevels.length > s.levelNumber) {
			return loadLevelByName(unlockedLevels[s.levelNumber]);
		} else {
			return null;
		}
	}
	
	/**
	 * Return a list of saved games. This only includes games saved by the user,
	 * not bundled saved games.
	 * 
	 * @return A list of (unique) names of saved games.
	 */
	public static String[] getSavedGames() {
		String[] games = SAVED_GAMES_DIR.list(ltFilter);
		// filter away the '.lt' extension
		for (int i = 0; i < games.length; i++) {
			games[i] = games[i].substring(0, games[i].length() - 3);
		}
		Arrays.sort(games);
		return games;
	}

	/**
	 * Return an ordered list of all levels that are unlocked to play.
	 * 
	 * @param s The state of the program, used to access the shared persistent
	 *          preferences object.
	 * @return A list of (unique) names of levels that the user unlocked.
	 */
	public static String[] getUnlockedLevels(State s) {
		int maxUnlocked = s.prefs.getInt("level.unlocked", 0);
		int numLevels = Integer.parseInt(PROPERTIES.getProperty("levels.num"));
		int numUnlockedLevels = Math.min(numLevels, maxUnlocked + 1);
		String[] levels = new String[numUnlockedLevels];
		for (int i = 0; i < numUnlockedLevels; i++) {
			levels[i] = PROPERTIES.getProperty("level." + (i + 1) + ".name");
		}
		return levels;
	}
	
	/**
	 * Load a level and return it.
	 * 
	 * @param levelName Name of the level to load. This can be one of the names
	 *            returned by {@link #getUnlockedLevels(State)}.
	 * @return The Level loaded from the given name.
	 * @throws FileNotFoundException If the given saved game was not found.
	 * @throws InputMismatchException If the Level throws that exception.
	 */
	public static Level loadLevelByName(String levelName)
			throws FileNotFoundException {
		int numLevels = Integer.parseInt(PROPERTIES.getProperty("levels.num"));
		String loadLevel = null;
		for (int i = 0; i < numLevels; i++) {
			if (PROPERTIES.getProperty("level." + (i + 1) + ".name").
					equals(levelName)) {
				loadLevel = PROPERTIES.getProperty("level." + (i + 1) + ".file");
				break;
			}
		}
		if (loadLevel == null) {
			throw new FileNotFoundException("Level \"" + levelName + "\" could "
					+ "not be found.");
		}
		return new Level(ResourceLoader.getResourceAsStream("/res/levels/" +
				loadLevel));
	}
	
	/**
	 * Load a saved game and return it.
	 * 
	 * @param gameName Name of the game to load. This can be one of the names
	 *            returned by {@link #getSavedGames()}.
	 * @return The Level loaded from the given game.
	 * @throws FileNotFoundException If the given saved game was not found.
	 * @throws InputMismatchException If the Level throws that exception.
	 * 
	 */
	public static Level loadSavedGame(String gameName)
			throws FileNotFoundException {
		return new Level(new File(SAVED_GAMES_DIR, gameName + ".lt"));
	}
	
	/**
	 * Remove the given saved game from disk.
	 * 
	 * @param gameName Name of the game to load. This can be one of the names
	 *            returned by {@link #getSavedGames()}.
	 * @throws FileNotFoundException If the given saved game was not found.
	 */
	public static void removeSavedGame(String gameName) throws FileNotFoundException {
		File toRemove = new File(SAVED_GAMES_DIR, gameName + ".lt");
		
		if (!toRemove.exists()) {
			throw new FileNotFoundException("File \"" + gameName + ".lt\" could not "
					+ "be found in directory \"" + SAVED_GAMES_DIR.getAbsolutePath()
					+ "\".");
		}
		
		toRemove.delete();
	}
	
	/**
	 * Write a given game to disk.
	 * 
	 * @param name Name of the file for the game. It will actually be stored on
	 *             disk as "name.lt", so with the extension added to it.
	 *             When retrieving the game using for example
	 *             {@link #getSavedGames()} you will get just "name" back again.
	 * @param game The game to be saved to disk.
	 * @throws IOException In case the file could not be opened for writing.
	 */
	public static void saveGame(String name, Level game) throws IOException {
		game.writeToFile(new File(SAVED_GAMES_DIR, name + ".lt"));
	}
}
