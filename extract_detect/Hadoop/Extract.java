import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

/**
 * Extracts image and metadata from MSTIFF 
 *
 */
public class Extract { 
	// Initialize values
	// Extraction variables
	public final static int BITS_PER_HEX_DIGIT = 4;
	public static String y;
	
	
	private static byte[] bytes = SonarImage.bytes;
	private static int bytes_length = SonarImage.bytes_length;
	private static byte[] IFD_byte;
	private static String[] IFD_HEX;
	private static int IFD_Address;
	private static File file;
	private static int SonarLines;
	private static int BinsPerChannel;

	// Metadata
	private static int Num_Meta_Fields = 9;
	// Y2K timestamp
	private static int Y2K_Address;
	private static long Y2K_timestamp = 0;
	private static byte[] Y2K_timestamp_byte_tmp = new byte[4];
	private static long Y2K_Date = 0;
	private static byte[] Y2K_Date_byte_tmp = new byte[4];
	private static long Y2K_Time = 0;
	private static byte[] Y2K_Time_byte_tmp = new byte[4];
	private static long start_time_sec;
	// Nav info
	private static int Nav_Address;
	private static int NavinfoCount;
	private static long[] Nav_timestamp;
	private static byte[] Nav_timestamp_byte_tmp = new byte[4];
	private static long[] Nav_timestamp_sec;
	private static float[] Nav_Lat;
	private static byte[] Nav_Lat_byte_tmp = new byte[4];
	private static int Nav_Lat_tmp;
	private static float[] Nav_Long;
	private static byte[] Nav_Long_byte_tmp = new byte[4];
	private static int Nav_Long_tmp;
	private static float[] Nav_SOG; // Speed over ground (SOG) in knots
	private static byte[] Nav_SOG_byte_tmp = new byte[4];
	private static int Nav_SOG_tmp;
	private static float[] Nav_COG; // Course over ground (COG) in degrees
	private static byte[] Nav_COG_byte_tmp = new byte[4];
	private static int Nav_COG_tmp;
	private static int Nav_Real_Count = 0;
	// Fathometer info
	private static int Fatho_Address;
	private static int FathometerCount;
	private static long[] Fatho_timestamp;
	private static byte[] Fatho_timestamp_byte_tmp = new byte[4];
	private static float[] Fatho_WaterDepth;
	private static byte[] Fatho_WaterDepth_byte_tmp = new byte[4];
	private static int Fatho_WaterDepth_tmp;
	private static float[] Fatho_TowfishDepth;
	private static byte[] Fatho_TowfishDepth_byte_tmp = new byte[4];
	private static int Fatho_TowfishDepth_tmp;
	private static long[] Fatho_timestamp_sec;
	// info from SonarDataInfo3
	private static int SDInfo_Address;
	private static long[] SDInfo_RangeCode; 
	private static byte[] SDInfo_RangeCode_byte_tmp = new byte[4];
	public static int[] SDInfo_RangeMode;

	// Save
	public static byte[][] imTempR;
	public static byte[][] imTempL;
	public static String[][] metaTemp;
	public static String[][] SDInfoData;
	private static String lastFile;
	
	public static int count;

	// Image
	private static int Compression;
	private static int BitsPerBin;
	private static byte[][] right;
	private static byte[][] left;
	private static String[][] fileHeader;
	
	
	//create copy of Util.java to use its methods
//	private static Util utility = new Util();

