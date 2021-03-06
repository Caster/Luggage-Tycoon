package accg;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * This class contains the textures for the application.
 */
public class Textures {
	
	// icon textures
	public Texture iconExit;
	public Texture iconConfigure;
	public Texture iconOpen;
	public Texture iconSave;
	public Texture iconJustifyCenter;
	public Texture iconJustifyLeft;
	public Texture iconJustifyRight;
	public Texture iconGoDown;
	public Texture iconGoRight;
	public Texture iconGoLeft;
	public Texture iconGoUp;
	public Texture iconMouse;
	public Texture iconPause;
	public Texture iconStart;
	public Texture iconStop;
	public Texture iconZoomIn;
	public Texture iconZoomOut;
	public Texture iconOk;
	public Texture iconLeft;
	public Texture iconRight;
	public Texture iconBomb;
	public Texture iconLocale;
	
	// icon flag textures
	public Texture iconFlagNL;
	public Texture iconFlagUK;
	
	// belts
	public Texture iconBeltFlat;
	public Texture iconBeltAscending;
	public Texture iconBeltDescending;
	public Texture iconBeltLeft;
	public Texture iconBeltRight;
	
	// scene textures
	public Texture conveyor;
	public Texture floorBuildMode;
	public Texture floorSimulationMode;
	public Texture shutterEnter;
	public Texture shutterExit;
	public Texture wall;
	
	/**
	 * Creates a {@link Textures} object and reads in all of the textures.
	 */
	public Textures() {
		try {
			// icon textures
			iconExit = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/application-exit.png"));
			iconConfigure = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/configure.png"));
			iconOpen = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/document-open.png"));
			iconSave = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/document-save.png"));
			iconJustifyCenter = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/format-justify-center.png"));
			iconJustifyLeft = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/format-justify-left.png"));
			iconJustifyRight = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/format-justify-right.png"));
			iconGoDown = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/go-down.png"));
			iconGoRight = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/go-next.png"));
			iconGoLeft = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/go-previous.png"));
			iconGoUp = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/go-up.png"));
			iconMouse = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/input-mouse.png"));
			iconPause = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/media-playback-pause.png"));
			iconStart = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/media-playback-start.png"));
			iconStop = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/media-playback-stop.png"));
			iconZoomIn = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/zoom-in.png"));
			iconZoomOut = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/zoom-out.png"));
			iconOk = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/dialog-ok.png"));
			iconLeft = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/object-rotate-left.png"));
			iconRight = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/object-rotate-right.png"));
			iconBomb = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/edit-bomb.png"));
			iconLocale = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/flags/preferences-desktop-locale.png"));
			
			// icon flag textures
			iconFlagNL = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/flags/nl.png"));
			iconFlagUK = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/flags/uk.png"));
			
			// belts
			iconBeltFlat = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/belts/flat.png"));
			iconBeltAscending = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/belts/ascending.png"));
			iconBeltDescending = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/belts/descending.png"));
			iconBeltLeft = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/belts/left.png"));
			iconBeltRight = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/belts/right.png"));
			
			// scene textures
			conveyor = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/conveyor.png"));
			floorBuildMode = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/floorBuildMode.png"));
			floorSimulationMode = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/floorSimulationMode.png"));
			shutterEnter = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/shutterEnter.png"));
			shutterExit = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/shutterExit.png"));
			wall = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/wall.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
