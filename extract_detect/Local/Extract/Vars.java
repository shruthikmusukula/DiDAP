package Extract;

import java.io.File;

public class Vars {
	// Initialize values
	// Extraction variables
	public final static int BITS_PER_HEX_DIGIT = 4;
	public static byte[] bytes;
	public static byte[] IFD_byte;
	public static String[] IFD_HEX;
	public static int IFD_Address;
	public static File file;
	public static int SonarLines;
	public static int BinsPerChannel;

	// Metadata
	public static int Num_Meta_Fields = 7;
	// Y2K timestamp
	public static int Y2K_Address;
	public static long Y2K_timestamp = 0;
	public static byte[] Y2K_timestamp_byte_tmp = new byte[4];
	public static long Y2K_Date = 0;
	public static byte[] Y2K_Date_byte_tmp = new byte[4];
	public static long Y2K_Time = 0;
	public static byte[] Y2K_Time_byte_tmp = new byte[4];
	public static long start_time_sec;
	// Nav info
	public static int Nav_Address;
	public static int NavinfoCount;
	public static long[] Nav_timestamp;
	public static byte[] Nav_timestamp_byte_tmp = new byte[4];
	public static long[] Nav_timestamp_sec;
	public static float[] Nav_Lat;
	public static byte[] Nav_Lat_byte_tmp = new byte[4];
	public static int Nav_Lat_tmp;
	public static float[] Nav_Long;
	public static byte[] Nav_Long_byte_tmp = new byte[4];
	public static int Nav_Long_tmp;
	public static int Nav_Real_Count = 0;
	// Fathometer info
	public static int Fatho_Address;
	public static int FathometerCount;
	public static long[] Fatho_timestamp;
	public static byte[] Fatho_timestamp_byte_tmp = new byte[4];
	public static float[] Fatho_WaterDepth;
	public static byte[] Fatho_WaterDepth_byte_tmp = new byte[4];
	public static int Fatho_WaterDepth_tmp;
	public static float[] Fatho_TowfishDepth;
	public static byte[] Fatho_TowfishDepth_byte_tmp = new byte[4];
	public static int Fatho_TowfishDepth_tmp;
	public static long[] Fatho_timestamp_sec;

	// Save
	public static byte[][] imTemp;
	public static String[][] metaTemp;
	public static String lastFile;
	public static String y;
	public static int count;

	// Image
	public static int Compression;
	public static int BitsPerBin;
	public static byte[][] right;
	public static byte[][] left;
	public static String[][] fileHeader;
}