	/**
	 * Initialize for a file
	 * 
	 * @return succesful
	 */
	public static boolean init() {
		fileHeader = new String[1][Num_Meta_Fields + 1];
		// Creates file header as
		// "Filename Date NavTime FaTime Lat Long WaterDepth TowfishDepth CourseOverGround "
		fileHeader[0][0] = "Filename";
		fileHeader[0][1] = "Date";
		fileHeader[0][2] = "NavTime";
		fileHeader[0][3] = "FaTime";
		fileHeader[0][4] = "Latitude";
		fileHeader[0][5] = "Longitude";
		fileHeader[0][6] = "WaterDepth";
		fileHeader[0][7] = "TowfishDepth";
		fileHeader[0][8] = "SpeedOverGround";
		fileHeader[0][9] = "CourseOverGround";

		imTempR = new byte[0][0];
		imTempL = new byte[0][0];
		metaTemp = new String[0][0];
		lastFile = "empty";
		count = 0;

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
//		file = new File(LoadFile);
//		System.out.println(file.length());
		bytes = SonarImage.bytes;
		bytes_length = SonarImage.bytes_length;
//		System.out.println("bytes.")
//
//		FileInputStream Input = null;
//		try {
//			Input = new FileInputStream(new File(LoadFile));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		bytes = new byte[(int) file.length()];
//
//		try {
//			Input.read(bytes);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		// Check if file is an MSTIFF file by checking that the identifier in the first
		// four bytes is 4C 54 53 4D
		byte[] Identifier_tmp = Arrays.copyOfRange(bytes, 0, 4);
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
		byte[] IFD_Address_tmp = Arrays.copyOfRange(bytes, 4, 8);
		IFD_Address = 0;
		i = 0;
		shiftBy = 0;
		for (shiftBy = 0; shiftBy < 32; shiftBy += 8) {
			IFD_Address |= ((long) (IFD_Address_tmp[i] & 0xff)) << shiftBy;
			i++;
		}

		// Extract HEX from IFD //
		System.out.println("bytes_length = " + bytes_length);
		IFD_byte = Arrays.copyOfRange(bytes, IFD_Address, (int) bytes_length);
		IFD_HEX = new String[(int) bytes_length - IFD_Address];

		for (i = 0; i < IFD_byte.length; i++) {
			IFD_HEX[i] = Util.toHexFromByte(IFD_byte[i]);
		}
		return true;
	}

	/**
	 * Get all image values
	 */
	public static void imageInit() {
		SonarLines = getData("0301");
		BinsPerChannel = getData("0401");
		Compression = getData("FE00");
		BitsPerBin = 8;
		right = getChannel2("2C01");
		left = getChannel2("2B01");
	}

	/**
	 * Get all metadata values
	 */
	public static void metaInit() {
		NavinfoCount = getData("0A01");
		FathometerCount = getData("1E01");
		Y2K_Address = getAddress("1D01");
		Nav_Address = getAddress("3401");
		Fatho_Address = getAddress("2801");
		SDInfo_Address = getAddress("2A01");
		Y2KtimeCorrelation();
		NavInfo6();
		Fathometer2();
		SonarDataInfo3();
	}

