package Function;

import DataStorage.*;

public class Converter {

	public static LightPoint[][] convert(double[][] vals) {
		
		LightPoint[][] send = new LightPoint[vals.length][vals[0].length];
		
		for (int r = 0; r < vals.length; r++)
			for (int c = 0; c < vals[0].length; c++)
				send[r][c] = new LightPoint(r, c, vals[r][c]);
		
		return send;
	}
	
	public static boolean[][] convert(LightPoint[][] map) {
		
		boolean[][] send = new boolean[map.length][map[0].length];
		
		for (int r = 0; r < map.length; r++)
			for (int c = 0; c < map[0].length; c++)
				send[r][c] = map[r][c].blink;
		
		return send;
	}
	
	public static LightPoint[][] reduce(LightPoint[][] map) {
		
		LightPoint[][] send = new LightPoint[map.length][map[0].length];
		
		for (int r = 0; r < map.length; r++)
			for (int c = 0; c < map[0].length; c++)
				send[r][c] = new LightPoint(r, c, map[r][c].blink ? 1 : -1);
		
		return send;
	}
	
	public static LightPoint[][] scanReduce(LightPoint[][] map) {
		
		LightPoint[][] send = new LightPoint[map.length][map[0].length];
		
		for (int r = 0; r < map.length; r++)
			for (int c = 0; c < map[0].length; c++)
				send[r][c] = new LightPoint(r, c, map[r][c].tag != -1 ? 1 : -1);
		
		return send;
	}
}
