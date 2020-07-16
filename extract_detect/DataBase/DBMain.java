package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Function.Util;

public class DBMain {
	private static Connection conn;
	private static Statement query_stmt;
	private static boolean connEstablished = false;

	public static void DBinit() {
		if (!connEstablished) {
			try {

				Class.forName("oracle.jdbc.OracleDriver");
				System.out.println("Driver loaded");

				String url = "jdbc:oracle:thin:@//localhost:1521/xe";
				String user = "tal";
				String pwd = "bin27";

				conn = DriverManager.getConnection(url, user, pwd);
				System.out.println("Database Connect ok");

				query_stmt = conn.createStatement();
				connEstablished = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Connection getConn() {
		return connEstablished ? conn : null;
	}

	public static void createTable(String tableName, String statement, boolean first, boolean select) {
		if (connEstablished) {
			try {
				String query = "";  
				if (!first) {
					query = "DROP TABLE " + tableName;
					query_stmt.executeQuery(query);
				}
				query = "CREATE TABLE " + tableName + (!select ? " (" + statement + ")" : " AS SELECT " + statement);
				System.out.println(query);
				query_stmt.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void executeQuery(String query) {
		if (connEstablished) {
			try {
				query_stmt.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void updateFileNameDB(String table, String colName) {
		if (connEstablished) {
			try {
				String query = "";
				query = "SELECT DISTINCT " + colName + " FROM " + table;
				ResultSet rs = query_stmt.executeQuery(query);
				ArrayList<String> uniqueVals = new ArrayList<String>();
				while (rs.next())
					uniqueVals.add(rs.getString(colName));
				for (String val : uniqueVals) {
					query = "UPDATE " + table + " SET " + colName + " = '" + Util.remPath(val) + "' WHERE " + colName
							+ " = '" + val + "'";
					query_stmt.executeQuery(query);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void linkDistinctVals(String mainTable, String mainValue, String newValue, String linkedTable,
			String linkedValue) {
		if (connEstablished) {
			try {
				String query = "";
				query = "SELECT DISTINCT " + mainValue + " FROM " + mainTable;
				ResultSet rs = query_stmt.executeQuery(query);
				ArrayList<String> uniqueVals = new ArrayList<String>();
				while (rs.next())
					uniqueVals.add(rs.getString(mainValue));
				System.out.println(uniqueVals.size());
				for (String val : uniqueVals) {
					query = "UPDATE " + mainTable + " SET " + newValue + " = (select " + linkedValue + " from "
							+ linkedTable + " where " + mainValue + "='" + val + "') " + "WHERE " + mainValue + " = '"
							+ val + "'";
					query_stmt.executeUpdate(query);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void DBfinish() {
		if (connEstablished) {
			System.out.println("Finished updates");

			try {
				query_stmt.close();
				conn.close();
				connEstablished = false;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
