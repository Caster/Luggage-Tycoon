package accg;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Textures {
	
	public Texture conveyor;
	
	public Textures() {
		try {
			conveyor = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/conveyor.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
