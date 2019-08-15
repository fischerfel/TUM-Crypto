/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Bernard
 */
@ManagedBean
@SessionScoped
public class Authenticator {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String authenticateUser(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(true);
        session.setMaxInactiveInterval(30);
        Boolean authenticated = (Boolean) session.getAttribute("authenticated");

        Database pgDatabase = new Database();
        Admin foundAdmin = null;
        try {
            foundAdmin = (Admin) pgDatabase.findAdminByUsername(username);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Authenticator.class.getName()).log(Level.SEVERE, null, ex);
        }

        Admin currentAdmin = new Admin();
        currentAdmin.userName = username;
        currentAdmin.password = this.hashPassword((password));
        if (authenticated != null && authenticated != true) {
            if (foundAdmin != null) {
                if (currentAdmin.equals(foundAdmin)) {
                    authenticated = true;
                    session.setAttribute("authenticated", true);
                    return "success";
                } else {
                    authenticated = false;
                    session.setAttribute("authenticated", false);
                    return "failure";
                }
            } else {
                authenticated = false;
                session.setAttribute("authenticated", false);
                return "failure";
            }
        } else {
            session.setAttribute("authenticated", true);
            authenticated = true;
            return "success";
        }
    }

    public String logOut() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extCtx = ctx.getExternalContext();
        Map<String, Object> sessionMap = extCtx.getSessionMap();
        sessionMap.put("authenticated", false);
        return "failure";
    }

    public String hashPassword(String passwordToHash) {
        String hashword = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes());
            BigInteger hash = new BigInteger(1, md5.digest());
            hashword = hash.toString(16);
        } catch (NoSuchAlgorithmException nsae) {
        }
        return hashword;
    }
}
