@Stateless
@Named
public class LoginBean {

@EJB
private UserFacadeREST ejbRef;

private String email;
private String password;

public String getEmail() {
    return this.email;
}

public void setEmail(String email) {
    this.email = email;
}

public String getPassword() {
    return this.password;
}

public void setPassword(String password) {
    this.password = password;
}

public String login() {
    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest)
                                context.getExternalContext().getRequest();

    User user = getUser(this.email);

    if (user != null) {
        hashPassword();
        context.addMessage(null, new FacesMessage(this.email));
        context.addMessage(null, new FacesMessage(this.password));

        context.addMessage(null, new FacesMessage(user.getEmail()));
        context.addMessage(null, new FacesMessage(user.getPassword()));

        if (user.getPassword().equals(this.password)) {
            try {
                request.login(this.email, this.password);
            } catch (ServletException e) {
                context.addMessage(null, new FacesMessage("Login failed."));
                return "error";
            }
            return "admin/index";

        } else {
            context.addMessage(null, new FacesMessage("Incorrect password"));
        }

    } else {
        context.addMessage(null, new FacesMessage("There is no account "
                                                    + "with that email"));
    }
    return "error";
}

public void logout() {
    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest)
                                context.getExternalContext().getRequest();
    try {
        request.logout();
    } catch (ServletException e) {
        context.addMessage(null, new FacesMessage("Logout failed."));
    }
}

private User getUser(String email) {
    try {
        User user = (User) getEntityManager().
                            createNamedQuery("User.findByEmail").
                            setParameter("email", email).
                            getSingleResult();
        return user;
    } catch (NoResultException e) {
        System.err.println("NoResultException" + e.getMessage());
        return null;
    }
}

public EntityManager getEntityManager() {
    return ejbRef.getEntityManager();
}

/*
 * md5 password. 
 */
public void hashPassword(){
    try {
        byte[] hash = this.password.getBytes();
        MessageDigest md = MessageDigest.getInstance("md5");
        hash = md.digest(hash);
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0").append(Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }

        String md5Password = hexString.toString();
        setPassword(md5Password);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
    }
}