	/**
	 * Extract and save all data from the file
	 * 
	 * @param x
	 *            file name
	 * @return sucessful
	 */
	public static boolean fileProcess(String x) throws IOException {
		// Get mission number from file path
//		y = (Util.remPath(x.substring(0, x.length() - Util.remPath(x).length() - 1)));
//		System.out.println("fileProcess() rempath retrieval" + y);
		// Check if not MSTIFF
		y = SonarImage.currentkey;
		if (!Extract.fileInit(x)) {
			System.out.println(x + " is not an MSTIFF file.");
			return false;
		}

		// Extract data
		Extract.imageInit();
		Extract.metaInit();

		// Check if first
		if (imTempR == null)
			init();

		// Save if last of mission
		if (!y.equals(lastFile) && !lastFile.equals("empty"))
			saveMeta();

		// Add all metadata to array
		int i;
		int Data_Count = 0;
		String[][] Data = new String[Nav_Real_Count][Num_Meta_Fields + 1];
		for (i = 0; i < Nav_Real_Count; i++) {
			for (int j = 0; j < FathometerCount; j++) {
				if (Math.abs(Nav_timestamp_sec[i] - Fatho_timestamp_sec[j]) <= 3) {
					Data[Data_Count][0] = "" + x;
					Data[Data_Count][1] = "" + Y2K_Date;
					Data[Data_Count][2] = Util.JulianToTime(Nav_timestamp_sec[i]);
					Data[Data_Count][3] = Util.JulianToTime(Fatho_timestamp_sec[j]);
					Data[Data_Count][4] = "" + Nav_Lat[i] / 60;
					Data[Data_Count][5] = "" + Nav_Long[i] / 60;
					Data[Data_Count][6] = "" + Fatho_WaterDepth[j];
					Data[Data_Count][7] = "" + Fatho_TowfishDepth[j];
					Data[Data_Count][8] = "" + Nav_SOG[i];
					Data[Data_Count][9] = "" + Nav_COG[i];
					Data_Count++;
					break;
				}
			}
		}
		
		SDInfoData = new String[SonarLines][3];
		for (i = 0; i < SonarLines; i++) {
			SDInfoData[i][0] = "" + x;
			try {
				SDInfoData[i][1] = "" + SDInfo_RangeCode[i];
				SDInfoData[i][2] = "" + SDInfo_RangeMode[i];
			} catch(Exception e) {
				System.out.println("Exception at line "+ e.getStackTrace()[0].getLineNumber() + ": "+ e + "i: "+ i);
//				System.out.println("Data_Count: "+ Data_Count);
//				System.out.println("i: "+ i);
			}
		}

		// Combine left and right channels, previous image
		if (imTempR.length != 0) {
			imTempR = Util.combineVertically(imTempR, right);
			imTempL = Util.combineVertically(imTempL, left);
			saveIm();
			imTempR = right;
			imTempL = left;
		} else {
			imTempR = right;
			imTempL = left;
		}

		// Add metadata to growing table
		if (metaTemp.length != 0)
			metaTemp = Util.combineVertically(metaTemp, Data);
		else
			metaTemp = Util.combineVertically(fileHeader, Data);

		lastFile = y;
		return true;
	}

	/**
	 * Save image
	 */
	public static void saveIm() {
		Util.saveIm(Util.combineHorizontally(imTempL, imTempR), "im" + count, false);
		count++;
	}

	/**
	 * Save metadata as CSV
	 */
	public static void saveMeta() throws IOException {
//		Util.saveIm(imTempR, "im" + count);
		imTempR = new byte[0][0];
		imTempL = new byte[0][0];
		String tempLabel = Util.remPath(lastFile);
		Util.save(metaTemp, tempLabel + "META");
		metaTemp = new String[0][0];
	}

	/**
	 * Get a data field
	 * 
	 * @param dTag
	 *            4 character MSTIFF tag
	 * @param Num_Fields
	 *            number of fields
	 */
	public static void getData(String dTag, int Num_Fields) {
		// Find out Value
		String tag = "";

		String Value_Hex = "";
		int Value_Address = 0;
		int i, shiftBy;
		for (i = 0; i < (int) bytes_length - IFD_Address; i = i + 2) {
			tag = IFD_HEX[i] + IFD_HEX[i + 1];

			if (tag.equals(dTag)) {
				Value_Hex = IFD_HEX[i + 11] + IFD_HEX[i + 10] + IFD_HEX[i + 9] + IFD_HEX[i + 8];
				Value_Address = Integer.parseInt(Value_Hex, 16);
			}
		}
		// get info byte
		byte[] Value_info_byte = Arrays.copyOfRange(bytes, Value_Address,
				Value_Address + Num_Fields * BITS_PER_HEX_DIGIT);
		String[] Value_HEX = new String[Num_Fields * BITS_PER_HEX_DIGIT];
		String[] Fields_HEX = new String[Num_Fields];
		int cnt = 0;

		for (int j = 0; j < Num_Fields * BITS_PER_HEX_DIGIT; j++) {
			Value_HEX[j] = Util.toHexFromByte(Value_info_byte[cnt]);
			cnt++;
		}

		for (int j = 0; j < Num_Fields * BITS_PER_HEX_DIGIT; j += BITS_PER_HEX_DIGIT) {
			Fields_HEX[j / BITS_PER_HEX_DIGIT] = Value_HEX[j].toString() + Value_HEX[j + 1].toString()
					+ Value_HEX[j + 2].toString() + Value_HEX[j + 3].toString();
		}

		// Transform HEX -> Binary -> Long //
		long[] data = new long[Num_Fields];
		byte[][] values_byte_tmp = new byte[Num_Fields][BITS_PER_HEX_DIGIT];

		for (int j = 0; j < Num_Fields; j++) {
			values_byte_tmp[j] = Util.toByteFromHex(Fields_HEX[j]);
		}

		for (int j = 0; j < Num_Fields; j++) {
			i = 0;
			for (shiftBy = 0; shiftBy < 32; shiftBy += 8) {
				data[j] |= ((long) (values_byte_tmp[j][i] & 0xff)) << shiftBy;
				i++;
			}
		}
	}

