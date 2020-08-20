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

/**
* <h1>Metadata Table Creation Servlet</h1>
* Loads the data from HDFS into a Hive table.
* Table must be pre-created my DB Admin.
*/
public class MetadataTableCreation extends HttpServlet {

	/**
	   * This method is used to allow the servlet to support HTTP GET requests. 
	   * This means simply requesting to see the following servlet or webpage via a link.
	   * @param request object that contains the request the client has made of the servlet.
	   * @param response object that contains the response the servlet sends to the client.
	   * @exception IOException some sort of input or output error while the GET request is handled.
	   * @exception ServletException thrown when servlet encounters difficulty handling the GET request.
	   */
    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
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
        DBMain.DBinit(out);
        
        /*
        // Create Table in Hive without help of DB Admin
        DBCreateTable(request.getParameter("table_name"));
        */
        
        // Load HDFS Data
        String query = "LOAD DATA INPATH '/user/cloudera/" + request.getParameter("directory_name") + "/part-m-00000' OVERWRITE INTO TABLE " + request.getParameter("table_name");
        DBMain.DBExecuteQuery(query);
        
        out.println("Table created!");
        
        out.println("</body>");
        out.println("</html>");
        
        // Close DB Connection
        DBMain.DBfinish();
    }
}
