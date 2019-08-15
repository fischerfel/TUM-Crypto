package com.baskettracer.dao.impl;

import com.baskettracer.dao.PersonDAO;
import com.baskettracer.entity.Person;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Cornel
 */
@Repository
public class PersonDAOImpl extends AbstractDAOImpl<Person, String> implements PersonDAO {

    protected PersonDAOImpl() {
        super(Person.class);
    }

    @Override
    public void updatePassword(String pw) {
        String hql = "update Person set wachtwoord = :pw";
        Query q = getCurrentSession().createQuery(hql);
        q.setParameter("pw", hash(pw));
    }

    @Override
    public String hash(String s) {

        /*public function encrypt_password($password) {
         $salt = sha1(md5($password));
         $encryptedPassword = md5($password . $salt);

         return $encryptedPassword;*/
        return s;
    }

    @Override
    public String sha1(String s) {
        java.security.MessageDigest d = null;
        try {
            d = java.security.MessageDigest.getInstance("SHA-1");
            d.reset();
            d.update(s.getBytes("UTF-8"));

            return d.digest().toString();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            return null;
        }
    }

    @Override
    public String md5(String s) {
        java.security.MessageDigest d = null;
        try {
            d = java.security.MessageDigest.getInstance("MD5");
            d.reset();
            d.update(s.getBytes("UTF-8"));

            return d.digest().toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            return null;
        }
    }
}
