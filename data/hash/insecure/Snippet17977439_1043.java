package com.dataart.mediaportal.controller.bean;

import com.dataart.mediaportal.dao.impl.UserDAOImpl;
import com.dataart.mediaportal.model.User;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@RequestScoped
@ManagedBean(name = "registration")
@Scope("request")
public class RegistrationBean extends BaseBean {

    @Autowired(required = false)
    private UserDAOImpl userDAO;
    private String regLogin;
    private String regPassword;
    private String name;
    private String surname;
    @Autowired(required = true)
    private User user;


    public String getRegLogin() {
        return regLogin;
    }

    public void setRegLogin(String regLogin) {
        this.regLogin = regLogin;
    }

    public String getRegPassword() {
        return regPassword;
    }

    public void setRegPassword(String regPassword) {
        this.regPassword = regPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String register() throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(regPassword.getBytes());
        String hash = new BigInteger(1, md.digest()).toString(16);
        user.setUserLogin(regLogin);
        user.setUserPassword(hash);
        user.setUserName(name);
        user.setUserLastname(surname);
        user.setRoleId(0);

        if (userDAO.insertUser(user)) {
            getSession(false).setAttribute("user", user);
            return "home";
        } else {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage("Registration failed!"));
            return null;
        }
    }

}
