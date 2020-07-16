package Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Convert {

	/**
	 * Returns a double array of an image with each cell representing a pixel
	 * 
	 * @param name
	 *            file name
	 * @return the 2D double array representing the image
	 * @throws IOException
	 */
	public static double[][] convertImage(String name) throws IOException {
		
		BufferedImage image = ImageIO.read(new File("COSMIC/Images/" + name));
		SimplePicture pic = new SimplePicture(image);
		Pixel[][] arr = pic.getPixels2D();
		double[][] out = new double[arr.length][arr[0].length];
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				out[i][j] = arr[i][j].getAverage();
			}
		}
		return out;
	}
	

	/**
	 * Returns a double array of an image with each cell representing a pixel
	 * 
	 * @param name
	 *            file name
	 * @return the 2D double array representing the image
	 * @throws IOException
	 */
	public static double[][] convertImageCompress(String name, int scale) throws IOException {
		
		BufferedImage image = ImageIO.read(new File("COSMIC/Images/" + name));
		SimplePicture pic = new SimplePicture(image);
		Pixel[][] arr = pic.getPixels2D();
		double[][] out = new double[arr.length / scale][arr[0].length / scale];
		for (int i = 0; i < out.length; i++) {
			for (int j = 0; j < out[0].length; j++) {
				out[i][j] = arr[i*scale][j*scale].getAverage();
			}
		}
		return out;
	}
}
