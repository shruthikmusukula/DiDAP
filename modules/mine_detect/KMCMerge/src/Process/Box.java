package Process;

import DataStorage.*;

public class Box {

	public static boolean[][] bmap;
	
	static Point[] corners; // TL, TR, BL, BR
	static int[] sides; // T, B, L, R
	
	static int exp;  // box adjust size
	
	public static void init(boolean[][] bmapSet, int expSet) {
		
		bmap = bmapSet;
		exp = expSet;
		
		corners = new Point[4];
		
		for (int i = 0; i < corners.length; i++)
			corners[i] = findPoint(i);
		
		sides = new int[4];
		
		for (int i = 0; i < sides.length; i++)
			sides[i] = findSide(i);
	}
	
	public static boolean[][] process() {
		
		if (!check()) return bmap;
		
		for (int r = 0; r < bmap.length; r++)
			for (int c = 0; c < bmap[0].length; c++)
				bmap[r][c] = false;

		if (sides[3] - sides[2] + 1 >= 0.5*bmap[0].length)
			return bmap;

		for (int r = sides[0]; r <= sides[1]; r++) {
			bmap[r][sides[2]] = true;
			bmap[r][sides[3]] = true;
		}
		
		for (int c = sides[2]; c <= sides[3]; c++) {
			bmap[sides[0]][c] = true;
			bmap[sides[1]][c] = true;
		}
		
		return bmap;
	}
	
	public static boolean check() {
		
		for (boolean[] row : bmap)
			for (boolean b : row)
				if(b) return true;
		
		return false;
	}
	
	public static Point findPoint(int type) {
		
		Point anchor = new Point(type <= 1 ? 0 : bmap.length - 1, type%2 == 0 ? 0 : bmap[0].length - 1);
		
		Point minC = new Point(0, 0);
		for (int r = 0; r < bmap.length; r++)
			for (int c = 0; c < bmap[0].length; c++)
				if (bmap[r][c])
					minC = new Point(r, c);
		
		for (int r = 0; r < bmap.length; r++)
			for (int c = 0; c < bmap[0].length; c++)
				if (bmap[r][c] && Function.Calc.dist(new Point(r, c), anchor) < Function.Calc.dist(minC, anchor))
					minC = new Point(r, c);
				
		return minC;
	}
	
/*	public static int findSide(int type) {
		
		Point TL = corners[0];
		Point TR = corners[1];
		Point BL = corners[2];
		Point BR = corners[3];
		
		switch (type) {
			case 0 : return Math.max(TL.r, TR.r) - exp;
			case 1 : return Math.max(BL.r, BR.r) + exp;
			case 2 : return Math.max(TL.c, BL.c) - exp;
			case 3 : return Math.max(TR.c, BR.c) + exp;
		}
		
		return -1;
	}
*/
	
	public static int findSide(int type) {
		
		if (type == 0)
			for (int r = 0; r < bmap.length; r++)
				for (int c = 0; c < bmap[0].length; c++)
					if (bmap[r][c]) return r;
		
		if (type == 1)
			for (int r = bmap.length - 1; r >= 0; r--)
				for (int c = bmap[0].length - 1; c >= 0; c--)
					if (bmap[r][c]) return r;
		
		if (type == 2)
			for (int c = 0; c < bmap[0].length; c++)
				for (int r = 0; r < bmap.length; r++)
					if (bmap[r][c]) return c;
		
		if (type == 3)
			for (int c = bmap[0].length - 1; c >= 0; c--)
				for (int r = bmap.length - 1; r >= 0; r--)
					if (bmap[r][c]) return c;
		
		return 0;
	}
}
