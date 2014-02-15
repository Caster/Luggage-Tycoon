package accg;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Textures {
	
	public Texture conveyor;
	public Texture floorBuildMode;
	public Texture floorSimulationMode;
	
	public Textures() {
		try {
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
