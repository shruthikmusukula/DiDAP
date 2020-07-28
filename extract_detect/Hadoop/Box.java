/**
 * Create box around mine
 *
 */
public class Box {

	public static boolean[][] bmap;

	/** TL, TR, BL, BR */
	static Point[] corners;
	/** T, B, L, R */
	static int[] sides;
	/** box adjust size */
	static int exp;

	/**
	 * Initalize
	 * 
	 * @param bmapSet
	 *            set boolean map
	 * @param expSet
	 *            set box adjust size
	 */
	public static void init(boolean[][] bmapSet, int expSet) {

		bmap = bmapSet;
		exp = expSet;

		sides = new int[4];

		for (int i = 0; i < sides.length; i++)
			sides[i] = findSide(i);
	}

	/**
	 * Map box
	 * 
	 * @return boolean map
	 */
	public static boolean[][] process() {

		if (!Calc.containsTrue(bmap))
			return bmap;

		for (int r = 0; r < bmap.length; r++)
			for (int c = 0; c < bmap[0].length; c++)
				bmap[r][c] = false;

		if (sides[3] - sides[2] + 1 >= 0.5 * bmap[0].length)
			return bmap;

		for (int r = sides[0]; r <= sides[1]; r++) {
			bmap[r][sides[2]] = true;
			bmap[r][sides[3]] = true;
		}

		for (int c = sides[2]; c <= sides[3]; c++) {
			bmap[sides[0]][c] = true;
			bmap[sides[1]][c] = true;
		}

		return bmap;
	}

	/**
	 * Find a side
	 * 
	 * @param type
	 *            choose corner
	 * @return outer limits of box
	 */
	public static int findSide(int type) {

		if (type == 0)
			for (int r = 0; r < bmap.length; r++)
				for (int c = 0; c < bmap[0].length; c++)
					if (bmap[r][c])
						return r;

		if (type == 1)
			for (int r = bmap.length - 1; r >= 0; r--)
				for (int c = bmap[0].length - 1; c >= 0; c--)
					if (bmap[r][c])
						return r;

		if (type == 2)
			for (int c = 0; c < bmap[0].length; c++)
				for (int r = 0; r < bmap.length; r++)
					if (bmap[r][c])
						return c;

		if (type == 3)
			for (int c = bmap[0].length - 1; c >= 0; c--)
				for (int r = bmap.length - 1; r >= 0; r--)
					if (bmap[r][c])
						return c;

		return 0;
	}
}
