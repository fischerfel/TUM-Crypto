package com.viktor.solodoukhin.beans;


import javax.persistence.*;
import java.sql.Timestamp;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name="tuser")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", unique=true, nullable=false)
    private Long id;

    @Column(name="first_name", nullable=false)
    private String firstName;

    @Column(name="last_name", nullable=false)
    private String lastName;

    @Column(name="email", unique=true)
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="born_on")
    private Date bornOn;

    @Column(name="created_at")
    private Timestamp createdAt;

    @Column(name="updated_at")
    private Timestamp updatedAt;

    @Column(name="is_archived")
    private short isArchived;

    @Column(name="is_validated")
    private short isValidated;

    @OneToMany(mappedBy = "userId")
    private List<Address> addresses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        String hashedPassword = null;
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes(), 0, password.length());
            hashedPassword = new BigInteger(1, md.digest()).toString(16);
            this.password = hashedPassword;

        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public Date getBornOn() {
        return bornOn;
    }

    public void setBornOn(Date bornOn) {
        this.bornOn = bornOn;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public short getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(short isArchived) {
        this.isArchived = isArchived;
    }

    public short getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(short isValidated) {
        this.isValidated = isValidated;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}
