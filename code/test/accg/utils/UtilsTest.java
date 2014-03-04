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

		assertFalse("time > mod: expected to return false", Utils.hasTimePassed(s, 1, 2));
		
		// TODO: check those test cases to see whether they are valid
		// (I don't understand the documentation completely)
		assertTrue("38 % 10 < 0 <= 42 % 10: expected to return true", Utils.hasTimePassed(s, 10, 0));
		assertTrue("38 % 10 < 1 <= 42 % 10: expected to return true", Utils.hasTimePassed(s, 10, 1));
		assertTrue("38 % 10 < 2 <= 42 % 10: expected to return true", Utils.hasTimePassed(s, 10, 2));
		assertFalse("38 % 10 < 3 <= 42 % 10: expected to return false", Utils.hasTimePassed(s, 10, 3));
		assertFalse("38 % 10 < 4 <= 42 % 10: expected to return false", Utils.hasTimePassed(s, 10, 4));
		assertFalse("38 % 10 < 5 <= 42 % 10: expected to return false", Utils.hasTimePassed(s, 10, 5));
		assertFalse("38 % 10 < 6 <= 42 % 10: expected to return false", Utils.hasTimePassed(s, 10, 6));
		assertFalse("38 % 10 < 7 <= 42 % 10: expected to return false", Utils.hasTimePassed(s, 10, 7));
		assertFalse("38 % 10 < 8 <= 42 % 10: expected to return false", Utils.hasTimePassed(s, 10, 8));
		assertTrue("38 % 10 < 9 <= 42 % 10: expected to return true", Utils.hasTimePassed(s, 10, 9));
	}
}
