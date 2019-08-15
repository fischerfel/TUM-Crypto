// Loading required libraries
import java.lang.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import javax.naming.*;
import java.util.Random;
import java.math.*;
import java.security.*;
import java.sql.*;

/**
 * Servlet implementation class test
 */
@WebServlet("/html")
public class html extends HttpServlet{
    private static final long serialVersionUID = 12L;

    public html() {
        super();
        // TODO Auto-generated constructor stub
    }
    private DataSource dataSource;

    public void init() throws ServletException {
        try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            dataSource = (DataSource)envContext.lookup("jdbc/testdb");
            System.out.println("Obtained Cached Data Source ");

        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {

      // Get all parameters from request.

      String source = null;
      String UA = null;
      String id = null;
      String ip = null;

      source = request.getParameter("source");
      UA = request.getHeader("User-Agent");
      //is client behind something?
      ip = request.getHeader("X-FORWARDED-FOR");  
      if (ip == null) {  
           ip = request.getRemoteAddr();  
       }


      Connection conn = null;
      Statement stmt = null;
      int exit = 0;

      // Set response content type
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();

      try{
         // Open a connection
         conn = dataSource.getConnection();
         stmt = conn.createStatement();
         // Execute SQL query
         stmt = conn.createStatement();
         String sql;
         sql =  "SELECT data from db";
         ResultSet rs = stmt.executeQuery(sql);

         rs.next();
         String url = rs.getString("url");
         String url_src = rs.getString("url_src");



    out.println("<html>");
    out.println("<body>");
    out.println("<a href=\"url\">");
    out.println("<img src=\"url_src\"/></a>");
    out.println("</body></html>");


     long time = System.currentTimeMillis()/1000;

     Random randomGenerator = new Random();
     int randomInt = randomGenerator.nextInt(250);
     String toEnc =  ip + time + UA + randomInt; // Value to hash
     MessageDigest mdEnc = MessageDigest.getInstance("MD5"); 
     mdEnc.update(toEnc.getBytes(), 0, toEnc.length());
     id = new BigInteger(1, mdEnc.digest()).toString(16);// Hashed

     sql = "insert request";
     stmt.execute(sql);




         // Clean-up environment
         rs.close();
         stmt.close();
         conn.close();
      }catch(SQLException se){
         //Handle errors for JDBC
         se.printStackTrace();
      }catch(Exception e){
         //Handle errors for Class.forName
         e.printStackTrace();
      }finally{
         //finally block used to close resources
         try{
            if(stmt!=null)
               stmt.close();
         }catch(SQLException se2){
         }// nothing we can do
         try{
            if(conn!=null)
            conn.close();
         }catch(SQLException se){
            se.printStackTrace();
         }//end finally try
      } //end try*/

   }
}
