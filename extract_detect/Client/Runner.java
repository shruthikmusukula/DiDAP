package Client;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.ArrayList;

import Extract.Process;
import Function.ColorEdit;
import Function.Constants;
import Function.Constants.Save;
import Function.Format;
import Function.Util;
import GUI.Window;
import Image.ImageArrEdit;
import MacroProcess.Slide;

/**
 * @author Seth Knoop, Pyojeong Kim, Mitali Chowdhury
 *
 */

public class Runner {
	public static void main(String[] args) throws IOException {
		// test();
		processDir();
	}

	public static void processDir() {
		// Cycle through all files in the directory
		String dir = Constants.in_path;
		ArrayList<String> mstFiles = Util.findAllFiles(dir, "mst");
		ArrayList<String> pngFiles = Util.findAllFiles(dir, "png");

		if (mstFiles.size() > pngFiles.size()) {
			// Extract data from each file
			for (String x : mstFiles) {
				System.out.println("MSTIFF: " + x);
				Process.fileProcess(x, Save.SAVE_FINAL);
			}
			Process.saveMeta();
		} else {
			for (String x : pngFiles) {
				System.out.println("Image: " + x);
				try {
					// Process each file
					slide(x);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void test() throws IOException {
		// for (double t = -6; t < 0; t += 0.1) {
		// test = Math.round(t * 10) / 10.0;
		//// System.out.println(test);
		// for(int i = 1; i < 8; i++)
		// System.out.println(FrameProcess.Master.findMine("D:\\Mitali\\ML\\NPS20\\test\\smol"
		// + i + ".png", true, Save.SAVE_FINAL));
		// }
		// FrameProcess.Master.findMine("sonar3.png", true);
	}

	public static void slide(String file) throws IOException {
		System.out.println(MacroProcess.Master.findMine(file, Save.SAVE_FINAL));
	}

	public static void image() throws IOException {

		int[][] arr = new int[][] { { -2 } };

		Window.create("Image", "sonar2.png", arr, 1, 1, Save.NO_DISPLAY, false);
	}
}