	/**
	 * returns integer value for a field containing less than 4 bytes of data
	 * 
	 * @param dTag
	 *            4 character tag for MSTIFF
	 * @return integer value of specified field
	 */
	public static int getData(String dTag) {
		int i;
		String Value_Hex;
		int value = 0;
		for (i = 0; i < (int) bytes_length - IFD_Address; i = i + 2) {
			String tag = IFD_HEX[i] + IFD_HEX[i + 1];
			if (tag.equals(dTag)) {
				Value_Hex = IFD_HEX[i + 9] + IFD_HEX[i + 8];
				value = Integer.parseInt(Value_Hex, 16);
			}
		}
		return value;
	}

	/**
	 * Returns the address of a data field if there is more than 4 bytes of data
	 * 
	 * @param dTag
	 *            4 character tag for MSTIFF
	 * @return address of data
	 */
	public static int getAddress(String dTag) {
		String Value_Hex;
		int address = 0;
		for (int i = 0; i < (int) bytes_length - IFD_Address; i = i + 2) {
			String tag = IFD_HEX[i] + IFD_HEX[i + 1];
			if (tag.equals(dTag)) {
				Value_Hex = IFD_HEX[i + 11] + IFD_HEX[i + 10] + IFD_HEX[i + 9] + IFD_HEX[i + 8];
				address = Integer.parseInt(Value_Hex, 16);
			}
		}
		return address;
	}

	/**
	 * Extracts and saves the left or right channel of the sonar image
	 * 
	 * @param dTag
	 *            tag for left or right channel field
	 * @return byte array of image
	 */
	public static byte[][] getChannel2(String dTag) {
		int i;
		int Address = 0;
		String TempAddress = "";
		for (i = 0; i < (int) bytes_length - IFD_Address; i = i + 2) {
			String tag = IFD_HEX[i] + IFD_HEX[i + 1];
			if (tag.equals(dTag)) {
				TempAddress = IFD_HEX[i + 11] + IFD_HEX[i + 10] + IFD_HEX[i + 9] + IFD_HEX[i + 8];
				Address = Integer.parseInt(TempAddress, 16);
			}
		}

		byte[] info_byte = Arrays.copyOfRange(bytes, Address, Address + 4 * BinsPerChannel * SonarLines);
		String[] Sonar_HEX = new String[16];
		String[] data_Hex = new String[BinsPerChannel * SonarLines];
		int cnt = 0;
		for (i = 0; i < SonarLines * BinsPerChannel; i++) {
			for (int j = 0; j < 4; j++) {
				Sonar_HEX[j] = Util.toHexFromByte(info_byte[cnt]);
				cnt++;
			}
			data_Hex[i] = Sonar_HEX[0].toString() + Sonar_HEX[1].toString() + Sonar_HEX[2].toString()
					+ Sonar_HEX[3].toString();
		}

		byte[] data = new byte[BinsPerChannel * SonarLines * 4];
		i = 0;
		int count = 0;
		for (int j = 0; j < BinsPerChannel * SonarLines; j++) {
			for (int k = 0; k < 8; k += 2) {
				byte[] temp = Util.toByteFromHex(data_Hex[j].substring(k, k + 2));
				data[count] += temp[0];
				count++;
			}
		}
		cnt = 0;
		byte[][] hold = new byte[SonarLines][BinsPerChannel];

		for (int j = 0; j < SonarLines; j++) {
			for (int k = 0; k < BinsPerChannel; k++) {
				hold[j][k] = data[cnt];
				cnt++;
			}
		}
		return hold;
	}

