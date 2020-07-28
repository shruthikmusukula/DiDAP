/**
 * Identifies clusters of specified colors 
 *
 */
public class Sift {

	public static LightPoint[][] map;
	
	static int cenN;
	
	/**
	 * initializes variables
	 * @param mapSet
	 * @param cenNSet
	 */
	public static void init(LightPoint[][] mapSet, int cenNSet) {
		
		map = mapSet;
		cenN = cenNSet;
	}
	
	/**
	 * selects a certain color and removes the rest
	 * @param select
	 * @return map
	 */
	public static LightPoint[][] colorSelect(int select) {
		
		for (int r = 0; r < map.length; r++)
			for (int c = 0; c < map[0].length; c++)
				map[r][c] = new LightPoint(r, c, map[r][c].tag == select && map[r][c].lum != -1 ? 1 : -1);
				
		return map;
	}
	
/*	public static LightPoint[][] scanSelect(int select) {
		
		for (int r = 0; r < map.length; r++)
			for (int c = 0; c < map[0].length; c++)
				map[r][c] = new LightPoint(r, c, map[r][c].tag == select && map[r][c].tag != -1 ? 1 : -1);
				
		return map;
	}*/
	
	/**
	 * finds darkest centroid
	 * tag returned indicates color
	 * @return tag of the Centroid with the most black
	 */
	public static int maxColor() {
		
		double[] areas = new double[cenN];
		double count = 0;
		
		for (int i = 0; i < cenN; i++) {
			for (LightPoint[] row : map)
				for (LightPoint p : row)
					count += p.tag == i ? Calc.curve(p.lum, 0.1) : 0;
			
			areas[i] = count;
			count = 0;
		}
		
		int maxIndex = 0;
		for (int i = 0; i < areas.length; i++)
			maxIndex = areas[i] > areas[maxIndex] ? i : maxIndex;
			
		return maxIndex;
	}
}
