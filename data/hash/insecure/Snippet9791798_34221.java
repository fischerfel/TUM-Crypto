@Stateless
public class Authenticator {

private String userFullName;
private Long userId;

@EJB
private JpaUserDao userDao;

public Authenticator() {}

public AuthenticationError connect(String username, String password)
        throws Exception {

    String hashed_password = Authenticator.hash(password, "UTF-8");

    User user = null;

    user = userDao.findUserByUsernameAndPassword(username,
            hashed_password);

    if (user == null) {
        return AuthenticationError.UserNotFound;
    }

    this.userFullName = user.toString();
    this.userId = user.getId();

    return AuthenticationError.Success;
}

public Boolean disconnect(HttpSession session) throws Exception {
    try {
        session.invalidate();
        return true;
    } catch (Exception e) {
        return false;
    }

}

public String getUserFullName() {
    return this.userFullName;
}

public Long getUserId() {
    return this.userId;
}

/**
 * 
 * Static method
 * 
 * @throws Exception
 * 
 */

public static String hash(String data, String charset) throws Exception {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.reset();
    md.update(data.getBytes(charset), 0, data.length());
    String hash = new BigInteger(1, md.digest()).toString(16);
    return hash;
}

}
