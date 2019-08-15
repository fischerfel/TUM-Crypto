package com.supinfo.courses.EJB;

import com.supinfo.courses.EJB.local.UserFacadeLocal;
import com.supinfo.courses.entities.User;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


@Stateless
public class UserFacade extends AbstractFacade<User> implements UserFacadeLocal {

    @PersistenceContext(unitName = "4JVA-Courses-ejbPU")
    private EntityManager em;

    public UserFacade(EntityManager em, Class<User> entityClass) {
        super(entityClass);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }

    @Override
    public User findByCredentials(String email, String password) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> from = cq.from(User.class);
        cq.where(cb.equal(from.get("email"), email));
        cq.where(cb.equal(from.get("password"), hashPassword(password)));
        TypedQuery<User> query = getEntityManager().createQuery(cq);
        List<User> resultList = query.getResultList();

        // return first user that match or null
        return (resultList.isEmpty()) ? null : resultList.get(0);
    }


    @Override
    public void create(User entity) {
        // encrypt password (sha1)
        entity.setPassword(hashPassword(entity.getPassword()));
        super.create(entity);
    }


    public String hashPassword(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            byte[] byteHash = crypt.digest();

            try (Formatter formatter = new Formatter()) {
                for (byte b : byteHash)
                    formatter.format("%02x", b);
                sha1 = formatter.toString();
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new Error("Unable to hash password");
        }

        return sha1;
    }

}
