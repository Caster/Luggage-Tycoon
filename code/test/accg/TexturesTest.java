package accg;

import static org.junit.Assert.*;

import org.junit.Test;
import org.newdawn.slick.opengl.Texture;

public class TexturesTest {

	@Test
	public void testTextureLoading() {
		Textures t = new Textures();
		assertNotNull("iconExit texture is not loaded", t.iconExit);
		assertNotNull("iconConfigure texture is not loaded", t.iconConfigure);
		assertNotNull("iconOpen texture is not loaded", t.iconOpen);
		assertNotNull("iconSave texture is not loaded", t.iconSave);
		assertNotNull("iconJustifyCenter texture is not loaded", t.iconJustifyCenter);
		assertNotNull("iconJustifyLeft texture is not loaded", t.iconJustifyLeft);
		assertNotNull("iconJustifyRight texture is not loaded", t.iconJustifyRight);
		assertNotNull("iconGoDown texture is not loaded", t.iconGoDown);
		assertNotNull("iconGoRight texture is not loaded", t.iconGoRight);
		assertNotNull("iconGoLeft texture is not loaded", t.iconGoLeft);
		assertNotNull("iconGoUp texture is not loaded", t.iconGoUp);
		assertNotNull("iconMouse texture is not loaded", t.iconMouse);
		assertNotNull("iconPause texture is not loaded", t.iconPause);
		assertNotNull("iconStart texture is not loaded", t.iconStart);
		assertNotNull("iconStop texture is not loaded", t.iconStop);
		assertNotNull("iconZoomIn texture is not loaded", t.iconZoomIn);
		assertNotNull("iconZoomOut texture is not loaded", t.iconZoomOut);
		assertNotNull("conveyor texture is not loaded", t.conveyor);
		assertNotNull("floorBuildMode texture is not loaded", t.floorBuildMode);
		assertNotNull("floorSimulationMode texture is not loaded", t.floorSimulationMode);
		assertNotNull("wall texture is not loaded", t.wall);
	}
}
