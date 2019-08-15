import java.net.*;
import javax.net.ssl.*;
import java.io.*;
import java.security.MessageDigest;
import java.util.Date; 

public class BM_ButtonSearch { 

   public static void main(String[] args) {
      try {
         //String hostname = "paypal.com/cgi-bin/webscr"; //Set as host if live
         String hostname = "sandbox.paypal.com/cgi-bin/webscr"; //Set as host if testing
         // Set variables for PayPal transaction
         String user = "xxxxxxxxxxxxxxxxxxxx"; //Set PayPal API username
         String password = "xxxxxxxxxxx"; //API password, not acct password
         String signature = "xxxxxxxxxxxxxxxxxxx"; //Set unique user signature
         String method = "BMButtonSearch"; //Set Button Manager button type
         String version = "59.0"; //Set Button Manager Version as ##.#
         String startdate = "2012-10-11T00:00:00Z"; //Set start date using YYYY-MM-DDTHH:MM:SSZ
         String enddate = "2012-10-11T00:00:00Z"; //Set end date using YYYY-MM-DDTHH:MM:SSZ

         String requestID = "";
         MessageDigest md5 = MessageDigest.getInstance("MD5");
         byte[] md5summe = md5.digest(new Date().toString().getBytes()); 

         for (int it = 0; it < md5summe.length; it++) {
            String temp = Integer.toHexString(md5summe[it] & 0xFF);
            /*
             * toHexString has the side effect of making stuff like 0x0F
             * only one char F(when it should be '0F') so I check the length
             * of string
             */
            if (temp.length() < 2) {
               temp = "0" + temp;
            }
            requestID += temp.toUpperCase();
         } 

         // Build NVP String (currency string added, declare in variables)
         String paramNVP = "USER=" + user + "&PWD=" + password + "&SIGNATURE="
               + signature + "&METHOD=" + method + "&VERSION=" + version
               + "&STARTDATE=" + startdate + "&ENDDATE=" + enddate; 

         System.out.print("Request sent to PayPal: " + paramNVP); 

         // Create the SSL Socket
         int port = 443;
         InetAddress addr = InetAddress.getByName(hostname);
         SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
         SSLSocket sslsock = (SSLSocket) factory.createSocket(addr, port); 

         // Open Socket Output Stream
         String path = "/";
         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sslsock.getOutputStream(), "UTF8"));

         // Send data as a POST
         bw.write("POST " + path + " HTTP/1.0\r\n"); 

         // Write header info to the socket
         bw.write("Connection: close\r\n");
         bw.write("Content-Type: text/namevalue\r\n");
         bw.write("Content-Length: " + paramNVP.length() + "\r\n");
         bw.write("Host: " + hostname + "\r\n");
         bw.write("X-VPS-REQUEST-ID: " + requestID + "\r\n");
         bw.write("X-VPS-CLIENT-TIMEOUT: 240\r\n");
         // Sets the Payflow client IP variable
         // bw.write("PP_REMOTE_ADDR: 192.168.0.123\r\n");
         bw.write("\r\n"); 

         // Write the NVP String to the Socket
         bw.write(paramNVP);
         // Flush the stream
         bw.flush(); 

         // Read the Socket response
         BufferedReader br = new BufferedReader(new InputStreamReader(
               sslsock.getInputStream()));
         String line;
         while ((line = br.readLine()) != null) {
            System.out.println(line);
         }

         // Close the writer, reader, and the socket
         bw.close();
         br.close();
         sslsock.close();
      } 

      catch (Exception e) {
         e.printStackTrace(System.out);
      }
   } // end main()
}// end class SocketTest 
