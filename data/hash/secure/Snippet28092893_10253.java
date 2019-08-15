package beans;


import java.security.MessageDigest;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author bk-laptop
 */
@ManagedBean(name = "mngsession")
@SessionScoped
public class Session {

    private String userid;
    private String password;

    /**
     * Creates a new instance of Session
     */
    public Session() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() throws ServletException {

        if (this.userid.isEmpty()) {
            FacesMessage message = new FacesMessage("Please enter a user !!!");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);

            FacesContext.getCurrentInstance().addMessage("form-login:usernameInput", message);

            return null;
        }

        if (this.password.isEmpty()){
             FacesMessage message = new FacesMessage("Please enter a password !!!");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage("form-login:usernameInput", message);
            return null;
        }

        //context.addMessage(null,new FacesMessage("Ok"));
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {


            request.login(this.userid, this.password);
        } catch (ServletException e) {

            FacesMessage message = new FacesMessage("Login Failed !!!");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);

            FacesContext.getCurrentInstance().addMessage("form-login:usernameInput", message);

            return null;
        }

        return "/products/List";

    }

    public void logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();

        } catch (ServletException e) {

        }

    }

    private String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }


}
