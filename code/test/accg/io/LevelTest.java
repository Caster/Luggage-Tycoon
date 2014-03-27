package accg.io;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;
import org.newdawn.slick.util.ResourceLoader;

/**
 * Tests for {@link Level}.
 */
public class LevelTest {

	@Test
	public void testReadFile() {
		try {
			InputStream level1Stream = ResourceLoader.getResourceAsStream(
					"res/levels/level-1.lt");
			Level level1 = new Level(level1Stream);
			String expLevel1Name = "Level 1: the basics";
			assertEquals("Expected level name to be \"" + expLevel1Name + "\".",
					expLevel1Name, level1.getLevelName());
			assertEquals("Expected number of luggage items to be 10.", 10, level1.levelLuggageNum);
			assertEquals("Expected list of luggage colors to be null.", null, level1.levelLuggage);
			assertEquals("Expected length of level to be 9.", 9, level1.fieldLength);
			assertEquals("Expected width of level to be 9.", 9, level1.fieldWidth);
			assertEquals("Expected height of level to be 4.", 4, level1.fieldHeight);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
