package GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import Function.*;
import GUI.Frame;

public class Frame extends JFrame implements Runnable {

	public WindowPanel pane;
	Container content;
	static int w;
	static int h;
	double widthR;
	double heightR;

	String imageName;
	int[][] arr;
	int cenN;

	/**
	 * create JFrame
	 * 
	 * @param name
	 */
	public Frame(String name) {
		// frame = new JFrame(name);
		setTitle(name);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
//		setSize(dim + 17, dim + 68);
		setSize(w + 40, h + 40);

		pane = new WindowPanel();

		add(pane);
		setVisible(true);
	}

	public static void setDimensions(int width, int height) {
		w = width < height ? Constants.dim : (int) (((double) (width) / height) * Constants.dim);
		h = height < width ? Constants.dim : (int) (((double) (height) / width) * Constants.dim);
	}
	
	@Override
	public void run() {

	}
	
	/**
	 * Saves contents of frame as PNG
	 * 
	 * @param savename
	 */
	public void capture(String savename) {
		content = getContentPane();
		BufferedImage img = new BufferedImage(content.getWidth(), content.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = img.createGraphics();
		content.printAll(g2d);
		g2d.dispose();

		try {
			ImageIO.write(img, "png", new File(Function.Constants.out_path + "//" + savename + ".png"));
			System.out.println("\nWrote Image: " + savename);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		cleanup();
	}
	
	public void cleanup() {
		setVisible(false);
		dispose();
	}

	public class WindowPanel extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			addFullSizeImage(g, imageName);
			drawOutline(g, arr, cenN);
		}

		public void draw(String imageNameSet, int[][] arrSet, int cenNSet) {

			imageName = imageNameSet;
			arr = arrSet;
			cenN = cenNSet;

			widthR = 1.0 * w / arr[0].length;
			heightR = 1.0 * h / arr.length;

			repaint();
		}

		// int width = (int)(dim*(arr[0].length/3));
		// int height = (int)(dim*(arr.length/3));

		public void addFullSizeImage(Graphics g, String imageName) {

			Image image = Toolkit.getDefaultToolkit().getImage((imageName.indexOf("\\") == -1 ? "COSMIC/Images/" : "") + imageName);
			g.drawImage(image, 0, 0, w, h, this);
		}

		public void drawOutline(Graphics g, int[][] arr, int cenN) {

			// fill in centroid areas
			for (int i = 0; i < cenN; i++)
				fillGrid(g, ColorEdit.colorCycle(i, cenN), i);

			// for monochrome steps
			fillGrid(g, new Color(255, 40, 150, 100), -2);

			// mark centroid locations
			fillGrid(g, new Color(255, 255, 255, 100), -3);
		}

		/**
		 * Mark areas of image based on grid
		 * 
		 * @param g
		 * @param color
		 * @param i
		 *            where to fill
		 */
		public void fillGrid(Graphics g, Color color, int i) {

			g.setColor(color);

			for (int r = 0; r < arr.length; r++)
				for (int c = 0; c < arr[0].length; c++)
					if (arr[r][c] == i)
						refitRect(g, r, c);
		}

		/**
		 * Draw rectangle at given position
		 * 
		 * @param g
		 * @param r
		 * @param c
		 */
		public void refitRect(Graphics g, int r, int c) {
			int tempX = refit(c, widthR);
			int tempY = refit(r, heightR);
			int dX = refit(c + 1, widthR) - refit(c, widthR);
			int dY = refit(r + 1, heightR) - refit(r, heightR);
//			System.out.println(tempX + " " + tempY + " " + dX + " " + dY);
			g.fillRect(tempX, tempY, dX, dY);
		}

		public int refit(int pos, double ratio) {

			return (int) Math.round((pos * ratio));
		}
	}
}
