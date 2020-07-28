import java.io.*;
import java.util.*;

public abstract class ImageArrEdit {

	/**
	 * reads in file input
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static double[][] fileRead(String file) throws IOException {

		double[][] send = Convert.convertImageCompress(file, 1);
		System.out.println(send.length + "x" + send[0].length);
		return send;
	}

	/**
	 * Reduces to black and white
	 * 
	 * @param vals
	 * @param dark
	 * @return black = 1 white = 0 grey = 0-1
	 */
	public static double[][] colorReduction(double[][] vals, boolean dark) {

		double[][] send = new double[vals.length][vals[0].length];

		for (int r = 0; r < vals.length; r++)
			for (int c = 0; c < vals[0].length; c++)
				send[r][c] = dark ? (255 - vals[r][c]) / 255 : (vals[r][c]) / 255;

		return send;
	}
}
