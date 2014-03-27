package accg.io;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.ArrayList;

import org.junit.Test;
import org.newdawn.slick.util.ResourceLoader;

import accg.objects.Block;
import accg.objects.Luggage.LuggageColor;
import accg.objects.blocks.EnterBlock;

/**
 * Tests for {@link Level}.
 */
public class LevelTest {

	@Test
	public void testReadLevel1() {
		try {
			InputStream level1Stream = ResourceLoader.getResourceAsStream(
					"res/levels/level-1.lt");
			Level level1 = new Level(level1Stream);
			String expLevel1Name = "Level 1: the basics";
			assertEquals("Expected level name to be \"" + expLevel1Name + "\".",
					expLevel1Name, level1.getLevelName());
			assertEquals("Expected length of level to be 9.", 9, level1.fieldLength);
			assertEquals("Expected width of level to be 9.", 9, level1.fieldWidth);
			assertEquals("Expected height of level to be 4.", 4, level1.fieldHeight);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testReadLevel2() {
		try {
			InputStream level2Stream = ResourceLoader.getResourceAsStream(
					"res/levels/level-2.lt");
			Level level2 = new Level(level2Stream);
			String expLevel2Name = "Level 2: crossing";
			assertEquals("Expected level name to be \"" + expLevel2Name + "\".",
					expLevel2Name, level2.getLevelName());
			assertEquals("Expected length of level to be 9.", 9, level2.fieldLength);
			assertEquals("Expected width of level to be 9.", 9, level2.fieldWidth);
			assertEquals("Expected height of level to be 4.", 4, level2.fieldHeight);
			
			Block b = level2.blocks.get(0);
			assertTrue(b instanceof EnterBlock);
			EnterBlock eb = (EnterBlock) b;
			assertEquals(9, eb.getLuggageNum());
			ArrayList<LuggageColor> ebColors = eb.getLuggageColors();
			assertEquals(2, ebColors.size());
			assertEquals(LuggageColor.RED, ebColors.get(0));
			assertEquals(LuggageColor.GREEN, ebColors.get(1));
			
			b = level2.blocks.get(1);
			assertTrue(b instanceof EnterBlock);
			eb = (EnterBlock) b;
			assertEquals(11, eb.getLuggageNum());
			ebColors = eb.getLuggageColors();
			assertEquals(2, ebColors.size());
			assertEquals(LuggageColor.BLUE, ebColors.get(0));
			assertEquals(LuggageColor.ORANGE, ebColors.get(1));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
