package Function;

import DataStorage.*;

public class Calc {
	
	public static int adjCount(LightPoint[][] tmap, LightPoint p, double range) {
		
		int adj = 0;
		
		Radius.init(tmap, p, range);
		
		do {
			LightPoint act = tmap[Radius.r][Radius.c];
			adj += act.lum != -1 && dist(p, act) <= range ? 1 : 0;
		} while (Radius.process());
		
		return adj;
	}
	
	public static double adjValue(LightPoint[][] tmap, LightPoint p, double range) {
		
		double adj = 0;
		
		Radius.init(tmap, p, range);
		
		do {
			LightPoint act = tmap[Radius.r][Radius.c];
			adj += act.lum != -1 && dist(p, act) <= range ? act.lum : 0;
		} while (Radius.process());
		
		return adj;
	}

	public static double dist(double x, double y, double z) {
		
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	public static double dist(double x, double y) {
		
		return Math.sqrt(x*x + y*y);
	}
	
	public static double dist(double x1, double y1, double x2, double y2) {
		
		double x = Math.abs(x1 - x2);
		double y = Math.abs(y1 - y2);
		
		return dist(x, y);
	}
	
	public static double dist(Point p1, Point p2) {
		
		return dist(p1.r, p1.c, p2.r, p2.c);
	}
	
	public static int edgeCut(int limit, int edge) {
		
		edge = edge < 0 ? 0 : edge;
		edge = edge > limit - 1 ? limit - 1 : edge;
		
		return edge;
	}
	
	// formula that inputs 0-1 and outputs 0-1 (1/x curve)
	public static double curve(double n, double weight) {
		
		double h = (1 + Math.sqrt(1 + 4*weight))/2;
		double k = weight/h;
		
		double func = weight/(h - n) - k;
		
		return func;
	}
}
