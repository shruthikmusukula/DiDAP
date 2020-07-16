package Client;

import java.io.FileNotFoundException;

import java.io.IOException;

import Function.ColorEdit;
import Function.Format;
import GUI.Window;
import Image.ImageArrEdit;
import MacroProcess.Slide;

public class Runner {

	public static void main(String[] args) throws IOException {

		System.out.println("start");
		slide("imageim4.png");
	}
	
	public static void test() throws IOException {
		FrameProcess.Master.findMine("", true);
//		FrameProcess.Master.findMine("sonar3.png", true);
	}
	
	public static void slide(String file) throws IOException {
		MacroProcess.Master.findMine(file);
	}
	
	public static void image() throws IOException {
		
		int[][] arr = new int[][] {{-2}};
		
		Window.create("Image", "sonar2.png", arr, 1, 1);
	}
}
