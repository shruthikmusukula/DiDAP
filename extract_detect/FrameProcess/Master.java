package FrameProcess;

import java.io.FileNotFoundException;

import java.io.IOException;

import Client.Runner;
import DataStorage.*;
import GUI.*;
import Image.ImageArrEdit;
import Function.*;
import Function.Constants.Save;
import Function.Constants.Stage;

/**
 * Where the magic happens
 * 
 */
public class Master {

	static String file;
	static double[][] csv;

	static Save save;
	static boolean dark;

	public static boolean findMine(String fileSet, boolean darkSet, Save saveSet) throws IOException {

		file = fileSet;
		dark = darkSet;
		save = saveSet;
		csv = ImageArrEdit.colorReduction(ImageArrEdit.fileRead(file), dark);

		Frame.setDimensions(csv[0].length, csv.length);

		System.out.println("start");

		return process(csv);
	}

	public static boolean findMine(double[][] vals, boolean darkSet, Save saveSet) {

		dark = darkSet;
		save = saveSet;
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

		return Function.Calc.containsTrue(bmap);
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

		if (save == Save.DISPLAY_ALL)
			System.out.println("Topograph");

		Cluster.init(tmap, weight, cenN);
		Cluster.process();

		Window.display(file, cenN, Stage.TOPOGRAPH, save);

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

		if (save == Save.DISPLAY_ALL)
			System.out.println("Plane");

		Cluster.init(tmap, 0, cenN);
		Cluster.process();

		Window.display(file, cenN, Stage.PLANE, save);

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

		if (save == Save.DISPLAY_ALL)
			System.out.println("Scan");

		Scan.init(tmap, epsilon, minP);
		Scan.process();

		Window.display(file, Scan.cenN, Stage.SCAN, save);

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

		if (save == Save.DISPLAY_ALL)
			System.out.println("Color Reduce");

		Sift.init(tmap, cenN);
		Sift.colorSelect(Sift.maxColor());

		Window.display(file, cenN, Stage.COLOR_REDUCE, save);

		return Sift.map;
	}

	/**
	 * Reduces noise
	 * 
	 * @param rmap
	 * @return map
	 */
	public static LightPoint[][] Cut(LightPoint[][] rmap) {

		if (save == Save.DISPLAY_ALL)
			System.out.println("Cut");

		Polish.init(rmap);
		Polish.cut(6, 1.5);
		Polish.cut(3, 1);
		Polish.cut(3, 1);
		Polish.cut(2, 1);

		Window.display(file, 1, Stage.CUT, save);

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

		if (save == Save.DISPLAY_ALL || save == Save.DISPLAY_FINAL)
			System.out.println("Box");

		Box.init(bmap, exp);
		Box.process();

		Window.display(file, 1, Stage.BOX, save);

		return Box.bmap;
	}
}
