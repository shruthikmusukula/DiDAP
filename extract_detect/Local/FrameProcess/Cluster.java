package FrameProcess;

import DataStorage.*;
import GUI.Window;

/**
 * K-means Clustering
 *
 */
public class Cluster {
	/** LightPoint array for 3D clustering */
	public static LightPoint[][] tmap;
	/** LightPoint array for 2D clustering */
	public static Centroid[] cens;

	/** value of the luminosity scale in 3D clustering */
	static double weight;
	public static int cenN;

	/**
	 * initializes variables
	 * 
	 * @param tmapSet
	 * @param weightSet
	 * @param cenNSet
	 */
	public static void init(LightPoint[][] tmapSet, double weightSet, int cenNSet) {

		tmap = tmapSet;
		weight = weightSet * ((tmap.length + tmap[0].length) / 2);
		cenN = cenNSet;

		cens = new Centroid[cenN];

		for (int i = 0; i < cens.length; i++) {

			int r = (int) (tmap.length * Math.random());
			int c = (int) (tmap[0].length * Math.random());
			int lum = (int) Math.random();

			cens[i] = new Centroid(r, c, lum, i);
		}
	}

	/**
	 * overall master method
	 * 
	 * @param file
	 * @return map
	 */
	public static LightPoint[][] process() {

		Centroid[] prev = new Centroid[cens.length];

		int m = 0;
		do {
			for (int i = 0; i < cens.length; i++)
				prev[i] = new Centroid(cens[i].r, cens[i].c, cens[i].lum, i);
			assignPoints();
			updateCens();
		} while (checkCens(prev) && m++ < 500);

		return tmap;
	}

	/**
	 * checks if Centroids have moved
	 * 
	 * @param prev
	 * @return whether centroids moved
	 */
	public static boolean checkCens(Centroid[] prev) {

		for (int i = 0; i < prev.length; i++)
			if (prev[i].r != cens[i].r || prev[i].c != cens[i].c || prev[i].lum != cens[i].lum)
				return true;

		return false;
	}

	/**
	 * assigns each LightPoint the Centroid closest to them
	 */
	public static void assignPoints() {

		for (int r = 0; r < tmap.length; r++) {
			for (int c = 0; c < tmap[0].length; c++) {
				LightPoint p = tmap[r][c];
				p.cen = closestCen(p);
				p.tag = p.cen.tag;
			}
		}
	}

	/**
	 * moves Centroids to the average position of all their associated LightPoints
	 */
	public static void updateCens() {

		for (Centroid cen : cens) {
			cen.r = (int) Math.round(avg(cen, 0));
			cen.c = (int) Math.round(avg(cen, 1));
			cen.lum = avg(cen, 2);
		}
	}

	/**
	 * calculates the closest Centroid to a given LightPoint
	 * 
	 * @param p
	 * @return closest centroid
	 */
	public static Centroid closestCen(LightPoint p) {

		Centroid minCen = cens[0];

		for (Centroid cen : cens)
			minCen = dist(p, cen) < dist(p, minCen) ? cen : minCen;

		return minCen;
	}

	/**
	 * finds the distance between a given LightPoint and Centroid
	 * 
	 * @param p
	 * @param cen
	 * @return distance
	 */
	public static double dist(LightPoint p, Centroid cen) {

		int x = Math.abs(p.c - cen.c);
		int y = Math.abs(p.r - cen.r);
		double z = weight * Math.abs(p.lum - cen.lum);

		return Function.Calc.dist(x, y, z);
	}

	/**
	 * finds the average of a given variable type
	 * 
	 * @param cen
	 * @param type
	 * @return average
	 */
	public static double avg(Centroid cen, int type) {

		double sum = 0;
		int count = 0;

		for (LightPoint[] row : tmap) {
			for (LightPoint p : row) {
				if (p.cen.tag == cen.tag && p.lum != -1) {
					count++;
					if (type == 0) {
						sum += p.r;
					} else if (type == 1) {
						sum += p.c;
					} else {
						sum += p.lum;
					}
				}
			}
		}

		// don't forget about /0 errors
		return sum / count;
	}
}
