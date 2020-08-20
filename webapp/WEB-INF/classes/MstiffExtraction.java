import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ResourceBundle;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* <h1>MSTIFF Extraction Servlet</h1>
* Kicks off a Hadoop job via a shell script defined in mstiff.sh
* that parses through *.mst input files and extracts the important data
* required for the DiDAP Platform.
*/
public class MstiffExtraction extends HttpServlet {

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

        String title = "MSTIFF Extraction";

        // sets title in tab of browser to display the following text
        out.println("<title>" + title + "</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"white\">");

        out.println("<h1>" + title + " Job Status:</h1>\n"); // Page Header
        
        // Builds command to run shell script in provided filepath
		String[] command = {"/bin/bash", "/usr/share/tomcat/webapps/didap/WEB-INF/scripts/mstiff.sh", request.getParameter("directory_name")};
        ProcessBuilder p = new ProcessBuilder(command);
        Process p2 = p.start();
        try {
            p2.waitFor(); // runs scripts and throws exception when shell script fails to execute
        } catch (InterruptedException e) {
        	out.println("Did not execute in hadoop!");
            e.printStackTrace();
        }
        
        // Builds output to display to user from shell script
        BufferedReader br = new BufferedReader(new InputStreamReader(p2.getInputStream()));
        String line;
        //String statusDisplay;
        
		out.println("Output of running " + Arrays.toString(command) + " is: ");
        while ((line = br.readLine()) != null) {
        	out.println(line + "\n");
        	// statusDisplay = line;  ignores long, unneccesary output from jar compilation steps
        }
        out.println("\n");
        // prints last line in input which is job status, either a 1 or 0 for a failure of success, respectively
        out.println("\n" + "Folder named " + request.getParameter("directory_name") +  " was created");

        out.println("</body>");
        out.println("</html>");
    }
}

