package Function;

import java.awt.Color;

public class ColorEdit {

	public static Color colorCycle(int i, int n) {
		
		double pos = 6.0*i/n;
		int[] rgb = new int[3];
		
		for (int j = 0; j < rgb.length; j++) {
			rgb[j] = (int)(255*(pos < 3 ? top(pos) : -1*top(pos - 3) + 1));
			pos = (pos + 4)%6;
		}
		
/*		for (int v : rgb)
			System.out.print(v + " ");
		System.out.print("\t" + i + " " + n + "\n");
*/
			
		return new Color(rgb[0], rgb[1], rgb[2], 175);
	}
	
	public static Color heat(double n) {
		
		int[] rgb = new int[3];
		
		if (n > 0.75) {
			rgb[0] = 255;
			rgb[1] = (int)(255*(quart(1 - n, true)));
		} else if (n > 0.5) {
			rgb[0] = (int)(255*(quart(0.75 - n, false)));
			rgb[1] = 255;
		} else if (n > 0.25) {
			rgb[1] = 255;
			rgb[2] = (int)(255*(quart(0.5 - n, true)));
		} else {
			rgb[1] = (int)(255*(quart(0.25 - n, false)));
			rgb[2] = 255;
		}
		
		return new Color(rgb[0], rgb[1], rgb[2]);
	}
	
	public static double top(double pos) {
		
		if (pos >= 2)
			return 0;
		else if (pos >= 1)
			return -1*drop(pos) + 1;
		else
			return 1;
	}
	
	public static double drop(double n) {
		
		return n - (int)n;
	}
	
	public static double quart(double n, boolean pos) {
		
		return pos ? 4*n : 1 - 4*n;
	}
}
