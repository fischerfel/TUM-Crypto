@Entity
@Table(name="User")
public class User extends AbstractEntity {

private static final long serialVersionUID = 1L;

@Column (unique=true, length=30)
@NotNull
private String login;

@Column (length=32)
@NotNull
private String password;

@NotNull
@Email
@Column (unique=true, length=80)
private String email;

@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="owner")
private List<UserPicture> profilePictures = new LinkedList<UserPicture>();

public String getLogin() {
    return login;
}

public void setLogin(String login) {
    this.login = login;
}

public String getPassword() {
    return password;
}

public void setPassword(String password) {
    this.password = password;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

@Transient
public void encryptPassword() {
    this.password = md5(password);
}

public List<UserPicture> getProfilePicture() {
    return Collections.unmodifiableList(profilePictures);
}

public void addProfilePicture(UserPicture profilePicture) {
    profilePicture.setOwner(this);
    profilePictures.add(profilePicture);
}

@Transient
private String md5(String input) {

    String md5 = null;

    if(null == input) return null;

    try {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(input.getBytes(), 0, input.length());
        md5 = new BigInteger(1, digest.digest()).toString(16);

    } catch (NoSuchAlgorithmException e) {

        e.printStackTrace();
    }
    return md5;
}   
}
