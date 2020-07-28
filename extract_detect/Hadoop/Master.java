import java.io.FileNotFoundException;

import java.io.IOException;

/**
 * Where the magic happens
 * 
 */
public class Master {

	static String file;
	static double[][] csv;

	static boolean dark;

	public static boolean findMine(String fileSet, boolean darkSet) throws IOException {

		file = fileSet;
		dark = darkSet;
		csv = ImageArrEdit.colorReduction(ImageArrEdit.fileRead(file), dark);

		System.out.println("start");

		return process(csv);
	}

	public static boolean findMine(double[][] vals, boolean darkSet) {

		dark = darkSet;
		csv = ImageArrEdit.colorReduction(vals, dark);

		System.out.print("-");

		return process(csv);
	}

	/**
	 * Processes image values
	 * 
	 * @param vals
	 *            array of color values from image
	 * @return if mine was found
	 */
	public static boolean process(double[][] vals) {
		LightPoint[][] tmap;
		LightPoint[][] rmap;
		boolean[][] bmap;

		int cenN;

		tmap = Converter.convert(vals);
		double minP = dark ? 28.3 : 20;
		rmap = Scan(tmap, 3, minP);
		rmap = ColorReduce(rmap, Scan.cenN);
		bmap = Converter.convert(rmap);
		bmap = Box(bmap, 0);

		return Calc.containsTrue(bmap);
	}

	/**
	 * 3 dimensional k-means clustering
	 * 
	 * @param tmap
	 * @param weight
	 * @param cenN
	 * @return topographic map
	 */
	public static LightPoint[][] Topograph(LightPoint[][] tmap, int weight, int cenN) {

		Cluster.init(tmap, weight, cenN);
		Cluster.process();

		return Cluster.tmap;
	}

	/**
	 * 2 dimensional k-means clustering
	 * 
	 * @param tmap
	 * @param cenN
	 * @return map
	 */
	public static LightPoint[][] Plane(LightPoint[][] tmap, int cenN) {

		Cluster.init(tmap, 0, cenN);
		Cluster.process();

		return Cluster.tmap;
	}

	/**
	 * DB Scan clustering
	 * 
	 * @param tmap
	 * @param epsilon
	 * @param minP
	 * @return map
	 */
	public static LightPoint[][] Scan(LightPoint[][] tmap, double epsilon, double minP) {

		Scan.init(tmap, epsilon, minP);
		Scan.process();

		return Scan.tmap;
	}

	/**
	 * Reduces number of clusters
	 * 
	 * @param tmap
	 * @param cenN
	 * @return map
	 */
	public static LightPoint[][] ColorReduce(LightPoint[][] tmap, int cenN) {

		Sift.init(tmap, cenN);
		Sift.colorSelect(Sift.maxColor());

		return Sift.map;
	}

	/**
	 * Reduces noise
	 * 
	 * @param rmap
	 * @return map
	 */
	public static LightPoint[][] Cut(LightPoint[][] rmap) {

		Polish.init(rmap);
		Polish.cut(6, 1.5);
		Polish.cut(3, 1);
		Polish.cut(3, 1);
		Polish.cut(2, 1);

		return Polish.rmap;
	}

	/**
	 * Creates box showing mine location
	 * 
	 * @param bmap
	 * @param exp
	 * @return map
	 */
	public static boolean[][] Box(boolean bmap[][], int exp) {

		Box.init(bmap, exp);
		Box.process();

		return Box.bmap;
	}
}
