import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ResourceBundle;
import java.util.Arrays;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MetadataTableCreation extends HttpServlet {
	private Connection conn;
	private Statement stmt;
	private boolean connEstablished = false;
	private PrintWriter out;
	
	public void DBinit() {
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
				String url = "jdbc:hive://localhost:10000/metadata";
				conn = DriverManager.getConnection(url, "", "");
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
	
	public void createTable(String tableName) {
		if (connEstablished) {
			try {
				String query = "";
				// create hive table statement
				query = "CREATE EXTERNAL TABLE IF NOT EXISTS " + tableName
			+ " (filePath STRING, dateStamp INT,navTime STRING,FathTime STRING,latitude DOUBLE,longitude DOUBLE,waterDepth FLOAT,towfishDepth FLOAT)"
			+ "ROW FORMAT DELIMITED"
			+ "FIELDS TERMINATED BY ','"
			+ "STORED AS TEXTFILE"
			+ "LOCATION '/user/cloudera/successfulOutput_fullScript'";
				out.println("Your query:" + query);
				stmt.executeQuery(query); // executeUpdate
			} catch (SQLException e) {
				e.printStackTrace();
				out.println("Query Failed!!");
			}
		}
	}
	
	public void DBExecuteQuery(String query) {
		if (connEstablished) {
			try {
				out.println("Running: " + query);
				stmt.execute(query);
				out.println("Finished!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void DBfinish() {
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

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        out = response.getWriter();
        
        out.println("<!DOCTYPE html><html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\" />");

        String title = "Metadata Table Creation";

        out.println("<title>" + title + "</title>");
        out.println("</head>");

        out.println("<body bgcolor=\"white\">");

        out.println("<h1>" + title + " Status:</h1>\n");
		out.println("Metadata Table Data Being Loaded from HDFS:");
		
		// Create DB Connection
        DBinit();

		// Create Actual Table
		//createTable("florida");
		
		// Display HDFS Data
        String query = "LOAD DATA INPATH '/user/cloudera/successfulOutput_fullScript/part-m-00000' OVERWRITE INTO TABLE florida";
		DBExecuteQuery(query);
		
        out.println("</body>");
        out.println("</html>");
        
        // Close DB Connection
        DBfinish();
    }
}
