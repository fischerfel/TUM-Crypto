package com.prime.controller;

import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import com.prime.util.DateUtility;

/**
 * Login Controller class allows only authenticated users to log in to the web
 * application.
 *
 * @author prime
 */
@Named
@SessionScoped
public class LoginController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private transient Logger logger;

    @Inject
    FacesContext facesContext;

    @NotNull
    private String username;

    @NotNull
    private String password;

    private String email; 

    /**
     * Creates a new instance of LoginController
     */
    public LoginController() {
    }

    //  Getters and Setters
    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Listen for button clicks on the #{loginController.login} action,
     * validates the username and password entered by the user and navigates to
     * the appropriate page.
     *
     * @param actionEvent
     */
    public void login(ActionEvent actionEvent) {

        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        try {

            try{
                MessageDigest md = java.security.MessageDigest.getInstance("MD5");
                md.update(password.getBytes("UTF-8"));
                byte[] passwordDigest = md.digest();
                @SuppressWarnings("restriction")
                String encodedPasswordHash = new sun.misc.BASE64Encoder().encode(passwordDigest);
                logger.log(Level.INFO, "Encripted Password {0}" ,encodedPasswordHash);
            } catch(Exception e){
                e.printStackTrace();
            }

            // Checks if username and password are valid if not throws a ServletException
            request.login(username, password);
            // gets the user principle and navigates to the appropriate page
            Principal principal = request.getUserPrincipal();

            String navigateString = "/user/expirepage.xhtml"; 

            try {
                logger.log(Level.INFO, "User ({0}) loging in #" + DateUtility.getCurrentDateTime(), request.getUserPrincipal().getName());
                facesContext.getExternalContext().redirect(request.getContextPath() + navigateString);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "IOException, Login Controller" + "Username : " + principal.getName(), ex);

            }
        } catch (ServletException e) {            
            logger.log(Level.SEVERE, "Login Controller: The username or password you provided does not match our records.");            
            facesContext.validationFailed();
        }
    }

    /**
     * Listen for logout button clicks on the #{loginController.logout} action
     * and navigates to login screen.
     */
    public void logout(ActionEvent actionEvent) {

        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        logger.log(Level.INFO, "User ({0}) loging out #" + DateUtility.getCurrentDateTime(), request.getUserPrincipal().getName());
        if (session != null) {
            session.invalidate();
        }
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, "/Login.xhtml?faces-redirect=true");

    }


}
