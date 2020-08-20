import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;

import java.io.PrintWriter;

/**
* <h1>DB Main Operations CLass</h1>
* Contains functions to perform Hive Data Warehouse Operations from corresponding servlets.
*/
public class DBMain {
	private static Connection conn;
	private static Statement stmt;
	private static boolean connEstablished = false;
	private static PrintWriter out;

	/**
	   * This method is used to establish the DB connection in Hive
	   * and handles the appropriate exception when an error occurs.
	   */
	public static void DBinit(PrintWriter p) {
		out = p;
		if (!connEstablished) {
			try {
				// Register driver and create driver instance
				String driverName = "com.cloudera.hive.jdbc4.HS1Driver";
				Class.forName(driverName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace(out);
				System.exit(1);
			}
			
			try {
				// Establish connection
				String url = "jdbc:hive://localhost:10000/metadata"; // metadata is the database name within local Hive instance
				conn = DriverManager.getConnection(url, "", ""); // no authentication needed for Hive Server 1 access
				out.println("Connected to Database!");

				// Create statement
				stmt = conn.createStatement();
				connEstablished = true;
			} catch (SQLException e) {
				e.printStackTrace(out);
				System.exit(1);
			}
		}
	}
	
	/**
	   * This method is used to create the metadata table schema using user input
	   * for the proper table name.
	   */
	/*public static void DBCreateTable(String tableName) {
		if (connEstablished) {
			String query = "";
			// create hive table statement
			query = "CREATE TABLE IF NOT EXISTS " + tableName
		+ " (filePath STRING, dateStamp INT,navTime STRING,FathTime STRING,latitude DOUBLE,longitude DOUBLE,waterDepth FLOAT,towfishDepth FLOAT)"
		+ "ROW FORMAT DELIMITED"
		+ "FIELDS TERMINATED BY ','"
		+ "STORED AS TEXTFILE"
		+ "LOCATION '/user/hive/warehouse/metadata.db'";
			out.println("Your query:" + query);
			DBExecuteQuery(query);
		}
	}*/
	
	/**
	   * This method is used to handle any DDL related query, aka CREATE, LOAD, INSERT, etc.
	   */
	public static void DBExecuteQuery(String query) {
		if (connEstablished) {
			try {
				out.println("Running: " + query);
				stmt.execute(query);
				out.println("Finished!");
			} catch (SQLException e) {
				out.println("Query Failed!!");
				e.printStackTrace();
			}
		}
	}

	/**
	   * This method closed the DB Connection once the program has completed execution
	   */
	public static void DBfinish() {
		if (connEstablished) {
			out.println("Connection Closed.");
			try {
				stmt.close();
				conn.close();
				connEstablished = false;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}