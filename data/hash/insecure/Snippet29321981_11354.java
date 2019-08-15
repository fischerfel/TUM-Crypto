public class LoginDAOImpl implements LoginDAO{

private String username;
private String password;

private SessionFactory sessionFactory;

public void setSessionFactory(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
}

@Override
public boolean login(String username, String password) throws BusinessException{
    Query query = sessionFactory.getCurrentSession().createQuery(
            "SELECT u FROM User u WHERE u.username=:un");
    query.setParameter("un", username);
    if(query.list().size()==0)throw new BusinessException("No user in database!");
    User user = (User)(query.list().get(0));
    return getHashMD5(password).equals(user.getPassword());
}

@Override
public boolean register(String username, String password, UserType type) throws BusinessException{
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
}

public static String getHashMD5(String string) throws BusinessException{
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        BigInteger bi = new BigInteger(1, md.digest(string.getBytes()));
        return bi.toString(16);
    } catch (Exception ex) {
        throw new BusinessException(ex.getMessage());
    }
}

}
