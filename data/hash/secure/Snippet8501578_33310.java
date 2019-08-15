/**     
 Description
 * Bean for checking users and passwords.
The password is converted into SHA-256 hash
 and compared with the hash from a database.
 If the check is successful the user is
 redirected to sr.xhtml page */

package com.dx.sr_57;
/** include default packages for Beans */
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
   // or import javax.faces.bean.SessionScoped;
import javax.inject.Named;
/** include package for SHA-256 encryption */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/** SQL Packages */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.annotation.Resource;
   // or import javax.faces.bean.ManagedBean;   


@Named("loginController")
@SessionScoped
public class user_check implements Serializable {
    private String user;
    private String password;    

       public user_check(){
       }

       /** Call the Oracle JDBC Connection driver */
       @Resource(name="java:/Oracle")
       private DataSource ds;

       /** get the content of the variables from the JSF Login page */
       public void setUser(String newValue) { 
           user = newValue; 
       }

       public String getUser(){
           return user;       
       }

       public void setPassword(String newValue) { 
           password = newValue; 
       } 

       public String getPassword(){
           return password;
       }

       /** method for converting simple string into SHA-256 hash */
       public String string_hash(String hash) throws NoSuchAlgorithmException{

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(hash.getBytes());

            byte byteData[] = md.digest();

            /** convert the byte to hex format */
            StringBuilder sb = new StringBuilder();
                for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }              
           return sb.toString();
       }

       /** method for checking password into the Oracle database */
       public String CheckUserDB(String userToCheck) throws SQLException {
            String storedPassword = null;
            if (ds == null) throw new SQLException("No data source");      
       Connection conn = ds.getConnection();
            if (conn == null) throw new SQLException("No connection");      

       try {
            conn.setAutoCommit(false);
            boolean committed = false;
                try {
                       PreparedStatement passwordQuery = conn.prepareStatement(
                            "SELECT passwd from USERS WHERE userz = ?");
                       passwordQuery.setString(1, userToCheck);

                       ResultSet result = passwordQuery.executeQuery();

                       if(result.next()){
                            storedPassword = result.getString("passwd");
                       }

                       conn.commit();
                       committed = true;
                 } finally {
                       if (!committed) conn.rollback();
                       }
            }
                finally {               
                conn.close();

                }      
       return storedPassword;

       }       

       /** compare the user and the password */
       public String user_compare() throws NoSuchAlgorithmException, SQLException { 
            String hash_passwd;           
            String passwdQuery;

            /** check the password into Oracle using the username */
            passwdQuery = CheckUserDB(user);

            /** convert the plain password in SHA-256 hash */
            hash_passwd = string_hash(password);                                  

            if (password.equals(passwdQuery)){      // just for example, non encrypted passwords are compared
                return "success";        
            } else {
                return "failure";               
            }

       }            

}
