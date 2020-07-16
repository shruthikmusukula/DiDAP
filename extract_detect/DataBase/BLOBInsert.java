package DataBase;

import java.sql.*;
import java.io.*;

public class BLOBInsert {

	public static void tableSetup(String tableName, boolean first) {
		try {
			if (!first) {
				DBMain.executeQuery("DROP TABLE " + tableName);
			}
			DBMain.executeQuery("CREATE TABLE " + tableName + "(NAME VARCHAR2(55), BINARY_FILE BLOB)");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadFile(String tableName, byte[][] file, String im1, String im2) {
		try {

			// load the binary file into the DB

			byte[] bytes = new byte[file.length * file[0].length];

			int counter = 0;
			for (byte[] b : file) {
				for (byte y : b) {
					bytes[counter] = y;
				}
			}

			Connection DBconn = DBMain.getConn();
			PreparedStatement query_stmt = DBconn.prepareStatement("insert into " + tableName + " values(?,?,?,?)");

			query_stmt.setString(1, "image" + im1 + ".png");
			query_stmt.setBytes(2, bytes);
			query_stmt.setString(3, im1);
			query_stmt.setString(4, im2);
			query_stmt.executeUpdate();
			query_stmt.close();
			System.out.println("binary file loaded into Table ok");

		} catch (Exception exp) {
			System.out.println("Exception = " + exp);
		}
	}
	
	public static void loadFile(String tableName, String file, int file1, int file2) {
		try {

			// load the binary file into the DB

			String binary_file_name = file.trim();
			System.out.println(binary_file_name.length());
			File binary_file = new File(binary_file_name);
			FileInputStream fis = new FileInputStream(binary_file);
			BufferedInputStream bis = new BufferedInputStream(fis);

			byte[] bytes = new byte[(int) binary_file.length()];

			bis.read(bytes);

			Connection DBconn = DBMain.getConn();
			PreparedStatement query_stmt = DBconn.prepareStatement("insert into " + tableName + " values(?,?,?,?)");

			query_stmt.setString(1, binary_file_name);
			query_stmt.setBytes(2, bytes);
			query_stmt.setInt(3, file1);
			query_stmt.setInt(4, file2);
			query_stmt.executeUpdate();
			query_stmt.close();
			System.out.println("binary file loaded into Table ok");

		} catch (Exception exp) {
			System.out.println("Exception = " + exp);
		}
	}
}
