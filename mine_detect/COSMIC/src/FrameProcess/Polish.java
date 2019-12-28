package FrameProcess;

import DataStorage.*;

public class Polish {

	public static LightPoint[][] rmap;
	static LightPoint[][] hold;
	
	public static void init(LightPoint[][] rmapSet) {
		
		rmap = rmapSet;
	}
	
	// concen = number of pixels needed to return true || range = pixels must be within radius to return true
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
	
	public static int adjCount(LightPoint p, double range) {
		
		int adj = -1;
		
		for (int i = 0; i < hold.length; i++)
			for (int j = 0; j < hold[0].length; j++)
				adj += hold[i][j].blink && Function.Calc.dist(p, hold[i][j]) <= range ? 1 : 0;
		
		return adj;
	}
}
