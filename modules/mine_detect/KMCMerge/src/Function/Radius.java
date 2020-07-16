package Function;

import DataStorage.*;

public class Radius {

	static LightPoint[][] map;
	static LightPoint p;
	static double range;
	
	static int ulim;
	static int dlim;
	static int llim;
	static int rlim;
	public static int r;
	public static int c;
	
	public static void init(LightPoint[][] mapSet, LightPoint pSet, double rangeSet) {
		
		map = mapSet;
		p = pSet;
		range = rangeSet;
		
		ulim = Calc.edgeCut(map.length, (int)Math.floor(p.r - range));
		dlim = Calc.edgeCut(map.length, (int)Math.ceil(p.r + range));
		llim = Calc.edgeCut(map[0].length, (int)Math.floor(p.c - range));
		rlim = Calc.edgeCut(map[0].length, (int)Math.ceil(p.c + range));
		
		r = ulim;
		c = llim;
	}
	
	public static boolean process() {
		
		c++;
		
		if (c > rlim) {
			r++;
			c = llim;
		}
		
		return r <= dlim;
	}
}
