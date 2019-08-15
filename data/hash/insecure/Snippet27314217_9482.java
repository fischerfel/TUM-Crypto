package beans.controller;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;

@ManagedBean(value="loginBean")
@SessionScoped
public class LoginBean implements Serializable{

    /**
     * attribute that will contain the password input by the user
     */
    private String password;
    /**
     * attribute that will contain error message
     */
    private String message;
    /**
     * attribute that will contain the user email input by the user
     */
    private String email;
    /**
     * attribute that will contain the hash of password input by the user
     * We remember that for security reasons password are stored in the DB in way that 
     * it can't be restored for any reasons
     */
    private String hash_password;

    /** 
     * getter method for attribute hash_password 
     * @return hash_password
     */
    public String getHash_password() {
        return hash_password;
    }

    /** 
     * setter method for hash_password
     * @param hash_password 
     */
    public void setHash_password(String hash_password) {
        this.hash_password = hash_password;
    }

    /**
     * getter method for attribute password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * setter method for attribute password
     * @param password 
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * getter method for attribute message
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * setter methos for attribute message
     * @param message 
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * getter method for attribute email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * setter method for attribute email
     * @param email 
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Method that checks if the user exists inside the users database
     * and it creates a new session
     * @return String: it returns the page where we have to be readdressed
     */
    public String login(){
        //I check that the user exists inside the users database
        boolean result=UserEJB.login(email,password);

        if(result){   
            return "home";
        }else{
            return "login";
        }
    }

    /** 
     * MD5 algorithm for encrypting password. We provide a clear password as input
     * and as output we get a hashed function
     * @param message
     * @throws NoSuchAlgorithmException 
     */
    private void MD5(String password) throws UnsupportedEncodingException
    {
        try {
            MessageDigest m=MessageDigest.getInstance("MD5");
            m.update(password.getBytes(),0,password.length());
            System.out.println("MD5: "+new BigInteger(1,m.digest()).toString(16));

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
