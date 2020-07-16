package GUI;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Client.Runner;
import FrameProcess.*;
import Function.*;
import Function.Constants.Save;
import Function.Constants.Stage;

/**
 * Display steps of processing
 *
 */
public class Window {

	/**Number of windows created */ static int count = 1;
		
	/** 
	 * Create a window 
	 * @param name window name
	 * @param file 
	 * @param arr
	 * @param cenN
	 * @param loc window location
	 */
	public static void create(String name, String file, int[][] arr, int cenN, int loc, Save save, boolean fin) {
		if (save == Save.NO_DISPLAY || (save == Save.DISPLAY_FINAL && !fin)) return;
		
//		name = name + " " + count++;
		Frame p = new Frame(name);
		p.pane.draw(file, arr, cenN);
		
		if (save == Save.SAVE_ALL || (save == Save.SAVE_FINAL && fin)) p.capture(name);
		
		int x = 0;
		int y = 0;
		
		if (save == Save.DISPLAY_ALL) {
			x = Constants.dim * ((loc - 1)%3);
			y = loc <= 3 ? 0 : Constants.dim;
		}
		p.setLocation(x, y);
		
		if (save == Save.SAVE_FINAL && !fin) { 
			double time = System.currentTimeMillis();
			while(System.currentTimeMillis() - time < 100)
				continue;
			p.cleanup();
		}
	}
	
	/**
	 * Show step in window
	 * @param file
	 * @param cenN
	 * @param stage
	 * @param save
	 */
	public static void display(String file, int cenN, Stage stage, Save save) {
		if (save == Save.NO_DISPLAY)
			return;
		
		switch (stage) {
		case TOPOGRAPH: 
			create(stage.toString() + " " + Util.getFileName(file), file, Format.cenArea(Cluster.tmap, Cluster.cens), cenN, 1, save, false);
			break;
		case PLANE:
			create(stage.toString() + " " + Util.getFileName(file), file, Format.cenArea(Cluster.tmap, Cluster.cens), cenN, 5, save, false);
			break;
		case SCAN:
			create(stage.toString() + " " + Util.getFileName(file), file, Format.Area(Scan.tmap), cenN, 6, save, false);
			break;
		case COLOR_REDUCE:
			create(stage.toString() + " " + Util.getFileName(file), file, Format.blinkArea(Sift.map), cenN, 2, save, false);
			break;
		case CUT:
			create(stage.toString() + " " + Util.getFileName(file), file, Format.blinkArea(Polish.rmap), 1, 7, save, false);
			break;
		case BOX:
			create(stage.toString() + " " + Util.getFileName(file), file, Format.boxArea(Box.bmap), cenN, 8, save, true);
			break;
		case SLIDE:
		default:
			break;
		}
		
	}
	
}