	// Metadata methods start here

	/**
	 * Initialize Y2K Time Data
	 */
	public static void Y2KtimeCorrelation() {
		byte[] Y2K_info_byte = Arrays.copyOfRange(bytes, Y2K_Address, Y2K_Address + 12);
		String[] Y2K_HEX = new String[12];
		String Y2K_timestamp_Hex = "";
		String Y2K_Date_Hex = "";
		String Y2K_Time_Hex = "";
		int cnt = 0;

		// converts Y2K section of file into hex
		for (int j = 0; j < 12; j++) {
			Y2K_HEX[j] = Util.toHexFromByte(Y2K_info_byte[cnt]);
			cnt++;
		}

		// bytes 0-3 contain the timestamp according to system time, which is time
		// elapsed in milliseconds since Windows was last started
		Y2K_timestamp_Hex = Y2K_HEX[0].toString() + Y2K_HEX[1].toString() + Y2K_HEX[2].toString()
				+ Y2K_HEX[3].toString();
		// bytes 4-7 contain the date
		Y2K_Date_Hex = Y2K_HEX[4].toString() + Y2K_HEX[5].toString() + Y2K_HEX[6].toString() + Y2K_HEX[7].toString();
		// bytes 8-11 contain the time in accordance with US Y2K standards
		Y2K_Time_Hex = Y2K_HEX[8].toString() + Y2K_HEX[9].toString() + Y2K_HEX[10].toString() + Y2K_HEX[11].toString();

		// Y2KtimeCorrelation : Transform HEX -> Binary -> Long //

		int i = 0;
		int shiftBy = 0;

		Y2K_timestamp_byte_tmp = Util.toByteFromHex(Y2K_timestamp_Hex);
		Y2K_Date_byte_tmp = Util.toByteFromHex(Y2K_Date_Hex);
		Y2K_Time_byte_tmp = Util.toByteFromHex(Y2K_Time_Hex);

		// packs the byte arrays into one long for each variable (timestamp, date, time)
		for (shiftBy = 0; shiftBy < 32; shiftBy += 8) {
			Y2K_timestamp |= ((long) (Y2K_timestamp_byte_tmp[i] & 0xff)) << shiftBy;
			Y2K_Date |= ((long) (Y2K_Date_byte_tmp[i] & 0xff)) << shiftBy;
			Y2K_Time |= ((long) (Y2K_Time_byte_tmp[i] & 0xff)) << shiftBy;
			i++;
		}
		start_time_sec = Y2K_Time - Y2K_timestamp / 1000;
	}

