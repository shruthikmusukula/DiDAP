package Extraction;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

import FrameProcess.Master;

/**
 * Utility functions for the SonarProcessing file
 * @author Pyojeong Kim, Mitali Chowdhury
 *
 */
public class Util {

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
		String Degree_Min_Sec = degree + "°" + min + "'" + sec + "''";
		return Degree_Min_Sec;
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
		if (top[0].length != bottom[0].length)
			return null;
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
	 * Save a 2D array to a CSV
	 * 
	 * @param arr
	 *            array to save
	 * @param label
	 *            name to give file
	 */
	public static void save(byte[][] arr, String label) {
		// Write image or metadata to CSV file
		// Specify directory
		try (PrintWriter writer = new PrintWriter(
				new File("D:\\Mitali\\ML\\NPS19\\data_csv\\extract\\data" + label + ".csv"))) {

			StringBuilder sb = new StringBuilder();

			for (int j = 0; j < arr.length; j++) {
				for (int k = 0; k < arr[0].length; k++) {
					sb.append(arr[j][k] + ",");
				}
				sb.append('\n');
			}
			writer.write(sb.toString());

			System.out.println("Saved " + label);

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
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

	public static void saveIm(byte[][] arr, String label) throws IOException {
		// Write image to png file
		BufferedImage img = new BufferedImage(arr.length, arr[0].length, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < arr.length; x++) {
			for (int y = 0; y < arr[0].length; y++) {
				int hold = (int) ((arr[x][y]));
				if (hold < 0) hold = 256 + hold;
				int val = hold >= 0 ? hold : 0;
				img.setRGB(x, y, new Color(val, val, val).getRGB());
			}
		}
		File out = new File("D:\\Mitali\\ML\\NPS19\\data_csv\\extract\\image" + label + ".png");
		ImageIO.write(img, "png", out);
//		Master.findMine("D:\\Mitali\\ML\\NPS19\\data_csv\\extract\\image" + label + ".png");
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
		// Write image or metadata to CSV file
		// Specify directory
		try (PrintWriter writer = new PrintWriter(
				new File("D:\\Mitali\\ML\\NPS19\\data_csv\\extract\\data" + label + ".csv"))) {

			StringBuilder sb = new StringBuilder();

			for (int j = 0; j < arr.length; j++) {
				if (arr[j][0] != (null)) {
					for (int k = 0; k < arr[0].length; k++) {
						sb.append(arr[j][k] + ",");
					}
					sb.append('\n');
				}
			}
			writer.write(sb.toString());

			System.out.println("Saved " + label);

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
}
