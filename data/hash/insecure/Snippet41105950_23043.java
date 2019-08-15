/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.jacobhewitt.entitys;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.persistence.NamedQueries;

/**
 *
 * @author jake
 */
@Entity
@NamedQueries({@NamedQuery(name="findUserByEmail", query="SELECT u FROM UserEntity u WHERE u.email = :email")})
public class UserEntity implements Serializable {

    @Id
    @Column(name="email")
    private String email;

    @Column(name="password", length=32, columnDefinition = "VARCHAR(32)")
    private char[] password;

    private String firstName;
    private String lastName;

    @Column(name="since", columnDefinition = "DATE", nullable = true)
    private Date since;


    private String role;

    public void setRole(String role){
        this.role = role;

    }

    public String getRole(){
        return role;
    }

    public void setPassword(char[] password){
        this.password = password;
    }

    public void setPassword(String password){
        this.password = hashPassword(password.toCharArray());
    }

    public char[] getPassword(){
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getSince() {
        return since;
    }

    public void setSince(Date since) {
        this.since = since;
    }

    private char[] hashPassword(char[] password) {
        char[] encoded = null;
        try {
            ByteBuffer passwdBuffer = Charset.defaultCharset().encode(CharBuffer.wrap(password));
            byte[] passwdBytes = passwdBuffer.array();
            MessageDigest mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(passwdBytes, 0, password.length);
            encoded = new BigInteger(1, mdEnc.digest()).toString(16).toCharArray();
        } catch (NoSuchAlgorithmException ex) {

        }

        return encoded;
    }

}