	/**
	 * Initialize Nav Info Data
	 */
	public static void NavInfo6() {
		int numOfBytes = 20; // it's (number of fields we want in NavInfo) * (4 bytes)
		
		byte[] Nav_info_byte = Arrays.copyOfRange(bytes, Nav_Address, Nav_Address + 560 * NavinfoCount);
		String[] Nav_HEX = new String[numOfBytes];
		String[] Nav_timestamp_Hex = new String[NavinfoCount];
		String[] Nav_Lat_Hex = new String[NavinfoCount];
		String[] Nav_Long_Hex = new String[NavinfoCount];
		String[] Nav_SOG_Hex = new String[NavinfoCount];
		String[] Nav_COG_Hex = new String[NavinfoCount];
		
		int cnt = 0;
		int i = 0;
		for (i = 0; i < NavinfoCount; i++) {
			for (int j = 0; j < numOfBytes; j++) {
				Nav_HEX[j] = Util.toHexFromByte(Nav_info_byte[cnt]);
				cnt++;
			}
			Nav_timestamp_Hex[i] = Nav_HEX[0].toString() + Nav_HEX[1].toString() + Nav_HEX[2].toString()
					+ Nav_HEX[3].toString();
			Nav_Lat_Hex[i] = Nav_HEX[4].toString() + Nav_HEX[5].toString() + Nav_HEX[6].toString()
					+ Nav_HEX[7].toString();
			Nav_Long_Hex[i] = Nav_HEX[8].toString() + Nav_HEX[9].toString() + Nav_HEX[10].toString()
					+ Nav_HEX[11].toString();
			Nav_SOG_Hex[i] = Nav_HEX[12].toString() + Nav_HEX[13].toString() + Nav_HEX[14].toString()
					+ Nav_HEX[15].toString();
			Nav_COG_Hex[i] = Nav_HEX[16].toString() + Nav_HEX[17].toString() + Nav_HEX[18].toString()
					+ Nav_HEX[19].toString();
			cnt = cnt + (560 - numOfBytes);
		}

		// Navinfo6 : Transform HEX -> Binary -> Long or Float //
		Nav_timestamp = new long[NavinfoCount];
		Nav_Lat = new float[NavinfoCount];
		Nav_Lat_tmp = 0;
		Nav_Long = new float[NavinfoCount];
		Nav_Long_tmp = 0;
		Nav_SOG = new float[NavinfoCount];
		Nav_SOG_tmp = 0;
		Nav_COG = new float[NavinfoCount];
		Nav_COG_tmp = 0;
		Nav_Real_Count = 0;
		i = 0;
		int shiftBy = 0;
		for (int j = 0; j < NavinfoCount; j++) {
			Nav_timestamp_byte_tmp = Util.toByteFromHex(Nav_timestamp_Hex[j]);
			Nav_Lat_byte_tmp = Util.toByteFromHex(Nav_Lat_Hex[j]);
			Nav_Long_byte_tmp = Util.toByteFromHex(Nav_Long_Hex[j]);
			Nav_SOG_byte_tmp = Util.toByteFromHex(Nav_SOG_Hex[j]);
			Nav_COG_byte_tmp = Util.toByteFromHex(Nav_COG_Hex[j]);
			Nav_Lat_tmp = 0;
			Nav_Long_tmp = 0;
			Nav_SOG_tmp = 0;
			Nav_COG_tmp = 0;

			for (shiftBy = 0; shiftBy < 32; shiftBy += 8) {
				Nav_timestamp[j] |= ((long) (Nav_timestamp_byte_tmp[i] & 0xff)) << shiftBy;
				Nav_Lat_tmp |= ((long) (Nav_Lat_byte_tmp[i] & 0xff)) << shiftBy;
				Nav_Long_tmp |= ((long) (Nav_Long_byte_tmp[i] & 0xff)) << shiftBy;
				Nav_SOG_tmp |= ((long) (Nav_SOG_byte_tmp[i] & 0xff)) << shiftBy;
				Nav_COG_tmp |= ((long) (Nav_COG_byte_tmp[i] & 0xff)) << shiftBy;
				i++;
			}
			// stop if abnormal data comes out //
			if (j > 1) {
				if (Nav_timestamp[j - 1] > Nav_timestamp[j]) {
					break;
				}
			}
			Nav_Real_Count++;
			Nav_Lat[j] = Float.intBitsToFloat(Nav_Lat_tmp);
			Nav_Long[j] = Float.intBitsToFloat(Nav_Long_tmp);
			Nav_SOG[j] = Float.intBitsToFloat(Nav_SOG_tmp);
			Nav_COG[j] = Float.intBitsToFloat(Nav_COG_tmp);
			i = 0;
		}

		for (i = 0; i < NavinfoCount; i++) {
			if (Nav_timestamp[i] == 0) {
				break;
			}
		}
		Nav_timestamp_sec = new long[i];
		for (int j = 0; j < i; j++) {
			Nav_timestamp_sec[j] = Nav_timestamp[j] / 1000 + start_time_sec; // Nav_timestamp : millisecond ->
																				// second //
		}
	}

