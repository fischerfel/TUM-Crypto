package models;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;

@Entity(name = "USERS")
@NamedQueries({
    @NamedQuery(name = "User.all", query = "SELECT u FROM USERS u"),
    @NamedQuery(name = "User.find", query = "SELECT u FROM USERS u WHERE u.userName = :username")
})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "PASSWD", length = 32,columnDefinition = "VARCHAR(32)")
    private String password;
    private String name;
    private String lastname;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date birthday;
    private String email;
    private Integer mobile;
    @ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(
            name="USER_GROUPS",
            joinColumns={ @JoinColumn(name="USER_NAME") },
            inverseJoinColumns={ @JoinColumn(name="GROUP_NAME") }
    )
    private Set<Group> groups;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getMobile() {
        return mobile;
    }

    public void setMobile(Integer mobile) {
        this.mobile = mobile;
    }

    public String getFullname(){
        return name + " " + lastname; 
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if ((this.userName == null) ? (other.userName != null) : 
                    !this.userName.equals(other.userName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (this.userName != null ? this.userName.hashCode() : 0);
        return hash;
    }


    private String hashPassword(String password) {
        String encoded = null;
        try {
            ByteBuffer passwdBuffer = 
              Charset.defaultCharset().encode(CharBuffer.wrap(password));
            byte[] passwdBytes = passwdBuffer.array();
            MessageDigest mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(passwdBytes, 0, password.length());
            encoded = new BigInteger(1, mdEnc.digest()).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }

        return encoded;
    }
}
