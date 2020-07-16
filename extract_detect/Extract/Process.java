package Extract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import Function.Util;
import Function.Constants.Save;
import Extract.Vars.*;

public class Process {
	
	/**
	 * Initialize for a file
	 * 
	 * @return succesful
	 */
	public static boolean init() {
		Vars.fileHeader = new String[1][8];
		// Creates file header as
		// "Filename Date NavTime FaTime Lat Long WaterDepth TowfishDepth "
		Vars.fileHeader[0][0] = "Filename";
		Vars.fileHeader[0][1] = "Date";
		Vars.fileHeader[0][2] = "NavTime";
		Vars.fileHeader[0][3] = "FaTime";
		Vars.fileHeader[0][4] = "Latitude";
		Vars.fileHeader[0][5] = "Longitude";
		Vars.fileHeader[0][6] = "WaterDepth";
		Vars.fileHeader[0][7] = "TowfishDepth";

		Vars.imTemp = new byte[0][0];
		Vars.metaTemp = new String[0][0];
		Vars.lastFile = "empty";
		Vars.count = 0;

		return true;
	}

	/**
	 * Initialize file
	 * 
	 * @param LoadFile
	 *            String path to load file
	 * @return true if the specified file is an MSTIFF, false if failed attempt
	 */
	public static boolean fileInit(String LoadFile) {
		// Load the file on Input by the size of a file //
		Vars.file = new File(LoadFile);

		FileInputStream Input = null;
		try {
			Input = new FileInputStream(new File(LoadFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Vars.bytes = new byte[(int) Vars.file.length()];

		try {
			Input.read(Vars.bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Check if file is an MSTIFF file by checking that the identifier in the first
		// four bytes is 4C 54 53 4D
		byte[] Identifier_tmp = Arrays.copyOfRange(Vars.bytes, 0, 4);
		String[] Identifier_Hex = new String[4];
		String Identifier = "";
		int i = 0;
		int shiftBy = 0;
		for (i = 0; i < Identifier_tmp.length; i++) { // converts Identifier bytes to Hex array
			Identifier_Hex[i] = Util.toHexFromByte(Identifier_tmp[i]);
		}
		// compiles entries in Hex array to one String
		Identifier = Identifier_Hex[3] + Identifier_Hex[2] + Identifier_Hex[1] + Identifier_Hex[0];

		// if Identifier is correct, then prints that file is a MSTIFF file
		// if Identifier is wrong, then prints out that file is not MSTIFF and skips
		// over the rest of the code in the for loop
		if (!Identifier.equals("4C54534D")) {
			return false;
		}

		// Extract IFD_Address //
		byte[] IFD_Address_tmp = Arrays.copyOfRange(Vars.bytes, 4, 8);
		Vars.IFD_Address = 0;
		i = 0;
		shiftBy = 0;
		for (shiftBy = 0; shiftBy < 32; shiftBy += 8) {
			Vars.IFD_Address |= ((long) (IFD_Address_tmp[i] & 0xff)) << shiftBy;
			i++;
		}

		// Extract HEX from IFD //
		Vars.IFD_byte = Arrays.copyOfRange(Vars.bytes, Vars.IFD_Address, (int) Vars.file.length());
		Vars.IFD_HEX = new String[(int) Vars.file.length() - Vars.IFD_Address];

		for (i = 0; i < Vars.IFD_byte.length; i++) {
			Vars.IFD_HEX[i] = Util.toHexFromByte(Vars.IFD_byte[i]);
		}
		return true;
	}

	/**
	 * Get all image values
	 */
	public static void imageInit() {
		Vars.SonarLines = Extract.getData("0301");
		Vars.BinsPerChannel = Extract.getData("0401");
		Vars.Compression = Extract.getData("FE00");
		Vars.BitsPerBin = 8;
		Vars.right = Extract.getChannel2("2C01");
		Vars.left = Extract.getChannel2("2B01");
	}

	/**
	 * Get all metadata values
	 */
	public static void metaInit() {
		Vars.NavinfoCount = Extract.getData("0A01");
		Vars.FathometerCount = Extract.getData("1E01");
		Vars.Y2K_Address = Extract.getAddress("1D01");
		Vars.Nav_Address = Extract.getAddress("3401");
		Vars.Fatho_Address = Extract.getAddress("2801");
		Extract.Y2KtimeCorrelation();
		Extract.NavInfo6();
		Extract.Fathometer2();
	}


	/**
	 * Save image
	 * @throws IOException 
	 */
	public static void saveIm(Save save) throws IOException {
		if (save == Save.SAVE_ALL) 
			Util.saveIm(Vars.imTemp, "im" + Vars.count, false);
		else 
			MacroProcess.Master.findMine(Util.saveIm(Vars.imTemp, "im" + Vars.count, false), save);
		System.out.println(Vars.count);
		Vars.count++;
	}

	/**
	 * Save metadata as CSV
	 */
	public static void saveMeta() {
		Vars.imTemp = new byte[0][0];
		Util.save(Vars.metaTemp, Vars.lastFile + "_meta");
		Vars.metaTemp = new String[0][0];
	}
	
	/**
	 * Extract and save all data from the file
	 * 
	 * @param x
	 *            file name
	 * @return sucessful
	 */
	public static boolean fileProcess(String x, Save save) {
		// Get mission number from file path
		Vars.y = (Util.remPath(x.substring(0, x.length() - Util.remPath(x).length() - 1)));

		// Check if not MSTIFF
		if (!fileInit(x)) {
			System.out.println(x + " is not an MSTIFF file.");
			return false;
		}

		// Extract data
		imageInit();
		metaInit();

		// Check if first
		if (Vars.imTemp == null)
			init();

		// Save if last of mission
		if (!Vars.y.equals(Vars.lastFile) && !Vars.lastFile.equals("empty"))
			saveMeta();

		// Add all metadata to array
		int i;
		int Data_Count = 0;
		String[][] Data = new String[Vars.Nav_Real_Count][Vars.Num_Meta_Fields + 1];
		for (i = 0; i < Vars.Nav_Real_Count; i++) {
			for (int j = 0; j < Vars.FathometerCount; j++) {
				if (Math.abs(Vars.Nav_timestamp_sec[i] - Vars.Fatho_timestamp_sec[j]) <= 3) {
					Data[Data_Count][0] = "" + x;
					Data[Data_Count][1] = "" + Vars.Y2K_Date;
					Data[Data_Count][2] = Util.JulianToTime(Vars.Nav_timestamp_sec[i]);
					Data[Data_Count][3] = Util.JulianToTime(Vars.Fatho_timestamp_sec[j]);
					Data[Data_Count][4] = "" + Vars.Nav_Lat[i] / 60;
					Data[Data_Count][5] = "" + Vars.Nav_Long[i] / 60;
					Data[Data_Count][6] = "" + Vars.Fatho_WaterDepth[j];
					Data[Data_Count][7] = "" + Vars.Fatho_TowfishDepth[j];
					Data_Count++;
					break;
				}
			}
		}

		// Combine left and right channels, previous image
		if (Vars.imTemp.length != 0) {
			Vars.imTemp = Util.combineVertically(Vars.imTemp, Util.combineHorizontally(Vars.left, Vars.right));
			try {
				saveIm(save);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Vars.imTemp = Util.combineHorizontally(Vars.left, Vars.right);
		} else {
			Vars.imTemp = Util.combineHorizontally(Vars.left, Vars.right);
		}

		// Add metadata to growing table
		if (Vars.metaTemp.length != 0)
			Vars.metaTemp = Util.combineVertically(Vars.metaTemp, Data);
		else
			Vars.metaTemp = Util.combineVertically(Vars.fileHeader, Data);

		Vars.lastFile = Vars.y;
		return true;
	}
}
