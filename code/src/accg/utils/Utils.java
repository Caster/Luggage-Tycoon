package accg.utils;

import java.util.AbstractList;
import java.util.ArrayList;

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
	 * Perform Bresenham's algorithm in 3D and return the ordered list of
	 * interesting cells from start to end.
	 * 
	 * @param start Position to start.
	 * @param end Position to end.
	 * @return
	 */
	public static ArrayList<Vector3f> bresenham3D(Vector3f start, Vector3f end) {
		ArrayList<Vector3f> result = new ArrayList<>();
		
		// http://www.ict.griffith.edu.au/anthony/info/graphics/bresenham.procs
		Vector3f pixel = new Vector3f((float) Math.floor(start.x),
				(float) Math.floor(start.y), (float) Math.floor(start.z));
		int dx = (int) (Math.floor(end.x) - Math.floor(start.x));
		int dy = (int) (Math.floor(end.y) - Math.floor(start.y));
		int dz = (int) (Math.floor(end.z) - Math.floor(start.z));
		int xInc = (dx < 0 ? -1 : 1);
		int l = Math.abs(dx);
		int yInc = (dy < 0 ? -1 : 1);
		int m = Math.abs(dy);
		int zInc = (dz < 0 ? -1 : 1);
		int n = Math.abs(dz);
		int dx2 = l << 1;
		int dy2 = m << 1;
		int dz2 = n << 1;
		
		if (l >= m && l >= n) {
			int err1 = dy2 - l;
			int err2 = dz2 - l;
			for (int i = 0; i < l; i++) {
				result.add(new Vector3f(pixel));
				if (err1 > 0) {
					pixel.y += yInc;
					err1 -= dx2;
				} else if (err2 > 0) {
					pixel.z += zInc;
					err2 -= dx2;
				}
				err1 += dy2;
				err2 += dz2;
				pixel.x += xInc;
			}
		} else if (m >= l && m >= n) {
			int err1 = dx2 - m;
			int err2 = dz2 - m;
			for (int i = 0; i < m; i++) {
				result.add(new Vector3f(pixel));
				if (err1 > 0) {
					pixel.x += xInc;
					err1 -= dy2;
				} else if (err2 > 0) {
					pixel.z += zInc;
					err2 -= dy2;
				}
				err1 += dx2;
				err2 += dz2;
				pixel.y += yInc;
			}
		} else {
			int err1 = dy2 - n;
			int err2 = dx2 - n;
			for (int i = 0; i < n; i++) {
				result.add(new Vector3f(pixel));
				if (err1 > 0) {
					pixel.y += yInc;
					err1 -= dz2;
				} else if (err2 > 0) {
					pixel.x += xInc;
					err2 -= dz2;
				}
				err1 += dy2;
				err2 += dx2;
				pixel.z += zInc;
			}
		}
		result.add(pixel);
		
		return result;
	}
	
	/**
	 * Computes the range for <code>t</code> such that
	 * <code>
	 * start + t * direction
	 * </code>
	 * lies inside the box given by the other six parameters.
	 * 
	 * <p>Note: this function has been taken from the code we wrote for the
	 * VolVis project of the 2IV35 Visualization course.
	 * 
	 * @param start The start vector for the line.
	 * @param direction The direction vector for the line.
	 * @param xMin Lower x coordinate for the box.
	 * @param xMax Upper x coordinate for the box.
	 * @param yMin Lower y coordinate for the box.
	 * @param yMax Upper y coordinate for the box.
	 * @param zMin Lower z coordinate for the box.
	 * @param zMax Upper z coordinate for the box.
	 * @return A 2-array containing the begin and end values for
	 *         <code>t</code>.
	 */
	public static double[] getIntersectWithBox(Vector3f start,
			Vector3f direction, double xMin, double xMax, double yMin,
			double yMax, double zMin, double zMax) {
		// check for a bad direction vector
		if (direction.x == 0 && direction.y == 0 && direction.z == 0) {
			return null;
		}
		
		// calculate boundaries per axis for t
		double[] xCalc = calculateXAxisBoundary(start, direction, xMin, xMax);
		double[] yCalc = calculateYAxisBoundary(start, direction, yMin, yMax);
		double[] zCalc = calculateZAxisBoundary(start, direction, zMin, zMax);
		
		// maybe flip variables if needed
		double tmp;
		if (xCalc[0] > xCalc[1]) {
			tmp = xCalc[0];
			xCalc[0] = xCalc[1];
			xCalc[1] = tmp;
		}
		if (yCalc[0] > yCalc[1]) {
			tmp = yCalc[0];
			yCalc[0] = yCalc[1];
			yCalc[1] = tmp;
		}
		if (zCalc[0] > zCalc[1]) {
			tmp = zCalc[0];
			zCalc[0] = zCalc[1];
			zCalc[1] = tmp;
		}
		
		// determine value for tMin: maximum of minimum values
		double tMin = Double.NEGATIVE_INFINITY;
		if (direction.x != 0) {
			tMin = Math.max(tMin, xCalc[0]);
		}
		if (direction.y != 0) {
			tMin = Math.max(tMin, yCalc[0]);
		}
		if (direction.z != 0) {
			tMin = Math.max(tMin, zCalc[0]);
		}

		// determine value for tMax: minimum of maximum values
		double tMax = Double.POSITIVE_INFINITY;
		if (direction.x != 0) {
			tMax = Math.min(tMax, xCalc[1]);
		}
		if (direction.y != 0) {
			tMax = Math.min(tMax, yCalc[1]);
		}
		if (direction.z != 0) {
			tMax = Math.min(tMax, zCalc[1]);
		}
		
		// an invalid interval means no interval
		if (tMin > tMax) {
			return null;
		}
		
		return new double[] {tMin, tMax};
	}
	
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
	
	private static final double[] calculateXAxisBoundary(Vector3f start,
			Vector3f direction, double xMin, double xMax) {
		if (direction.x == 0) {
			if (xMin <= start.x && xMax >= start.x) {
				return new double[] {Double.NEGATIVE_INFINITY,
						Double.POSITIVE_INFINITY};
			}
			return new double[] {0, 0};
		}
		return new double[] {
				(xMin - start.x) / direction.x,
				(xMax - start.x) / direction.x
		};
	}
	
	private static final double[] calculateYAxisBoundary(Vector3f start,
			Vector3f direction, double yMin, double yMax) {
		if (direction.y == 0) {
			if (yMin <= start.y && yMax >= start.y) {
				return new double[] {Double.NEGATIVE_INFINITY,
						Double.POSITIVE_INFINITY};
			}
			return new double[] {0, 0};
		}
		return new double[] {
				(yMin - start.y) / direction.y,
				(yMax - start.y) / direction.y
		};
	}
	
	private static final double[] calculateZAxisBoundary(Vector3f start,
			Vector3f direction, double zMin, double zMax) {
		if (direction.z == 0) {
			if (zMin <= start.z && zMax >= start.z) {
				return new double[] {Double.NEGATIVE_INFINITY,
						Double.POSITIVE_INFINITY};
			}
			return new double[] {0, 0};
		}
		return new double[] {
				(zMin - start.z) / direction.z,
				(zMax - start.z) / direction.z
		};
	}
}
