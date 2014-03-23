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
	
	// scene textures
	public Texture conveyor;
	public Texture floorBuildMode;
	public Texture floorSimulationMode;
	public Texture shutter;
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
			
			// scene textures
			conveyor = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/conveyor.png"));
			floorBuildMode = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/floorBuildMode.png"));
			floorSimulationMode = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/floorSimulationMode.png"));
			shutter = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/shutter.png"));
			wall = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/wall.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
