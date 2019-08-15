@Named(value = "utilisateursManageur")
@SessionScoped
public class UtilisateursManageur implements Serializable {

Utilisateurs currentUser;
String nom;
String mdp;

@EJB
        UtilisateursFacade utilisateursFacade;



public UtilisateursFacade getUtilisateursFacade() {
    return utilisateursFacade;
}

/**
 * Creates a new instance of utilisateursManageur
 */
public void setUtilisateursFacade(UtilisateursFacade utilisateursFacade) {
    this.utilisateursFacade = utilisateursFacade;
}

public UtilisateursManageur() {
}

public Utilisateurs getCurrentUser() {
    return currentUser;
}

public void setCurrentUser(Utilisateurs currentUser) {
    this.currentUser = currentUser;
}

public String getNom() {
    return nom;
}

public void setNom(String nom) {
    this.nom = nom;
}

public String getMdp() {
    return mdp;
}

public void setMdp(String mdp) {
    this.mdp = mdp;
}

public String logout() throws IOException {

    /* Récupération et destruction de la session en cours */
    FacesContext facesContext = FacesContext.getCurrentInstance();
    HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
    session.invalidate();


    return "toLoginFromAll";
}

public String login() throws NoSuchAlgorithmException, NoSuchProviderException{
    // byte[] salt = getSalt();
    currentUser = utilisateursFacade.findOne(nom, get_SHA_256_SecurePassword(mdp));

    if (currentUser.getNomUtil() != null ) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        session.setAttribute("user", currentUser.getNomUtil());
        System.out.println("logic.UtilisateursManageur.login()");
        return "toCompteFromLogin";
    }
    FacesContext context = FacesContext.getCurrentInstance();
    FacesMessage message = new FacesMessage();
    message.setSeverity(FacesMessage.SEVERITY_INFO);
    message.setSummary("Impossible de se connecter. Veuillez vérifier votre identifiants ou votre mot de passe");
    context.addMessage("formOrFieldID", message);
    return "";
}

private static String get_SHA_256_SecurePassword(String passwordToHash)
{
    String generatedPassword = null;
    String salt = "5f8f041b75042e56";
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        // md.update(salt);
        byte[] bytes = md.digest((salt+passwordToHash).getBytes());
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        generatedPassword = sb.toString();
    }
    catch (NoSuchAlgorithmException e)
    {
        e.printStackTrace();
    }
    return generatedPassword;
}
