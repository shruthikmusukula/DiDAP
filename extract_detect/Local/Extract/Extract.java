package Extract;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import Function.Constants.Save;
import Function.Util;
import Image.Convert;

/**
 * Extraction functions
 */
public class Extract {
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
		for (i = 0; i < (int) Vars.file.length() - Vars.IFD_Address; i = i + 2) {
			tag = Vars.IFD_HEX[i] + Vars.IFD_HEX[i + 1];

			if (tag.equals(dTag)) {
				Value_Hex = Vars.IFD_HEX[i + 11] + Vars.IFD_HEX[i + 10] + Vars.IFD_HEX[i + 9] + Vars.IFD_HEX[i + 8];
				Value_Address = Integer.parseInt(Value_Hex, 16);
			}
		}
		// get info byte
		byte[] Value_info_byte = Arrays.copyOfRange(Vars.bytes, Value_Address,
				Value_Address + Num_Fields * Vars.BITS_PER_HEX_DIGIT);
		String[] Value_HEX = new String[Num_Fields * Vars.BITS_PER_HEX_DIGIT];
		String[] Fields_HEX = new String[Num_Fields];
		int cnt = 0;

		for (int j = 0; j < Num_Fields * Vars.BITS_PER_HEX_DIGIT; j++) {
			Value_HEX[j] = Util.toHexFromByte(Value_info_byte[cnt]);
			cnt++;
		}

		for (int j = 0; j < Num_Fields * Vars.BITS_PER_HEX_DIGIT; j += Vars.BITS_PER_HEX_DIGIT) {
			Fields_HEX[j / Vars.BITS_PER_HEX_DIGIT] = Value_HEX[j].toString() + Value_HEX[j + 1].toString()
					+ Value_HEX[j + 2].toString() + Value_HEX[j + 3].toString();
		}

