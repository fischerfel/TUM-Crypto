package com.loginpanel.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.*;
import javax.sql.DataSource;

/**
 * Servlet implementation class CustomerController
 */
@WebServlet("/CustomerController")

public class CustomerController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CustomerDbUtil customerDbUtil;
    DataSource dataSource;



    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            // Get the command from the form
            String command = request.getParameter("command");

            // Route the flow accordingly
            switch(command) {
                case "ADD":
                    addCustomer(request, response);
                break;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    private void addCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {

        try {
            // Get the form values
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String country  = request.getParameter("country");

            // Create the Customer class object
            Customer theCustomer = new Customer(firstName, lastName, username, password, country);

            // Call the addCustomer function of the model
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println(theCustomer.getMd5());

            customerDbUtil.addCustomer(theCustomer);            

        } catch(Exception e){
            e.printStackTrace();
        } finally {
            // Redirect to the login page
            //response.sendRedirect("index.jsp");
        }   
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
