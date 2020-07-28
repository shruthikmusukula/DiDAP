package MacroProcess;

import java.io.IOException;

import Function.Constants.Save;
import Function.Format;
import GUI.Window;

/**
 * Main class for large images
 *
 */
public class Master {

	static String file;
	static String name;
	static Save save;
	static int dim = 200;

	public static boolean findMine(String fileSet, Save saveSet) throws IOException {
		init(fileSet, saveSet);

		boolean[][][] layers;
		boolean[][] dark;
		boolean[][] bright;
		boolean[][] bmap;

		Slide.init(file, dim, true);
		layers = new boolean[2][Slide.bmap.length][Slide.bmap[0].length];

		for (int i = 0; i < 2; i++) {
			Slide.init(file, dim, i == 0);
			Slide.process();

			Window.create(name, file, Format.slideArea(Slide.bmap, Slide.vals, Slide.dim), 1, 1, save, false);
			layers[i] = Slide.bmap;
		}

		bmap = new boolean[layers[0].length][layers[0][0].length];
		for (int r = 0; r < bmap.length; r++)
			for (int c = 0; c < bmap[0].length; c++)
				bmap[r][c] = layers[0][r][c] && layers[1][r][c];

		Window.create(name, file, Format.slideArea(bmap, Slide.vals, Slide.dim), 1, 1, save, true);

		return Function.Calc.containsTrue(bmap);
	}

	public static void init(String fileSet, Save saveSet) {

		file = fileSet;
		name = Function.Util.getFileName(file);
		save = saveSet;
	}
}
