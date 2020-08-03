import java.io.IOException;

/**
 * Splits image into sections to check for mines
 *
 */
public class Slide {

	public static boolean[][] bmap;

	public static double[][] vals;

	public static int dim;

	static boolean dark;

	/**
	 * Initialize slide
	 * 
	 * @param im
	 *            filename
	 * @param dimSet
	 *            dimensions of square to scan at a time (pixels)
	 * @param darkSet
	 *            filter for dark spots or light spots
	 * @throws IOException
	 */
	public static void init(byte[][] im, int dimSet, boolean darkSet) throws IOException {

		vals = Convert.convertByte(im);
		dim = dimSet;
		dark = darkSet;

		bmap = new boolean[(int) Math.ceil(1.0 * vals.length / dim) * 2
				- 1][(int) Math.ceil(1.0 * vals[0].length / dim) * 2 - 1];
	}

	/**
	 * Initialize slide
	 * 
	 * @param valSet
	 *            image values
	 * @param dimSet
	 *            dimensions of square to scan at a time (pixels)
	 * @param darkSet
	 *            filter for dark spots or light spots
	 * @throws IOException
	 */
	public static void init(double[][] valSet, int dimSet, boolean darkSet) {

		dim = dimSet;
		dark = darkSet;
		vals = valSet;

		bmap = new boolean[(int) Math.ceil(1.0 * vals.length / dim)][(int) Math.ceil(1.0 * vals[0].length / dim)];
	}

	/**
	 * Search each sector for a mine
	 */
	public static void process() {

		for (int r = 0; r < bmap.length; r++) {
			for (int c = 0; c < bmap[0].length; c++) {
				if (c == 0) bmap[r][c] = false;
				else bmap[r][c] = Master.findMine(findSector(r / 2.0, c / 2.0), dark);
			}
		}
	}

	/**
	 * Find specified area of image when split into rows and columns
	 * 
	 * @param r
	 *            row
	 * @param c
	 *            column
	 * @return array with values from sector
	 */
	public static double[][] findSector(double r, double c) {

		double[][] send = new double[refit(r, true)][refit(c, false)];

		for (int i = 0; i < send.length; i++)
			for (int j = 0; j < send[0].length; j++)
				send[i][j] = vals[(int) Math.round(r * dim + i)][(int) Math.round(c * dim + j)];

		return send;
	}

	/**
	 * Find starting position for sector
	 * 
	 * @param n
	 *            position
	 * @param row
	 * @return
	 */
	public static int refit(double n, boolean row) {

		int limit = row ? vals.length : vals[0].length;

		return (int) Math.round(n + 1) * dim > limit ? (int) Math.round(limit - n * dim) : dim;
	}
}
