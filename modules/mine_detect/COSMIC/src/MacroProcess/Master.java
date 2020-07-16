package MacroProcess;

import java.io.IOException;

import Function.Format;
import GUI.Window;

/**
 * Main class for large images
 *
 */
public class Master {

	static String file;
	
	public static boolean findMine(String fileSet) throws IOException {
		
		init(fileSet);
		
		boolean[][][] layers;
		boolean[][] dark;
		boolean[][] bright;
		boolean[][] bmap;
		
		Slide.init(file, 200, true);
		layers = new boolean[2][Slide.bmap.length][Slide.bmap[0].length];
		
		for (int i = 0; i < 2; i++) {
			
			Slide.init(file, 200, i == 0);
			Slide.process();
			
			Window.create("Slide", file, Format.slideArea(Slide.bmap, Slide.vals, Slide.dim), 1, 1);
			layers[i] = Slide.bmap;
		}
		
/*		Slide.init(file, 200, true);
		Slide.process();
		
		Window.create("Slide", file, Format.slideArea(Slide.bmap, Slide.vals, Slide.dim), 1, 1);
		dark = Slide.bmap;
		
		Slide.init(file, 200, false);
		Slide.process();
		
		Window.create("Slide", file, Format.slideArea(Slide.bmap, Slide.vals, Slide.dim), 1, 1);
		bright = Slide.bmap;*/
		
		bmap = new boolean[layers[0].length][layers[0][0].length];
		for (int r = 0; r < bmap.length; r++)
			for (int c = 0; c < bmap[0].length; c++)
				bmap[r][c] = layers[0][r][c] && layers[1][r][c];
		
		Window.create("Slide", file, Format.slideArea(bmap, Slide.vals, Slide.dim), 1, 1);
		
		return Function.Calc.containsTrue(bmap);
	}
	
	public static void init(String fileSet) {
		
		file = fileSet;
	}
}
