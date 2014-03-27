package accg;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.util.Log;

public class TexturesTest {
	
	private static Textures t;
	
	@BeforeClass
	public static void setUp() {
		try {
			// suppress output
			System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
			
			// create display
			Display.setDisplayMode(new DisplayMode(1, 1));
			Display.create();
			
			// initialise test object
			Log.setVerbose(false);
			t = new Textures();
		} catch (LWJGLException e) {
			fail("Could not initialise LWJGL/OpenGL context.");
		}
	}
	
	@AfterClass
	public static void tearDown() {
		Display.destroy();
	}
	
	@Test
	public void testTextureLoading() {
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
		assertNotNull("iconOk texture is not loaded", t.iconOk);
		assertNotNull("conveyor texture is not loaded", t.conveyor);
		assertNotNull("floorBuildMode texture is not loaded", t.floorBuildMode);
		assertNotNull("floorSimulationMode texture is not loaded", t.floorSimulationMode);
		assertNotNull("wall texture is not loaded", t.wall);
	}
}