	/**
	 * Initialize fathometer data
	 */
	public static void Fathometer2() {
		byte[] Fatho_info_byte = Arrays.copyOfRange(bytes, Fatho_Address, Fatho_Address + 12 * FathometerCount);
		String[] Fatho_HEX = new String[12];
		String[] Fatho_timestamp_Hex = new String[FathometerCount];
		String[] Fatho_WaterDepth_Hex = new String[FathometerCount];
		String[] Fatho_TowfishDepth_Hex = new String[FathometerCount];
		int cnt = 0;
		int i;
		for (i = 0; i < FathometerCount; i++) {
			for (int j = 0; j < 12; j++) {
				Fatho_HEX[j] = Util.toHexFromByte(Fatho_info_byte[cnt]);
				cnt++;
			}
			Fatho_timestamp_Hex[i] = Fatho_HEX[0].toString() + Fatho_HEX[1].toString() + Fatho_HEX[2].toString()
					+ Fatho_HEX[3].toString();
			Fatho_WaterDepth_Hex[i] = Fatho_HEX[4].toString() + Fatho_HEX[5].toString() + Fatho_HEX[6].toString()
					+ Fatho_HEX[7].toString();
			Fatho_TowfishDepth_Hex[i] = Fatho_HEX[8].toString() + Fatho_HEX[9].toString() + Fatho_HEX[10].toString()
					+ Fatho_HEX[11].toString();
		}

		// Fathometer2 : Transform HEX -> Binary -> Long or Float //
		Fatho_timestamp = new long[FathometerCount];
		Fatho_WaterDepth = new float[FathometerCount];
		Fatho_WaterDepth_tmp = 0;
		Fatho_TowfishDepth = new float[FathometerCount];
		Fatho_TowfishDepth_tmp = 0;

		i = 0;
		int shiftBy;

		for (int j = 0; j < FathometerCount; j++) {
			Fatho_timestamp_byte_tmp = Util.toByteFromHex(Fatho_timestamp_Hex[j]);
			Fatho_WaterDepth_byte_tmp = Util.toByteFromHex(Fatho_WaterDepth_Hex[j]);
			Fatho_TowfishDepth_byte_tmp = Util.toByteFromHex(Fatho_TowfishDepth_Hex[j]);
			Fatho_WaterDepth_tmp = 0;
			Fatho_TowfishDepth_tmp = 0;

			for (shiftBy = 0; shiftBy < 32; shiftBy += 8) {
				Fatho_timestamp[j] |= ((long) (Fatho_timestamp_byte_tmp[i] & 0xff)) << shiftBy;
				Fatho_WaterDepth_tmp |= ((long) (Fatho_WaterDepth_byte_tmp[i] & 0xff)) << shiftBy;
				Fatho_TowfishDepth_tmp |= ((long) (Fatho_TowfishDepth_byte_tmp[i] & 0xff)) << shiftBy;
				i++;
			}
			// stop if abnormal data comes out //
			if (j > 1) {
				if (Fatho_timestamp[j - 1] > Fatho_timestamp[j]) {
					break;
				}
			}
			Fatho_WaterDepth[j] = Float.intBitsToFloat(Fatho_WaterDepth_tmp);
			Fatho_TowfishDepth[j] = Float.intBitsToFloat(Fatho_TowfishDepth_tmp);
			i = 0;
		}

		Fatho_timestamp_sec = new long[FathometerCount];
		for (int j = 0; j < FathometerCount; j++) {
			Fatho_timestamp_sec[j] = Fatho_timestamp[j] / 1000 + start_time_sec; // Fatho_timestamp :
																					// millisecond ->
																					// second //
		}
	}
	/**
	 * Initialize SonarDataInfo3 data
	 */
	public static void SonarDataInfo3() {
		
		int numOfBytes = 8;
		
		// 6 fields * 4 bytes each - 2 because of gain = 6 * 4 - 2 = 22 bytes of info
		byte[] SDInfo_info_byte = Arrays.copyOfRange(bytes, SDInfo_Address, SDInfo_Address + 22 * SonarLines); 
		String[] SDInfo_HEX = new String[12];
		String[] SDInfo_RangeCode_Hex = new String[SonarLines];
		SDInfo_RangeMode = new int[SonarLines];
//		int[] SDInfo_RangeMode_intTmp = new int[SonarLines];
		int cnt = 0;
		int i;
		for (i = 0; i < SonarLines; i++) {
			for (int j = 0; j < numOfBytes; j++) {
				SDInfo_HEX[j] = Util.toHexFromByte(SDInfo_info_byte[cnt]);
				cnt++;
			}
			SDInfo_RangeCode_Hex[i] = SDInfo_HEX[4].toString() + SDInfo_HEX[5].toString() + SDInfo_HEX[6].toString()
					+ SDInfo_HEX[7].toString();
			
			SDInfo_RangeMode[i] = Integer.parseInt(SDInfo_HEX[7].toString()+SDInfo_HEX[6].toString(),16);
			
			if (SDInfo_RangeMode[i] == 1) SDInfo_RangeMode[i] = 5;
			else if (SDInfo_RangeMode[i] == 2) SDInfo_RangeMode[i] = 10;
			else if (SDInfo_RangeMode[i] == 3) SDInfo_RangeMode[i] = 20;
			else if (SDInfo_RangeMode[i] == 4) SDInfo_RangeMode[i] = 50;
			else if (SDInfo_RangeMode[i] == 5) SDInfo_RangeMode[i] = 75;
			else if (SDInfo_RangeMode[i] == 6) SDInfo_RangeMode[i] = 100;
			else if (SDInfo_RangeMode[i] == 7) SDInfo_RangeMode[i] = 150;
			else if (SDInfo_RangeMode[i] == 8) SDInfo_RangeMode[i] = 200;
			else if (SDInfo_RangeMode[i] == 9) SDInfo_RangeMode[i] = 300;
			else if (SDInfo_RangeMode[i] == 10) SDInfo_RangeMode[i] = 500;
			else if (SDInfo_RangeMode[i] == 11) SDInfo_RangeMode[i] = 30;
			else {
				SDInfo_RangeMode[i] = 40;
//				System.out.println("SDInfo_RangeMode[j]" + SDInfo_RangeMode[j]);
			}
			
			try {
				if (i==0) {
					System.out.println("SDInfo_RangeCode_Hex[1]: " + SDInfo_RangeCode_Hex[1]);
					System.out.println("SDInfo_RangeMode[1]: " + SDInfo_RangeMode[1]);
				}
			} catch(Exception e) {
				System.out.println("Exception: " + e);
				System.out.println("i: "+ i);
				System.out.println("cnt: "+ cnt);
			}
			
			cnt = cnt + (22 - numOfBytes);
		}
		
		SDInfo_RangeCode = new long[SonarLines];
//		SDInfo_RangeMode = new long[SonarLines];
		i = 0;
		int shiftBy;
		
		
		for (int j = 0; j < SonarLines; j++) {
			SDInfo_RangeCode_byte_tmp = Util.toByteFromHex(SDInfo_RangeCode_Hex[j]);

			for (shiftBy = 0; shiftBy < 32; shiftBy += 8) {
				SDInfo_RangeCode[j] |= ((long) (SDInfo_RangeCode_byte_tmp[i] & 0xff)) << shiftBy;
//				SDInfo_RangeMode[j] |= ((long) (SDInfo_RangeCode_byte_tmp[i] & 0xf)) << shiftBy; // find the decimal rangemode by masking the rangecode
				// find the sonar range in meters
				
				i++;
			}
			i = 0;
		}
		
	}

}