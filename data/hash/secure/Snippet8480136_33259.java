package lt.database.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Minutis
 */
@Entity
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotEmpty(message = "Vartotojo vardas privalomas.")
    @Size(min = 1, max = 50, message = "Vartotojo vardas turi būti tarp 1 ir 50 simbolių.")
    @Column(name = "username")
    private String username;

    @Basic(optional = false)
    @NotEmpty(message = "Slaptažodis yra privalomas.")
    @Size(min = 1, max = 64, message = "Slaptažodis turi būti tarp 1 ir 64 simbolių.")
    @Column(name = "password")
    private String password;

    @Email(message = "Netinkamai įvestas el. paštas.")
    @Basic(optional = false)
    @NotEmpty(message = "El. paštas yra privalomas.")
    @Size(min = 1, max = 100, message = "El. paštas turi būti tarp 1 ir 100 simbolių.")
    @Column(name = "email")
    private String email;

    @Basic(optional = false)
    @NotNull
    @Column(name = "user_type")
    private byte userType;

    @Size(max = 100, message = "Vardas neturi viršyti 100 simbolių.")
    @Column(name = "first_name")
    private String firstName;

    @Size(max = 100, message = "Pavardė neturi viršyti 100 simbolių.")
    @Column(name = "last_name")
    private String lastName;

    @Size(max = 100, message = "Adresas neturi viršyti 100 simbolių.")
    @Column(name = "address")
    private String address;

    @Size(max = 100, message = "Telefonas neturi viršyti 100 simbolių.")
    @Column(name = "phone")
    private String phone;

    @Size(max = 100, message = "Pašto kodas neturi viršyti 100 simbolių.")
    @Column(name = "postcode")
    private String postcode;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String username, String password, String email, byte userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.userType = userType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            byte[] digest = md.digest();

            StringBuffer hashPassword = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
              hashPassword.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            this.password = hashPassword.toString();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte getUserType() {
        return userType;
    }

    public void setUserType(byte userType) {
        this.userType = userType;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lt.database.model.User[ id=" + id + " ]";
    }

}
