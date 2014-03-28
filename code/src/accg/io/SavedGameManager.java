package accg.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.InputMismatchException;

import accg.ACCGProgram;

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
	 * Directory where saved games are stored. This is platform dependent.
	 */
	private static final File savedGamesDir;
	static {
		String argDir = ACCGProgram.getArgSavedGamesDir();
		if (argDir == null) {
			String os = System.getProperty("os.name").toLowerCase();
			String home = System.getProperty("user.home");
			if (os.contains("linux")) {
				savedGamesDir = new File(home + "/.local/share/luggage-tycoon/"
						+ "saved-games/");
			} else if (os.contains("windows")) {
				savedGamesDir = new File(System.getenv("APPDATA") +
						"\\luggage-tycoon\\saved-games\\");
			} else if (os.contains("mac")) {
				savedGamesDir = new File(home + "/Library/Application Support/"
						+ "luggage-tycoon/saved-games/");
			} else {
				savedGamesDir = new File(home + "/.luggage-tycoon/saved-games/");
			}
			
			// ensure that the directory actually exists
			if (!(savedGamesDir.exists() || savedGamesDir.mkdirs())) {
				throw new RuntimeException("Could not create directory for storing "
						+ "saved games \"" + savedGamesDir.getAbsolutePath() + "\"."
						+ " Please ensure that the program can write to this "
						+ "location or start the program with the \"--saved-games-"
						+ "dir [dir]\" parameter.");
			}
		} else {
			if (argDir.startsWith("~")) {
				argDir = System.getProperty("user.home") + argDir.substring(1);
			}
			
			savedGamesDir = new File(argDir);
			if (!savedGamesDir.exists()) {
				throw new RuntimeException("The specified directory for saving "
						+ "games, \"" + savedGamesDir.getAbsolutePath() + "\", "
						+ "does not exist.");
			}
			try {
				File tmp = File.createTempFile(".empty-", ".lt", savedGamesDir);
				tmp.delete();
			} catch (Exception e) {
				throw new RuntimeException("The specified directory for saving "
						+ "games, \"" + savedGamesDir.getAbsolutePath() + "\", "
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
	 * Return a list of saved games. This only includes games saved by the user,
	 * not bundled saved games.
	 * 
	 * @return A list of (unique) names of saved games.
	 */
	public static String[] getSavedGames() {
		String[] games = savedGamesDir.list(ltFilter);
		// filter away the '.lt' extension
		for (int i = 0; i < games.length; i++) {
			games[i] = games[i].substring(0, games[i].length() - 3);
		}
		return games;
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
	public static Level loadSavedGame(String gameName) throws FileNotFoundException {
		return new Level(new File(savedGamesDir, gameName + ".lt"));
	}
}
