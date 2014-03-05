package accg.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import accg.State;

/**
 * Tests for {@link Utils}.
 */
public class UtilsTest {

	@Test
	public void testHasTimePassed() {
		State s = new State();
		s.time = 42;
		s.prevTime = 38;

		assertFalse("time > mod: expected to return false",
				Utils.hasTimePassed(s, 2, 3));
		assertTrue("time == mod: expected to return true in this case",
				Utils.hasTimePassed(s, 2, 2));
		assertFalse("time < 0: expected to return false",
				Utils.hasTimePassed(s, 2, -1));
		
		assertTrue("Between 38 and 42, the time was 0 % 10 at some point. "
				+ "(Namely at 40.)", Utils.hasTimePassed(s, 10, 0));
		assertTrue("Between 38 and 42, the time was 1 % 10 at some point. "
				+ "(Namely at 41.)", Utils.hasTimePassed(s, 10, 1));
		assertTrue("Between 38 and 42, the time was 2 % 10 at some point. "
				+ "(Namely at 42.)", Utils.hasTimePassed(s, 10, 2));
		assertFalse("Between 38 and 42, the time was at no point 3 % 10.",
				Utils.hasTimePassed(s, 10, 3));
		assertFalse("Between 38 and 42, the time was at no point 4 % 10.",
				Utils.hasTimePassed(s, 10, 4));
		assertFalse("Between 38 and 42, the time was at no point 5 % 10.",
				Utils.hasTimePassed(s, 10, 5));
		assertFalse("Between 38 and 42, the time was at no point 6 % 10.",
				Utils.hasTimePassed(s, 10, 6));
		assertFalse("Between 38 and 42, the time was at no point 7 % 10.",
				Utils.hasTimePassed(s, 10, 7));
		assertFalse("Between 38 and 42, the time was at no point 8 % 10.",
				Utils.hasTimePassed(s, 10, 8));
		assertTrue("Between 38 and 42, the time was 9 % 10 at some point. "
				+ "(Namely at 39.)", Utils.hasTimePassed(s, 10, 9));
		assertTrue("Between 38 and 42, the time was 10 % 10 at some point. "
				+ "(Namely at 40, because 10 % 10 == 0 % 10, of course.)",
				Utils.hasTimePassed(s, 10, 10));
		
		assertTrue("Between 38 and 42, the time was 9.5 % 10 at some point. "
				+ "(Namely at 39.5.)", Utils.hasTimePassed(s, 10, 9.5));
		assertTrue("Between 38 and 42, the time was 8.001 % 10 at some point. "
				+ "(Namely at 38.001.)", Utils.hasTimePassed(s, 10, 8.001));
	}
}
