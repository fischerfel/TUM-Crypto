@ManagedBean
@RequestScoped

public class LoginView implements Serializable {

private static final long serialVersionUID = -2871644685227380013L;
private String username;
private String password;
@EJB
private myApp.ejb.LoginRequest request;
private static final Logger logger =  Logger.getLogger("myApp.ejb.LoginView");

public String getUsername() {
    return username;
}
public void setUsername(String userName) {
    this.username = userName;
}
public String getPassword() {
    return password;
}
public void setPassword(String password) throws  NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(password.getBytes("UTF-8"));
    byte byteData[] = md.digest();
    //convert the byte to hex format
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < byteData.length; i++) {
     sb.append(Integer.toString((byteData[i] & 0xff) + 0x100,  16).substring(1));
    }

    this.password = sb.toString();
}

public void login(ActionEvent event) {
    RequestContext requestContext = RequestContext.getCurrentInstance();
    FacesContext context = FacesContext.getCurrentInstance();
    boolean loggedIn = false;

    User user = request.findUser(username);
    if(user.getPassword().equals(this.getPassword()))
    {
        loggedIn = true;
        context.getExternalContext().getSession(false);
        context.getExternalContext().getSessionMap().put("user", user);
         logger.log(Level.INFO, "put user {0}", 
              new Object[]{user.getUserId()});
         logger.log(Level.INFO, "session id {0}", 
                  new Object[]{context.getExternalContext().getSessionId(false)});
        requestContext.addCallbackParam("loggedIn", loggedIn);
    }
} 

}
