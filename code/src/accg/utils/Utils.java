package accg.utils;

import accg.State;

/**
 * This class contains several static utility methods for general purposes.
 */
public class Utils {
	
	/**
	 * Returns if the time has passed a certain value. This works by considering
	 * the time modulo {@code mod} and then seeing if between the last and the
	 * current frame, the time must have been {@code time} at some point.
	 * 
	 * @param s State, used to retrieve current and previous time from.
	 * @param mod Fragment of time to consider.
	 * @param time Actual time in seconds to check for. Should lie between
	 *             0 and {@code mod} to make sense (otherwise, {@code false} is
	 *             guaranteed to be the outcome.
	 * @return If between {@code s.prevTime % mod} and {@code s.time % mod}, the
	 *         value {@code time} lies or not. This will include s.time, but will
	 *         not include s.prevTime.
	 */
	public static boolean hasTimePassed(State s, double mod, double time) {
		double prev = s.prevTime % mod;
		double curr = s.time % mod;
		
		// check if time has passed the modulo border
		if (prev < curr) {
			return (prev < time && time <= curr);
		}
		// if it has, check if time is either between prev and mod or between
		// 0 and curr (with prev exclusive, curr inclusive)
		return ((prev < time && time <= mod) || (0 <= time && time <= curr));
	}
	
}
