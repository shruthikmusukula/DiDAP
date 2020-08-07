import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* <h1>Hello World Servlet</h1>
* The HelloWorld Program simple displays a Hello, World! heading onto the webpage
* and into the corresponding tab of a browser.
*/
public class HelloWorld extends HttpServlet {
	
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

        // classic HTML Code below
        out.println("<!DOCTYPE html><html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\" />");

        String title = "Hello, World!";

        // sets title in tab of browser to display the following text
        out.println("<title>" + title + "</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"white\">");
        
        // displays "Hello, World!" as a large heading on a very simple html page
        out.println("<h1>" + title + ", I'm " + request.getParameter("data") + "</h1>");
        out.println("</body>");
        out.println("</html>");
    }
}



