import java.io.IOException;

/**
 * Main class for large images
 *
 */
public class MacroMaster {
	
	static byte[][] imL;
	static byte[][] imR;
	static byte[][] im;
	static int dim = 150;

	public static boolean findMine(byte[][] imTemp, char side) throws IOException {
		im = imTemp;
		boolean[][][] layers;
		boolean[][] bmap;

		// find dark spots
		Slide.init(im, dim, true);
		layers = new boolean[2][Slide.bmap.length][Slide.bmap[0].length];
		Slide.process();
		layers[0] = Slide.bmap;
		
		// find light spots
		Slide.init(im, dim, false);
		Slide.process();
		layers[1] = Slide.bmap;

		byte[][] imOut = new byte[imTemp.length][imTemp[0].length];
		
		// find overlapping areas
		bmap = new boolean[layers[0].length][layers[0][0].length];
		for (int r = 0; r < bmap.length; r++) {
			for (int c = 0; c < bmap[0].length; c++) {
				bmap[r][c] = layers[0][r][c] && layers[1][r][c];
				if(bmap[r][c]) System.out.println(r + ", " + c);
				
				// identify bounds of frame on original image and highlight if needed
				int[] temp = Util.refitRect(r, c, bmap[0].length, bmap.length, imTemp[0].length, imTemp.length);
				for (int x = temp[1]; x < (temp[3] < imTemp.length ? temp[3] : imTemp.length); x++) {
					for (int y = temp[0]; y < (temp[2] < imTemp[0].length ? temp[2] : imTemp[0].length); y++) {
						if (bmap[r][c]) { imOut[x][y] = (byte)(Util.getByteVal(imTemp[x][y]) > 150 ? 255 : Util.getByteVal(imTemp[x][y]) + 100); }
						else imOut[x][y] = imTemp[x][y];
					}						
				}
			}
		}
		
		if (side == 'L') imL = imOut;
		else imR = imOut;
		
		return Calc.containsTrue(bmap);
	}
}