		// Transform HEX -> Binary -> Long //
		long[] data = new long[Num_Fields];
		byte[][] values_byte_tmp = new byte[Num_Fields][Vars.BITS_PER_HEX_DIGIT];

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
		for (i = 0; i < (int) Vars.file.length() - Vars.IFD_Address; i = i + 2) {
			String tag = Vars.IFD_HEX[i] + Vars.IFD_HEX[i + 1];
			if (tag.equals(dTag)) {
				Value_Hex = Vars.IFD_HEX[i + 9] + Vars.IFD_HEX[i + 8];
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
		for (int i = 0; i < (int) Vars.file.length() - Vars.IFD_Address; i = i + 2) {
			String tag = Vars.IFD_HEX[i] + Vars.IFD_HEX[i + 1];
			if (tag.equals(dTag)) {
				Value_Hex = Vars.IFD_HEX[i + 11] + Vars.IFD_HEX[i + 10] + Vars.IFD_HEX[i + 9] + Vars.IFD_HEX[i + 8];
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
		for (i = 0; i < (int) Vars.file.length() - Vars.IFD_Address; i = i + 2) {
			String tag = Vars.IFD_HEX[i] + Vars.IFD_HEX[i + 1];
			if (tag.equals(dTag)) {
				TempAddress = Vars.IFD_HEX[i + 11] + Vars.IFD_HEX[i + 10] + Vars.IFD_HEX[i + 9] + Vars.IFD_HEX[i + 8];
				Address = Integer.parseInt(TempAddress, 16);
			}
		}

		byte[] info_byte = Arrays.copyOfRange(Vars.bytes, Address, Address + 4 * Vars.BinsPerChannel * Vars.SonarLines);
		String[] Sonar_HEX = new String[16];
		String[] data_Hex = new String[Vars.BinsPerChannel * Vars.SonarLines];
		int cnt = 0;
		for (i = 0; i < Vars.SonarLines * Vars.BinsPerChannel; i++) {
			for (int j = 0; j < 4; j++) {
				Sonar_HEX[j] = Util.toHexFromByte(info_byte[cnt]);
				cnt++;
			}
			data_Hex[i] = Sonar_HEX[0].toString() + Sonar_HEX[1].toString() + Sonar_HEX[2].toString()
					+ Sonar_HEX[3].toString();
		}

		byte[] data = new byte[Vars.BinsPerChannel * Vars.SonarLines * 4];
		i = 0;
		int count = 0;
		for (int j = 0; j < Vars.BinsPerChannel * Vars.SonarLines; j++) {
			for (int k = 0; k < 8; k += 2) {
				byte[] temp = Util.toByteFromHex(data_Hex[j].substring(k, k + 2));
				data[count] += temp[0];
				count++;
			}
		}
		cnt = 0;
		byte[][] hold = new byte[Vars.SonarLines][Vars.BinsPerChannel];

		for (int j = 0; j < Vars.SonarLines; j++) {
			for (int k = 0; k < Vars.BinsPerChannel; k++) {
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
		byte[] Y2K_info_byte = Arrays.copyOfRange(Vars.bytes, Vars.Y2K_Address, Vars.Y2K_Address + 12);
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

		Vars.Y2K_timestamp_byte_tmp = Util.toByteFromHex(Y2K_timestamp_Hex);
		Vars.Y2K_Date_byte_tmp = Util.toByteFromHex(Y2K_Date_Hex);
		Vars.Y2K_Time_byte_tmp = Util.toByteFromHex(Y2K_Time_Hex);

		// packs the byte arrays into one long for each variable (timestamp, date, time)
		for (shiftBy = 0; shiftBy < 32; shiftBy += 8) {
			Vars.Y2K_timestamp |= ((long) (Vars.Y2K_timestamp_byte_tmp[i] & 0xff)) << shiftBy;
			Vars.Y2K_Date |= ((long) (Vars.Y2K_Date_byte_tmp[i] & 0xff)) << shiftBy;
			Vars.Y2K_Time |= ((long) (Vars.Y2K_Time_byte_tmp[i] & 0xff)) << shiftBy;
			i++;
		}
		Vars.start_time_sec = Vars.Y2K_Time - Vars.Y2K_timestamp / 1000;
	}

	/**
	 * Initialize Nav Info Data
	 */
	public static void NavInfo6() {
		byte[] Nav_info_byte = Arrays.copyOfRange(Vars.bytes, Vars.Nav_Address, Vars.Nav_Address + 560 * Vars.NavinfoCount);
		String[] Nav_HEX = new String[12];
		String[] Nav_timestamp_Hex = new String[Vars.NavinfoCount];
		String[] Nav_Lat_Hex = new String[Vars.NavinfoCount];
		String[] Nav_Long_Hex = new String[Vars.NavinfoCount];
		int cnt = 0;
		int i = 0;
		for (i = 0; i < Vars.NavinfoCount; i++) {
			for (int j = 0; j < 12; j++) {
				Nav_HEX[j] = Util.toHexFromByte(Nav_info_byte[cnt]);
				cnt++;
			}
			Nav_timestamp_Hex[i] = Nav_HEX[0].toString() + Nav_HEX[1].toString() + Nav_HEX[2].toString()
					+ Nav_HEX[3].toString();
			Nav_Lat_Hex[i] = Nav_HEX[4].toString() + Nav_HEX[5].toString() + Nav_HEX[6].toString()
					+ Nav_HEX[7].toString();
			Nav_Long_Hex[i] = Nav_HEX[8].toString() + Nav_HEX[9].toString() + Nav_HEX[10].toString()
					+ Nav_HEX[11].toString();
			cnt = cnt + 548;
		}

		// Navinfo6 : Transform HEX -> Binary -> Long or Float //
		Vars.Nav_timestamp = new long[Vars.NavinfoCount];
		Vars.Nav_Lat = new float[Vars.NavinfoCount];
		Vars.Nav_Lat_tmp = 0;
		Vars.Nav_Long = new float[Vars.NavinfoCount];
		Vars.Nav_Long_tmp = 0;
		Vars.Nav_Real_Count = 0;
		i = 0;
		int shiftBy = 0;
		for (int j = 0; j < Vars.NavinfoCount; j++) {
			Vars.Nav_timestamp_byte_tmp = Util.toByteFromHex(Nav_timestamp_Hex[j]);
			Vars.Nav_Lat_byte_tmp = Util.toByteFromHex(Nav_Lat_Hex[j]);
			Vars.Nav_Long_byte_tmp = Util.toByteFromHex(Nav_Long_Hex[j]);
			Vars.Nav_Lat_tmp = 0;
			Vars.Nav_Long_tmp = 0;

			for (shiftBy = 0; shiftBy < 32; shiftBy += 8) {
				Vars.Nav_timestamp[j] |= ((long) (Vars.Nav_timestamp_byte_tmp[i] & 0xff)) << shiftBy;
				Vars.Nav_Lat_tmp |= ((long) (Vars.Nav_Lat_byte_tmp[i] & 0xff)) << shiftBy;
				Vars.Nav_Long_tmp |= ((long) (Vars.Nav_Long_byte_tmp[i] & 0xff)) << shiftBy;
				i++;
			}
			// stop if abnormal data comes out //
			if (j > 1) {
				if (Vars.Nav_timestamp[j - 1] > Vars.Nav_timestamp[j]) {
					break;
				}
			}
			Vars.Nav_Real_Count++;
			Vars.Nav_Lat[j] = Float.intBitsToFloat(Vars.Nav_Lat_tmp);
			Vars.Nav_Long[j] = Float.intBitsToFloat(Vars.Nav_Long_tmp);
			i = 0;
		}

		for (i = 0; i < Vars.NavinfoCount; i++) {
			if (Vars.Nav_timestamp[i] == 0) {
				break;
			}
		}
		Vars.Nav_timestamp_sec = new long[i];
		for (int j = 0; j < i; j++) {
			Vars.Nav_timestamp_sec[j] = Vars.Nav_timestamp[j] / 1000 + Vars.start_time_sec; 
			// Nav_timestamp : millisecond ->second 
		}
	}

	/**
	 * Initialize fathometer data
	 */
	public static void Fathometer2() {
		byte[] Fatho_info_byte = Arrays.copyOfRange(Vars.bytes, Vars.Fatho_Address, Vars.Fatho_Address + 12 * Vars.FathometerCount);
		String[] Fatho_HEX = new String[12];
		String[] Fatho_timestamp_Hex = new String[Vars.FathometerCount];
		String[] Fatho_WaterDepth_Hex = new String[Vars.FathometerCount];
		String[] Fatho_TowfishDepth_Hex = new String[Vars.FathometerCount];
		int cnt = 0;
		int i;
		for (i = 0; i < Vars.FathometerCount; i++) {
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
		Vars.Fatho_timestamp = new long[Vars.FathometerCount];
		Vars.Fatho_WaterDepth = new float[Vars.FathometerCount];
		Vars.Fatho_WaterDepth_tmp = 0;
		Vars.Fatho_TowfishDepth = new float[Vars.FathometerCount];
		Vars.Fatho_TowfishDepth_tmp = 0;

		i = 0;
		int shiftBy;

		for (int j = 0; j < Vars.FathometerCount; j++) {
			Vars.Fatho_timestamp_byte_tmp = Util.toByteFromHex(Fatho_timestamp_Hex[j]);
			Vars.Fatho_WaterDepth_byte_tmp = Util.toByteFromHex(Fatho_WaterDepth_Hex[j]);
			Vars.Fatho_TowfishDepth_byte_tmp = Util.toByteFromHex(Fatho_TowfishDepth_Hex[j]);
			Vars.Fatho_WaterDepth_tmp = 0;
			Vars.Fatho_TowfishDepth_tmp = 0;

			for (shiftBy = 0; shiftBy < 32; shiftBy += 8) {
				Vars.Fatho_timestamp[j] |= ((long) (Vars.Fatho_timestamp_byte_tmp[i] & 0xff)) << shiftBy;
				Vars.Fatho_WaterDepth_tmp |= ((long) (Vars.Fatho_WaterDepth_byte_tmp[i] & 0xff)) << shiftBy;
				Vars.Fatho_TowfishDepth_tmp |= ((long) (Vars.Fatho_TowfishDepth_byte_tmp[i] & 0xff)) << shiftBy;
				i++;
			}
			// stop if abnormal data comes out //
			if (j > 1) {
				if (Vars.Fatho_timestamp[j - 1] > Vars.Fatho_timestamp[j]) {
					break;
				}
			}
			Vars.Fatho_WaterDepth[j] = Float.intBitsToFloat(Vars.Fatho_WaterDepth_tmp);
			Vars.Fatho_TowfishDepth[j] = Float.intBitsToFloat(Vars.Fatho_TowfishDepth_tmp);
			i = 0;
		}

		Vars.Fatho_timestamp_sec = new long[Vars.FathometerCount];
		for (int j = 0; j < Vars.FathometerCount; j++) {
			Vars.Fatho_timestamp_sec[j] = Vars.Fatho_timestamp[j] / 1000 + Vars.start_time_sec; 
			// Fatho_timestamp : millisecond -> second //
		}
	}
}
