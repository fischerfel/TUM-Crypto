package com.bpl.loginservlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class LoginBPLServler
 */
public class LoginBPLServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String SALT = "my-salt-text";

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str = null;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        JSONObject jObj = new JSONObject(sb.toString());


        String username = jObj.getString("username");
        String unHashedPassword = jObj.getString("password");
        //signup(username, unHashedPassword);
        //response.sendRedirect("/BPL2016/second.html");

        // login should succeed.
        if (login(username, unHashedPassword))
        {
            System.out.println("user login successfull.");


            /*String successMessage ="Login Success. Redirecting ...";
            response.setContentType("text/plain");
            response.getWriter().write(successMessage);*/
            response.sendRedirect("http:/localhost:8080/BPL2016/second.html");
        }

        else
        {



        String error ="Invalid UserName or Password";
        response.setContentType("text/plain");
        response.getWriter().write(error);


        }

    }


    public void signup(String username, String password) {
    String saltedPassword = SALT + password;
    String hashedPassword = generateHash(saltedPassword);
    insertIntoDB(username,hashedPassword);
}

public void insertIntoDB(String username,String hashedPassword)
{
     Connection con;
        PreparedStatement pst;


            try{

                //MAKE SURE YOU KEEP THE mysql_connector.jar file in java/lib folder
                //ALSO SET THE CLASSPATH
                Class.forName("com.mysql.jdbc.Driver");
                con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bpl","root","root");
                            pst=con.prepareStatement("insert into tb_bpl_user_login(username,password) values(?,?)");
                            pst.setString(1, username);
                            pst.setString(2, hashedPassword);
                            pst.executeUpdate();


               }
            catch (Exception e) 
            {
                System.out.println(e);
            }


}
public Boolean login(String username, String password) {
    Boolean isAuthenticated = false;

    // remember to use the same SALT value use used while storing password
    // for the first time.
    String saltedPassword = SALT + password;
    String hashedPassword = generateHash(saltedPassword);
    System.out.println(hashedPassword);
    String storedPasswordHash = getFromDB(username);
    if(hashedPassword.equals(storedPasswordHash)){
        isAuthenticated = true;
    }else{
        isAuthenticated = false;
    }
    return isAuthenticated;
}

public String getFromDB(String username)
{
    Connection con;
    PreparedStatement pst;
    String storedPassword = "";
    ResultSet rs;
    try
    {
     Class.forName("com.mysql.jdbc.Driver");
     con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bpl","root","root");
                 pst=con.prepareStatement("select password from tb_bpl_user_login where username =?");
                 pst.setString(1, username);
                rs = pst.executeQuery();
                while(rs.next())
                {
                    storedPassword = rs.getString("password");
                    break;
                }


    }
    catch(Exception e)
    {
        System.out.println(e.getMessage());
    }
    return storedPassword;
}
public static String generateHash(String input) {
    StringBuilder hash = new StringBuilder();

    try {
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        byte[] hashedBytes = sha.digest(input.getBytes());
        char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        for (int idx = 0; idx < hashedBytes.length; ++idx) {
            byte b = hashedBytes[idx];
            hash.append(digits[(b & 0xf0) >> 4]);
            hash.append(digits[b & 0x0f]);
        }
    } catch (NoSuchAlgorithmException e) {
        // handle error here.
    }

    return hash.toString();
}


}
