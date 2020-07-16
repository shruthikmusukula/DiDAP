package MacroProcess;

import java.io.IOException;

/**
 * Splits image into sections to check for mines
 *
 */
public class Slide {

	public static boolean[][] bmap;

	public static double[][] vals;

	static String file;
	public static int dim;

	static boolean dark;

	/**
	 * Initialize slide
	 * 
	 * @param fileSet
	 *            filename
	 * @param dimSet
	 *            dimensions of square to scan at a time (pixels)
	 * @param darkSet
	 *            filter for dark spots or light spots
	 * @throws IOException
	 */
	public static void init(String fileSet, int dimSet, boolean darkSet) throws IOException {

		file = fileSet;
		dim = dimSet;

		dark = darkSet;

		vals = Image.Convert.convertImageCompress(file, 1);
		bmap = new boolean[(int) Math.ceil(1.0 * vals.length / dim)][(int) Math.ceil(1.0 * vals[0].length / dim)];
	}

	/**
	 * Search each sector for a mine
	 */
	public static void process() {

		for (int r = 0; r < bmap.length; r++)
			for (int c = 0; c < bmap[0].length; c++)
				bmap[r][c] = FrameProcess.Master.findMine(findSector(r, c), dark);
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
	public static double[][] findSector(int r, int c) {

		double[][] send = new double[refit(r, true)][refit(c, false)];

		for (int i = 0; i < send.length; i++)
			for (int j = 0; j < send[0].length; j++)
				send[i][j] = vals[r * dim + i][c * dim + j];

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
	public static int refit(int n, boolean row) {

		int limit = row ? vals.length : vals[0].length;

		return (n + 1) * dim > limit ? limit - n * dim : dim;
	}
}
