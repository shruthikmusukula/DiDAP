package GUI;

import java.awt.*;

import javax.swing.*;

import Function.*;

public class Frame extends JPanel implements Runnable {

	public JFrame frame;
	public int dim = 600;
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
		frame = new JFrame(name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(dim + 17, dim + 68);
		frame.setVisible(true);
		frame.add(this);
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		addFullSizeImage(g, imageName);
		drawOutline(g, arr, cenN);
	}

	public void draw(String imageNameSet, int[][] arrSet, int cenNSet) {

		imageName = imageNameSet;
		arr = arrSet;
		cenN = cenNSet;

		widthR = 1.0 * dim / arr[0].length;
		heightR = 1.0 * dim / arr.length;

		frame.repaint();
	}

	// int width = (int)(dim*(arr[0].length/3));
	// int height = (int)(dim*(arr.length/3));

	public void addFullSizeImage(Graphics g, String imageName) {

		Image image = Toolkit.getDefaultToolkit().getImage("COSMIC/Images/" + imageName);
		int width = dim;
		int height = dim;
		g.drawImage(image, 0, 0, width, height, this);
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
		g.fillRect(tempX, tempY, dX, dY);
	}

	public int refit(int pos, double ratio) {

		return (int) Math.round((pos * ratio));
	}

	@Override
	public void run() {

	}
}
