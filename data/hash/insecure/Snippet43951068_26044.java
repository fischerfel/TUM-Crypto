package com.loginpanel.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class LoginController
 */

@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Resource(name="jdbc/login_module")
    DataSource datasource;
    private CustomerDbUtil customerDbUtil;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            // Get the form data 
            String username = request.getParameter("username");
            String md5 = md5(request.getParameter("password"));

            Customer theCustomer = validate(username, md5); 

        } catch (Exception e) {
            throw new ServletException(); // Throws this exception
        }

    }


    private Customer validate(String username, String md5) throws Exception {

        Customer theCustomer;
        System.out.println("inside validate"); // Gets printed in the console

        try {

            theCustomer = customerDbUtil.selectCustomer(username, md5); // Throws Exception at this statement
            System.out.println("inside try"); // Not printed in the console

            if(theCustomer==null) {
                return null;
            } 

        } catch(Exception e) {
                throw new ServletException();
        }
        return theCustomer;

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Redirect back to the login page as the url does not support direct page access
        RequestDispatcher dispatcher = request.getRequestDispatcher("./");
        request.setAttribute("message", "You need to login first");
        dispatcher.forward(request, response);

    }



    // Hash the string to MD5
    private String md5(String str) throws Exception {

        String plainText = str;
        StringBuffer hexString = new StringBuffer();

        if(plainText!=null) {
            MessageDigest mdAlgorithm = MessageDigest.getInstance("MD5");
            mdAlgorithm.update(plainText.getBytes());

            byte[] digest = mdAlgorithm.digest();

            for (int i = 0; i < digest.length; i++) {
                plainText = Integer.toHexString(0xFF & digest[i]);

                if (plainText.length() < 2) {
                    plainText = "0" + plainText;
                }

                hexString.append(plainText);
            }
        }
        return hexString.toString();
    }

}
