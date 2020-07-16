package Client;

import java.io.FileNotFoundException;
import java.io.IOException;

import Extraction.Slide;
import Function.ColorEdit;
import Function.Format;
import Function.ImageArrEdit;
import GUI.Window;
import Process.Master;

public class Runner {

	public static void main(String[] args) throws IOException {

		System.out.println("start");
		slide();
	}
	
	public static void test() throws IOException {
		
		Master.findMine("sonar3.png", true);
	}
	
	public static void slide() throws IOException {
		
		String file = "bbbeefcake.png";
		
		boolean[][] bmap1;
		boolean[][] bmap2;
		boolean[][] bmapfinal;
		
		Slide.init(file, 200, true);
		Slide.process();
		
		Format.print2D(Slide.bmap);
		
		Window.create("Slide", file, Format.slideArea(Slide.bmap, Slide.vals, Slide.dim), 1, 1);
		bmap1 = Slide.bmap;
		
		
		Slide.init(file, 200, false);
		Slide.process();
		
		Format.print2D(Slide.bmap);
		
		Window.create("Slide", file, Format.slideArea(Slide.bmap, Slide.vals, Slide.dim), 1, 1);
		bmap2 = Slide.bmap;
		
		bmapfinal = new boolean[bmap1.length][bmap1[0].length];
		for (int r = 0; r < bmapfinal.length; r++)
			for (int c = 0; c < bmapfinal[0].length; c++)
				bmapfinal[r][c] = bmap1[r][c] && bmap2[r][c] ? true : false;
		
		Window.create("Slide", file, Format.slideArea(bmapfinal, Slide.vals, Slide.dim), 1, 1);
	}
	
	public static void image() throws IOException {
		
		int[][] arr = new int[][] {{-2}};
		
		Window.create("Image", "sonar2.png", arr, 1, 1);
	}
}
