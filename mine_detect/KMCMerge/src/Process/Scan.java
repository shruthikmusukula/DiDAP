package Process;

import DataStorage.*;
import Function.*;

public class Scan {

	public static LightPoint[][] tmap;	// LightPoint array for 2D clustering

	static double epsilon;
	static double minP;
	
	public static int cenN;
	
	public static void init(LightPoint[][] tmapSet, double epsilonSet, double minPSet) {
		
		tmap = tmapSet;
		
		epsilon = epsilonSet;
		minP = minPSet;
		
		reset();
	}
	
	public static void reset() {
		
		for (LightPoint[] row : tmap)
			for (LightPoint p : row)
				p.tag = -1;
	}
	
	public static void check() {
		
		if (cenN > 5)
			reset();
	}
	
	public static void process() {
		
		cenN = 0;
		
		for (int r = 0; r < tmap.length; r++) {
			for (int c = 0; c < tmap[0].length; c++) {
				LightPoint p = tmap[r][c];
				if (p.tag < 0 && p.blink) {
					double value = Calc.adjValue(tmap, p, epsilon);
					if (value >= minP)
						expandCen(p, findSeed(p, Calc.adjCount(tmap, p, epsilon)), cenN++);
				}
			}
		}
	}
	
	public static void expandCen(LightPoint p, LightPoint[] seed, int cenN) {

		for (int i = 0; i < seed.length; i++) {
			LightPoint pK = seed[i];
			if (pK.tag < 0) {
				pK.tag = cenN;
				double value = Calc.adjValue(tmap, pK, epsilon);
				if (value >= minP) {
					LightPoint[] seedK = findSeed(pK, Calc.adjCount(tmap, pK, epsilon));
					seed = combine(seed, seedK);
				}
			}
		}
	}

/*	public static LightPoint[] findSeed(LightPoint p, int count) {
		
		LightPoint[] send = new LightPoint[count];
		
		int n = 0;
		for (int i = 0; i < tmap.length; i++)
			for (int j = 0; j < tmap[0].length; j++)
				if (tmap[i][j].lum != -1 && Calc.dist(p, tmap[i][j]) <= epsilon)
					send[n++] = tmap[i][j];

		return send;
	}*/
	
	public static LightPoint[] findSeed(LightPoint p, int count) {
		
		LightPoint[] send = new LightPoint[count];
		
		Radius.init(tmap, p, epsilon);
		
		int n = 0;
		do {
			LightPoint act = tmap[Radius.r][Radius.c];
			if (act.lum != -1 && Calc.dist(p, act) <= epsilon)
				send[n++] = act;
		} while(Radius.process());

		return send;
	}
	
	public static LightPoint[] combine(LightPoint[] s1, LightPoint[] s2) {
		
		LightPoint[] send = new LightPoint[s1.length + s2.length];
		
		for (int i = 0; i < s1.length; i++)
			send[i] = s1[i];
		
		for (int i = 0; i < s2.length; i++)
			send[s1.length + i] = s2[i];
		
		return send;
	}
	
/*	public static int adjCount(LightPoint p, double range) {
		
		int adj = 0;
		
		for (int i = 0; i < tmap.length; i++)
			for (int j = 0; j < tmap[0].length; j++)
				adj += tmap[i][j].lum != -1 && Calc.dist(p, tmap[i][j]) <= range ? 1 : 0;
		
		return adj;
	}
	
	public static double adjValue(LightPoint p, double range) {
		
		double adj = 0;
		
		for (int i = 0; i < tmap.length; i++)
			for (int j = 0; j < tmap[0].length; j++)
				adj += tmap[i][j].lum != -1 && Calc.dist(p, tmap[i][j]) <= range ? tmap[i][j].lum : 0;
		
		return adj;
	}*/
}
