public class Format {
	
	public static int[][] Area(LightPoint[][] tmap) {
		
		int[][] send = new int[tmap.length][tmap[0].length];
		
		for (int r = 0; r < tmap.length; r++)
			for (int c = 0; c < tmap[0].length; c++)
				send[r][c] = tmap[r][c].tag;
		
		return send;
	}
	
	public static int[][] cenArea(LightPoint[][] tmap, Centroid cens[]) {
		
		int[][] send = Area(tmap);
		
		for (Centroid cen : cens)
			for (int r = -1; r <= 1; r++)
				for (int c = -1; c <= 1; c++) {
					int sr = cen.r == 0 || cen.r == send.length - 1 ? 0 : r;
					int sc = cen.c == 0 || cen.c == send[0].length - 1 ? 0 : c;
					send[cen.r + sr][cen.c + sc] = -3;
				}
		
		return send;
	}
	
	public static int[][] blinkArea(LightPoint[][] rmap) {
		
		int[][] send = new int[rmap.length][rmap[0].length];
		
		for (int r = 0; r < rmap.length; r++)
			for (int c = 0; c < rmap[0].length; c++)
				send[r][c] = rmap[r][c].blink ? -2 : -1;
		
		return send;
	}
	
	public static int[][] boxArea(boolean[][] bmap) {
		
		int[][] send = new int[bmap.length][bmap[0].length];
		
		for (int r = 0; r < bmap.length; r++)
			for (int c = 0; c < bmap[0].length; c++)
				send[r][c] = bmap[r][c] ? -2 : -1;
		
		return send;
	}
	
	public static int[][] slideArea(boolean[][] bmap, double[][] vals, int dim) {
		
		int[][] send = new int[vals.length][vals[0].length];
		
		for (int r = 0; r < send.length; r++)
			for (int c = 0; c < send[0].length; c++)
				send[r][c] = bmap[r/dim][c/dim] ? -2 : -1;
		
		return send;
	}
	
	// 	print 2D array
	public static void print2D(int[][] vals) {
		
		for (int[] row : vals) {
			for (int v : row)
				System.out.print(v + "  ");
			System.out.print("\n");
		}
	}
	
	public static void print2D(double[][] vals) {
		
		for (double[] row : vals) {
			for (double v : row)
				System.out.print(v + "  ");
			System.out.print("\n");
		}
	}
	
	public static void print2D(boolean[][] vals) {
		
		for (boolean[] row : vals) {
			for (boolean v : row)
				System.out.print(v + "  ");
			System.out.print("\n");
		}
	}
}
