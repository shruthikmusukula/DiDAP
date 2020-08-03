
import java.awt.Color;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


import javax.imageio.ImageIO;

/**
 * Utility functions for the SonarProcessing file
 *
 */
public class Util {

	public static int width;
	public static int height;
	
	public static String imgLabel;
	public static BufferedImage img;
	
	private final static String[] hexSymbols = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D",
			"E", "F" };
	public final static int BITS_PER_HEX_DIGIT = 4;

	/**
	 * Convert byte to hex
	 * 
	 * @param b
	 *            byte
	 * @return hex
	 */
	public static String toHexFromByte(final byte b) {
		byte leftSymbol = (byte) ((b >>> BITS_PER_HEX_DIGIT) & 0x0f);
		byte rightSymbol = (byte) (b & 0x0f);
		return (hexSymbols[leftSymbol] + hexSymbols[rightSymbol]);
	}

	/**
	 * Convert hex to byte
	 * 
	 * @param s
	 *            hex
	 * @return byte
	 */
	public static byte[] toByteFromHex(final String s) {
		byte[] b = new byte[s.length() / 2];
		for (int i = 0; i < b.length; i++) {
			int index = i * 2;
			int v = Integer.parseInt(s.substring(index, index + 2), 16);
			b[i] = (byte) v;
		}
		return b;
	}

	/**
	 * Convert Julian Time to Time Julian time is number of seconds since midnight
	 * JulianTime= (((Hour * 60) + Minutes) * 60) + Seconds
	 * 
	 * @param Long
	 * @return regular time
	 */
	public static String JulianToTime(final long Long) {
		String Time = "";
		Time = (int) Long / 3600 + ":" + (int) ((Long % 3600) / 60) + ":" + (int) Long % 60;
		return Time;
	}

	/**
	 * Convert the float value to degrees and decimal minutes for Latitude and
	 * Longitude
	 * 
	 * @param Float
	 *            float
	 * @return degrees
	 */
	public static String FloatToDegree(final float Float) {
		float degree = 0;
		degree = (Float - Float % 60) / 60;
		float min = 0;
		min = Float - (degree * 60);
		float sec = min * 60;
		min = (sec - sec % 60) / 60;
		sec = sec - min * 60;
		String Degree_Min_Sec = degree + "deg" + min + "'" + sec + "''";
		return Degree_Min_Sec;
	}

	public static String[][] trimNull(String[][] vals) {
		int i = 0; 
		while (vals[i][0] == null && i < vals.length)
			i++;
		int start = i;
		i = vals.length - 1;
		while (vals[i][0] == null && i > 0)
			i--;
		int end = i;
		String[][] hold = new String[end - start + 1][vals[0].length];
		for (i = start; i <= end; i++) 
			hold[i - start] = vals[i];
		return hold;
	}
	
	/**
	 * Combine left and right channels
	 * 
	 * @param left
	 *            byte array of left channel
	 * @param right
	 *            byte array of right channel
	 * @return combined byte array
	 */
	public static byte[][] combineHorizontally(byte[][] left, byte[][] right) {
		if (left.length != right.length)
			return null;
		byte[][] hold = new byte[left.length][left[0].length + right[0].length];
		for (int i = 0; i < hold.length; i++) {
			for (int j = 0; j < left[0].length; j++) {
				hold[i][j] = left[i][left[0].length - (j + 1)];
			}
			for (int j = 0; j < right[0].length; j++) {
				hold[i][left[0].length + j] = right[i][j];
			}
		}
		return hold;
	}

	/**
	 * Combine two consecutive byte arrays
	 * 
	 * @param top
	 *            top byte array
	 * @param bottom
	 *            bottom byte array
	 * @return combine byte array
	 */
	public static byte[][] combineVertically(byte[][] top, byte[][] bottom) {
		// check that the widths match
		if (top[0].length != bottom[0].length)
			return null;
		// add the first and then second arrays to hold
		byte[][] hold = new byte[top.length + bottom.length][top[0].length];
		for (int i = 0; i < top.length; i++) {
			for (int j = 0; j < top[0].length; j++) {
				hold[i][j] = top[i][j];
			}
		}
		for (int i = 0; i < bottom.length; i++) {
			for (int j = 0; j < bottom[0].length; j++) {
				hold[top.length + i][j] = bottom[i][j];
			}
		}
		return hold;
	}

	/**
	 * Combines two string arrays
	 * 
	 * @param top
	 *            first array
	 * @param bottom
	 *            second array
	 * @return combined array
	 */
	public static String[][] combineVertically(String[][] top, String[][] bottom) {
		if (top[0].length != bottom[0].length)
			return null;
		String[][] hold = new String[top.length + bottom.length][top[0].length];
		for (int i = 0; i < top.length; i++) {
			for (int j = 0; j < top[0].length; j++) {
				hold[i][j] = top[i][j];
			}
		}
		for (int i = 0; i < bottom.length; i++) {
			for (int j = 0; j < bottom[0].length; j++) {
				hold[top.length + i][j] = bottom[i][j];
			}
		}
		return hold;
	}
	
	/**
	 * Transform byte to integer value
	 * @param z
	 * @return
	 */
	public static int getByteVal(byte z) {
		int hold = (int) (z);
		if (hold < 0)
			hold = 256 + hold;
		int val = hold >= 0 ? hold : 0;
		return val;
	}
	
	/**
	 * Transform byte array to BufferedImage
	 */
	public static BufferedImage getImage(byte[][] arr, boolean sepia) {
		img = new BufferedImage(arr.length, arr[0].length, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < arr.length; x++) {
			for (int y = 0; y < arr[0].length; y++) {
				int val = getByteVal(arr[x][y]);
				if (sepia) {
					// Transformation to sepia
					int r = (int) ((val * .393) + (val * .769) + (val * .189));
					r = r > 255 ? 255 : r;
					int g = (int) ((val * .349) + (val * .686) + (val * .168));
					g = g > 255 ? 255 : g;
					int b = (int) ((val * .272) + (val * .534) + (val * .131));
					b = b > 255 ? 255 : b;
					img.setRGB(x, y, new Color(r, g, b).getRGB());
				} else {
					img.setRGB(x, y, new Color(val, val, val).getRGB());
				}
			}
		}
		
		// just info that's good to know
		width = img.getWidth();
	    height = img.getHeight();
	    System.out.println("BufferedImage width: " + width);
	    System.out.println("BufferedImage height: " + height);
	    
	    return img;
	}
	
	/**
	 * Write image to png file
	 * 
	 * @param arr
	 *            byte array for image
	 * @param label
	 *            label to save as
	 */
	public static String saveIm(byte[][] arr, String label, boolean sepia) {
//		String save = Constants.out_path + "\\" + label + ".png";
		getImage(arr, sepia);
	    
		// copy label to public static variable imgLabel
		imgLabel = label;
		System.out.println("Image saved in Util.saveIm!");

		return imgLabel;
	}
	
	/**
	 * Write image to png file
	 * 
	 * @param arr
	 *            integer array for image
	 * @param label
	 *            label to save as
	 */
	//TODO add getImage call
	public static String saveIm(int[][] arr, String label, boolean sepia) {
//		String save = Constants.out_path + "\\" + label + ".png";
		img = new BufferedImage(arr.length, arr[0].length, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < arr.length; x++) {
			for (int y = 0; y < arr[0].length; y++) {
				int val = arr[x][y];
				if (sepia) {
					// Transformation to sepia
					int r = (int) ((val * .393) + (val * .769) + (val * .189));
					r = r > 255 ? 255 : r;
					int g = (int) ((val * .349) + (val * .686) + (val * .168));
					g = g > 255 ? 255 : g;
					int b = (int) ((val * .272) + (val * .534) + (val * .131));
					b = b > 255 ? 255 : b;
					img.setRGB(x, y, new Color(r, g, b).getRGB());
				} else {
					img.setRGB(x, y, new Color(val, val, val).getRGB());
				}
			}
		}


		// just info that's good to know
		width = img.getWidth();
	    height = img.getHeight();
	    System.out.println("BufferedImage width: " + width);
	    System.out.println("BufferedImage height: " + height);
	    
		// copy label to public static variable imgLabel
		imgLabel=label;
		try {
			System.out.println("Image saved in Util.saveIm!");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return imgLabel;
	}

	/**
	 * Save a 2D array to a CSV
	 * 
	 * @param arr
	 *            array to save
	 * @param label
	 *            name to give
	 */
	public static void save(String[][] arr, String label) {
		// unused function
	}
	
	/**
	 * Scale two similar arrays and check if point pR, pC from im is in sector r, c from map
	 * @param r
	 * @param r sector in map along width
	 * @param c sector in map along height
	 * @param mapW map width
	 * @param mapH map height
	 * @param imW image width
	 * @param imH image height
	 * @return x1, y1, x2, y2
	 */
	public static int[] refitRect(int r, int c, double mapW, double mapH, double imW, double imH) {
		double widthR = 1.0 * imW / mapW;
		double heightR = 1.0 * imH / mapH;
		int tempX = refit(c, widthR);
		int tempY = refit(r, heightR);
		int dX = refit(c + 1, widthR) - refit(c, widthR);
		int dY = refit(r + 1, heightR) - refit(r, heightR);
		return new int[] { tempX, tempY, tempX + dX, tempY + dY };
	}

	/**
	 * Returns scaled point based on ratio
	 */
	public static int refit(int pos, double ratio) {
		return (int) ((pos * ratio) + 0.5);
	}

	/**
	 * returns arraylist of all filenames in directory
	 * 
	 * @param dir
	 *            directory to cycle through
	 * @param fileExtension
	 *            file extension to find
	 * @return list of files
	 */
	public static ArrayList<String> findAllFiles(String dir, String fileExtension) {
		ArrayList<String> totalFileList = new ArrayList<String>();
		File test = new File(dir);
		if (test.exists()) {
			if (test.isDirectory()) {
				String[] testList = test.list();
				System.out.println("ok");
				for (int i = 0; i < testList.length; i++) {
					File test_sub = new File(dir + "\\" + testList[i]);
					if (test_sub.isDirectory()) {
						String[] test_subList = test_sub.list();
						for (int j = 0; j < test_subList.length; j++) {
							if (test_subList[j].substring(test_subList[j].length() - 3).equals(fileExtension)) {
								totalFileList.add(dir + "\\" + testList[i] + "\\" + test_subList[j]);
							}
						}
					} else {
						if (testList[i].substring(testList[i].length() - 3).equals(fileExtension)) {
							totalFileList.add(dir + "\\" + testList[i]);
						}
					}
				}

			} else {
				System.out.println("This is not folder");
				return null;
			}

		} else {
			System.out.println("There is no folder.");
			return null;
		}
		return totalFileList;
	}

	/**
	 * Remove path prefix - get file name only
	 * 
	 * @param name
	 *            string
	 * @return file name only
	 */
	public static String remPath(String name) {
		if (name.indexOf('/') != -1) {
			int i;
			for (i = name.length() - 1; name.charAt(i) != '/'; i--)
				;
			name = name.substring(i + 1);
		}
		return name;
	}

	/**
	 * Remove path prefix and file extension
	 * @param file
	 * @return
	 */
	public static String getFileName(String file) {
		file = remPath(file);
		file = file.substring(0, file.lastIndexOf("."));
		return file;
	}
	/**
	 * Surrounds string with single quotes
	 * 
	 * @param s
	 *            String
	 * @return 'String'
	 */
	public static String addSingQuote(String s) {
		return "'" + s + "'";
	}

}
