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

public class MstiffExtraction extends HttpServlet {

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

        out.println("<title>" + title + "</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"white\">");

        out.println("<h1>" + title + " Job Status:</h1>\n");
        
		String[] command = {"/bin/bash", "/usr/share/tomcat/webapps/didap/WEB-INF/scripts/mstiff.sh"};
        ProcessBuilder p = new ProcessBuilder(command);
        Process p2 = p.start();
        try {
            p2.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
			System.out.println("Did not execute in hadoop!");
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(p2.getInputStream()));
        String line;
        
		out.println("Output of running " + Arrays.toString(command) + " is: ");
        while ((line = br.readLine()) != null) {
            out.println(line + "\n");
        }

        out.println("</body>");
        out.println("</html>");
    }
}

