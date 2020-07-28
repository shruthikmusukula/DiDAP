package Function;

import DataStorage.*;

/**
 * Calculation Functions
 *
 */
public class Calc {

	/**
	 * Counts number of points within radius with luminosity != -1
	 * 
	 * @param tmap
	 * @param p
	 *            center point
	 * @param range
	 * @return number of points
	 */
	public static int adjCount(LightPoint[][] tmap, LightPoint p, double range) {

		int adj = 0;

		Radius.init(tmap, p, range);

		do {
			LightPoint act = tmap[Radius.r][Radius.c];
			adj += act.lum != -1 && dist(p, act) <= range ? 1 : 0;
		} while (Radius.process());

		return adj;
	}

	/**
	 * Counts number of points within radius with luminosity != -1
	 * 
	 * @param tmap
	 * @param p
	 *            center point
	 * @param range
	 * @return number of points
	 */
	public static double adjValue(LightPoint[][] tmap, LightPoint p, double range) {

		double adj = 0;

		Radius.init(tmap, p, range);

		do {
			LightPoint act = tmap[Radius.r][Radius.c];
			adj += act.lum != -1 && dist(p, act) <= range ? act.lum : 0;
		} while (Radius.process());

		return adj;
	}

	/**
	 * Checks if any values within arr are true
	 * @param arr
	 * @return
	 */
	public static boolean containsTrue(boolean[][] arr) {
		for (boolean[] row : arr)
			for (boolean b : row)
				if (b)
					return true;

		return false;
	}

	// Distance functions

	public static double dist(double x, double y, double z) {

		return Math.sqrt(x * x + y * y + z * z);
	}

	public static double dist(double x, double y) {

		return Math.sqrt(x * x + y * y);
	}

	/**
	 * Distance
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return distance between points (x1, y1) and (x2, y2)
	 */
	public static double dist(double x1, double y1, double x2, double y2) {

		double x = Math.abs(x1 - x2);
		double y = Math.abs(y1 - y2);

		return dist(x, y);
	}

	public static double dist(Point p1, Point p2) {

		return dist(p1.r, p1.c, p2.r, p2.c);
	}

	/**
	 * Limit a value between 0 and limit
	 * 
	 * @param limit
	 *            limit such that edge < limit
	 * @param edge
	 * @return edge, unless outside range [0, limit)
	 */
	public static int edgeCut(int limit, int edge) {

		edge = edge < 0 ? 0 : edge;
		edge = edge > limit - 1 ? limit - 1 : edge;

		return edge;
	}

	/**
	 * get hyperbolic curve, input 0 - 1
	 * 
	 * @param n
	 * @param weight
	 * @return 0 - 1 (1/x curve)
	 */
	public static double curve(double n, double weight) {

		double h = (1 + Math.sqrt(1 + 4 * weight)) / 2;
		double k = weight / h;

		double func = weight / (h - n) - k;

		return func;
	}
}
