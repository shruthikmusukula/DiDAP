package Process;

import java.io.FileNotFoundException;

import java.io.IOException;

import DataStorage.*;
import GUI.*;
import Function.*;

public class Master {
	
	static String file;
	static double[][] csv;
	
	static boolean display;
	
	static boolean dark;
	
	public static boolean findMine(String fileSet, boolean darkSet) throws IOException {
		
		init(fileSet, darkSet);
		display = true;
		
		System.out.println("start");
		
		return process(csv);
	}
	
	public static boolean findMine(double[][] vals, boolean darkSet) {
		
		dark = darkSet;
		display = false;
		csv = ImageArrEdit.colorReduction(vals, dark);
		
		System.out.println("start");
		
		return process(csv);
	}
	
	public static void init(String fileSet, boolean darkSet) throws IOException {
		
		file = fileSet;
		
		dark = darkSet;
		
		csv = ImageArrEdit.colorReduction(ImageArrEdit.fileRead(file), dark);
	}
	
	public static boolean process(double[][] vals) {
		
		LightPoint[][] tmap;
		LightPoint[][] rmap;
		boolean[][] bmap;
		
		int cenN;
		
		tmap = Converter.convert(vals);
/*		cenN = 7;
		tmap = Topograph(tmap, 10, cenN);
		rmap = ColorReduce(tmap, Cluster.cenN);*/
		double minP = dark ? 28.3 : 20;
		rmap = Scan(tmap, 3, minP);
		rmap = ColorReduce(rmap, Scan.cenN);
		bmap = Converter.convert(rmap);
		bmap = Box(bmap, 0);
		
		for (boolean[] row : bmap)
			for (boolean b : row)
				if (b) return true;
		
		return false;
	}
	
	public static LightPoint[][] Topograph(LightPoint[][] tmap, int weight, int cenN) {
	
		if (display) System.out.println("Topograph");
		
		Cluster.init(tmap, weight, cenN);
		Cluster.process();
		
		if(display) Window.display(file, cenN, "Topograph");
	
		return Cluster.tmap;
	}
	
	public static LightPoint[][] Plane(LightPoint[][] tmap, int cenN) {
		
		if (display) System.out.println("Plane");
		
		Cluster.init(tmap, 0, cenN);
		Cluster.process();
		
		if(display) Window.display(file, cenN, "Plane");
		
		return Cluster.tmap;
	}
	
	public static LightPoint[][] Scan(LightPoint[][] tmap, double epsilon, double minP) {
		
		if (display) System.out.println("Scan");
		
		Scan.init(tmap, epsilon, minP);
		Scan.process();
		
		if(display) Window.display(file, Scan.cenN, "Scan");
		
		return Scan.tmap;
	}
	
	public static LightPoint[][] ColorReduce(LightPoint[][] tmap, int cenN) {
		
		if (display) System.out.println("Color Reduce");
		
		Sift.init(tmap, cenN);
		Sift.colorSelect(Sift.maxColor());
		
		if(display) Window.display(file, cenN, "Color Reduce");
		
		return Sift.map;
	}
	
	public static LightPoint[][] Cut(LightPoint[][] rmap) {
		
		if (display) System.out.println("Cut");
		
		Polish.init(rmap);
		Polish.cut(6, 1.5);
		Polish.cut(3, 1);
		Polish.cut(3, 1);
		Polish.cut(2, 1);

		if (display) Window.display(file, 1, "Cut");

		return Polish.rmap;
	}
	
	public static boolean[][] Box(boolean bmap[][], int exp) {
		
		if (display) System.out.println("Box");
		
		Box.init(bmap, exp);
		Box.process();
		
		if (display) Window.display(file, 1, "Box");

		return Box.bmap;
	}
}
