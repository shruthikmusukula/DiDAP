package database;

import java.util.ArrayList;

import util.Constants;
import util.Util;

public class Runner {
	private static int counter = 0;
	private static String prefix;

	public static void main(String[] args) {
		runDBSequence("test");
	}

	public static void runDBSequence(String prefix) {
		Runner.prefix = prefix;
		DBMain.DBinit();
		BLOBInsert.tableSetup(prefix + "images", false);
		DBMain.executeQuery("ALTER TABLE " + prefix + "images ADD file1 VARCHAR(50)");
		DBMain.executeQuery("ALTER TABLE " + prefix + "images ADD file2 VARCHAR(50)");
//		DBMain.createTable(prefix + "mines", "filename VARCHAR(50)", false, false);
		ArrayList<String> files = Util.findAllFiles(Constants.in_path, "mst");
		for (String file : files) {
			ExtractDB.fileProcess(file);
			if (ExtractDB.getMetaAvailable())
				saveMetaTable();
			if (ExtractDB.im1 != null)
				saveImage();
		}
		ExtractDB.saveMeta();
		saveMetaTable();
		createTableUniqueFilenames();
		DBMain.createTable(prefix + "idfilenames",
				prefix + "sonarsequence.nextval, Filename FROM " + prefix + "sonarfilenames", false, true);
		DBMain.executeQuery("ALTER TABLE " + prefix + "idfilenames ADD CONSTRAINT " + prefix
				+ "sonarkeyconstraint PRIMARY KEY (nextval)");
		for (int i = 0; i < counter; i++) {
			DBMain.executeQuery("ALTER TABLE " + prefix + "metadata" + i + " ADD numfilename NUMBER");
			DBMain.linkDistinctVals(prefix + "metadata" + i, "filename", "numfilename", prefix + "idfilenames",
					"nextval");
		}
//		DBMain.executeQuery("insert into " + prefix + "mines (numfilename) values (" + filenum + ")");
		DBMain.DBfinish();
	}

	private static void saveMetaTable() {
		String[][] meta = ExtractDB.getMeta();
		DBMain.createTable(prefix + "metadata" + counter, ExtractDB.getHeader(), false, false);
		for (String[] row : meta) {
			// System.out.println(row[0]);
			String values = Util.addSingQuote(row[0]) + ", " + Util.addSingQuote(row[1]) + ", "
					+ Util.addSingQuote(row[2]) + ", " + Util.addSingQuote(row[3]) + ", " + Util.addSingQuote(row[4])
					+ ", " + Util.addSingQuote(row[5]) + ", " + Util.addSingQuote(row[6]) + ", "
					+ Util.addSingQuote(row[7]);
			DBMain.executeQuery("insert into " + prefix + "metadata" + counter
					+ " (Filename, Date0, NavTime, FaTime, Latitude, Longitude, WaterDepth, TowfishDepth) values ("
					+ values + ")");
		}
		counter++;
	}

	private static void saveImage() {
		System.out.println("Saving image");
		BLOBInsert.loadFile(prefix + "images", ExtractDB.getImage(), ExtractDB.im1, ExtractDB.im2);
	}

	private static void createTableUniqueFilenames() {
		String q = "filename FROM " + prefix + "metadata" + 0;
		for (int i = 1; i < counter; i++) {
			q += " UNION SELECT filename FROM " + prefix + "metadata" + i;
		}
		DBMain.createTable(prefix + "sonarfilenames", q, false, true);
		DBMain.executeQuery("DROP SEQUENCE " + prefix + "sonarsequence");
		DBMain.executeQuery("CREATE SEQUENCE " + prefix + "sonarsequence START WITH 1 INCREMENT BY 1");
	}
}
