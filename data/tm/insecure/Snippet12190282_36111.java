/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LBS;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import javax.net.*;
import javax.net.ssl.*;
import java.security.cert.*;


/**
 *
 * @author Ruwan
 */
public class LBS2 extends HttpServlet {

/** 
 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
 * @param request servlet request
 * @param response servlet response
 * @throws ServletException if a servlet-specific error occurs
 * @throws IOException if an I/O error occurs
 */
protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();
    try {
        LBS2 s=new LBS2();
        s.myReq();

    } finally {            
        out.close();
    }
}



public void myReq(){
  System.setProperty("https.proxyHost", "10.48.242.90");
  System.setProperty("https.proxyPort", "3128");
        String uri = "https://somthing.com/abc?username=USERNAME&password=PASWORD";

    try{
        SSLContext sslctx = SSLContext.getInstance("SSL");
        sslctx.init(null, new X509TrustManager[] { new MyTrustManager()}, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslctx.getSocketFactory());
        URL url = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.connect();
        if (con.getResponseCode() == HttpsURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new
            InputStreamReader(con.getInputStream()));
            String line;
            while((line = br.readLine()) != null) {
            System.out.println(line);
            }
            br.close();
        }
        con.disconnect();

        }
        catch(Exception e){
        System.out.println(e.toString());
        }
}


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the +     sign on the left to edit the code.">
    /** 
 * Handles the HTTP <code>GET</code> method.
 * @param request servlet request
 * @param response servlet response
 * @throws ServletException if a servlet-specific error occurs
 * @throws IOException if an I/O error occurs
 */
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    processRequest(request, response);
}

/** 
 * Handles the HTTP <code>POST</code> method.
 * @param request servlet request
 * @param response servlet response
 * @throws ServletException if a servlet-specific error occurs
 * @throws IOException if an I/O error occurs
 */
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    processRequest(request, response);
}

/** 
 * Returns a short description of the servlet.
 * @return a String containing servlet description
 */
@Override
public String getServletInfo() {
    return "Short description";
}// </editor-fold>
}


class MyTrustManager implements X509TrustManager {
        @Override
            public void checkClientTrusted(X509Certificate[] chain, String
            authType) {
            }

        @Override
            public void checkServerTrusted(X509Certificate[] chain, String
            authType) {
            }

        @Override
            public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
 }    
