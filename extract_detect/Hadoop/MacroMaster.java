import java.io.IOException;

/**
 * Main class for large images
 *
 */
public class MacroMaster {

	static byte[][] im;
	static String name;
	static int dim = 200;
	
	static boolean lastVal = false;

	public static byte[][] findMine(byte[][] imTemp, String nameSet) throws IOException {
		init(imTemp, nameSet);

		boolean[][][] layers;
		boolean[][] bmap;

		Slide.init(im, dim, true);
		layers = new boolean[2][Slide.bmap.length][Slide.bmap[0].length];
		
		Slide.process();
		layers[0] = Slide.bmap;
		
		Slide.init(im, dim, false);
		Slide.process();
		layers[1] = Slide.bmap;

		byte[][] imOut = new byte[imTemp.length][imTemp[0].length];
		
		bmap = new boolean[layers[0].length][layers[0][0].length];
		for (int r = 0; r < bmap.length; r++) {
			for (int c = 0; c < bmap[0].length; c++) {
				bmap[r][c] = layers[0][r][c] && layers[1][r][c];
				int[] temp = Util.refitRect(r, c, bmap[0].length, bmap.length, imTemp[0].length, imTemp.length);
				for (int x = temp[1]; x < (temp[3] < imTemp.length ? temp[3] : imTemp.length); x++) {
					for (int y = temp[0]; y < (temp[2] < imTemp[0].length ? temp[2] : imTemp[0].length); y++) {
						if (bmap[r][c]) imOut[x][y] = (byte)(Util.getByteVal(imTemp[x][y]) > 150 ? 255 : Util.getByteVal(imTemp[x][y]) + 100);
						else imOut[x][y] = imTemp[x][y];
//						System.out.println(x + " " + y);
					}
				}
			}
		}
		
		Util.saveIm(imOut, nameSet, false);
		
		lastVal = Calc.containsTrue(bmap);
		
		return imOut;
	}

	public static void init(byte[][] imSet, String nameSet) {

		im = imSet;
		name = nameSet;
	}
}
