package accg.utils;

import java.util.AbstractList;

import javax.vecmath.Vector3f;

import accg.State;
import accg.objects.Block.Orientation;

/**
 * This class contains several static utility methods for general purposes.
 */
public class Utils {
	
	/** Epsilon that can be used in comparisons. */
	public static final float EPSILON = 0.0001f;
	
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
	
	/**
	 * Assuming points centered around the origin in a 1x1 tile and in the UP
	 * orientation, rotate them to match the given orientation. For example,
	 * when LEFT is given, all points are rotated 90 degrees counter-clockwise
	 * around the origin.
	 * 
	 * @param orientation Wanted orientation.
	 * @param points List of points to adapt.
	 */
	public static void rotatePoints(Orientation orientation,
			AbstractList<Vector3f> points) {
		switch (orientation) {
		default :
			return;
		case LEFT :
			for (Vector3f p : points) {
				float px = p.x;
				p.x = -p.y;
				p.y = px;
			}
			break;
		case RIGHT :
			for (Vector3f p : points) {
				float px = p.x;
				p.x = p.y;
				p.y = -px;
			}
			break;
		case DOWN :
			for (Vector3f p : points) {
				p.x = -p.x;
				p.y = -p.y;
			}
			break;
		}
	}
}
