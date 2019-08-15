package com.pointerunits.service;

import java.util.List;
import org.springframework.orm.hibernate3.HibernateTemplate;
import com.pointerunits.pojo.User;
import java.security.MessageDigest;

public class AuthticateService {
    private HibernateTemplate hibernateTemplate;

    public AuthticateService() {
        // TODO Auto-generated constructor stub
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public boolean verifyUserNameAndPassword(String userName,String password) {
        System.out.println("Inside into service class");
        boolean userStatus = false;
        try {
            @SuppressWarnings("unchecked")
            List<User> userObjs = hibernateTemplate.find("from User u where u.user_name=? and u.user_pwd=?",userName,md5(password.toCharArray()));
            if(userObjs.size() != 0) {
                //System.out.println("User ID : " + userObjs.get(0).getUserId() + ", User name : " + userObjs.get(0).getUserName() + ", Password : " + userObjs.get(0).getPassword());
                userStatus = true;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return userStatus;
    }

    private String md5 (char[] c){
        try{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((new String(c)).getBytes("UTF8"));
        String enc = new String(md.digest());
       // JOptionPane.showMessageDialog(null,enc);
        return enc;
        }
        catch (Exception ex){
        return "";
        }
    }
}
