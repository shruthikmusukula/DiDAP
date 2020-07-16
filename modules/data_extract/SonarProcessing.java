package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import util.Constants;
import util.Util;

/**
 * Main Processing file
 * @author Pyojeong Kim, Mitali Chowdhury, Kaylin Li, Nancy Daoud
 *
 */
public class SonarProcessing {
	public static void main(String[] args) {
		// Cycle through all files in the directory
		String dir = Constants.in_path;
		ArrayList<String> totalFileList = Util.findAllFiles(dir, "mst");

		// Extract data from each file
		for (String x : totalFileList) 
			Extract.fileProcess(x);
		Extract.saveMeta();
		System.out.println("Finished with all");
	}
}
