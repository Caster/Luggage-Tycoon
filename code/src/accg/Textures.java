package accg;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Textures {
	
	// icon textures
	public Texture iconExit;
	public Texture iconConfigure;
	public Texture iconOpen;
	public Texture iconSave;
	public Texture iconPause;
	public Texture iconStart;
	public Texture iconStop;
	
	// scene textures
	public Texture conveyor;
	public Texture floorBuildMode;
	public Texture floorSimulationMode;
	
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
			iconPause = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/media-playback-pause.png"));
			iconStart = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/media-playback-start.png"));
			iconStop = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/icons/media-playback-stop.png"));
			
			// scene textures
			conveyor = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/conveyor.png"));
			floorBuildMode = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/floorBuildMode.png"));
			floorSimulationMode = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/floorSimulationMode.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
