/**
 * Removes noise from Map
 *
 */
public class Polish {

	public static LightPoint[][] rmap;
	static LightPoint[][] hold;

	public static void init(LightPoint[][] rmapSet) {

		rmap = rmapSet;
	}

	/**
	 * Removes noise
	 * 
	 * @param concen
	 *            number of pixels needed to return true
	 * @param range
	 *            pixels must be within radius to return true
	 * @return map
	 */
	public static LightPoint[][] cut(int concen, double range) {

		hold = new LightPoint[rmap.length][rmap[0].length];

		for (int r = 0; r < rmap.length; r++)
			for (int c = 0; c < rmap[0].length; c++)
				hold[r][c] = rmap[r][c];

		for (int r = 0; r < rmap.length; r++)
			for (int c = 0; c < rmap[0].length; c++)
				rmap[r][c] = new LightPoint(r, c, hold[r][c].blink && adjCount(rmap[r][c], range) >= concen ? 1 : -1);

		return rmap;
	}

	/**
	 * Find all points in range
	 * 
	 * @param p
	 * @param range
	 * @return number of points in range of p
	 */
	public static int adjCount(LightPoint p, double range) {

		int adj = -1;

		int startI = p.r - range < 0 ? 0 : (int) (p.r - range);
		int endI = p.r + range >= hold.length ? hold.length - 1 : (int) (p.r + range);
		int startJ = p.c - range < 0 ? 0 : (int) (p.c - range);
		int endJ = p.c + range >= hold[0].length ? hold[0].length - 1 : (int) (p.c + range);

		for (int i = startI; i <= endI; i++)
			for (int j = startJ; j <= endJ; j++)
				adj += hold[i][j].blink && Calc.dist(p, hold[i][j]) <= range ? 1 : 0;

		return adj;
	}
}
