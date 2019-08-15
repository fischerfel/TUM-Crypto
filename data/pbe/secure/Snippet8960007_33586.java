@Entity
@Table(name="users")
@NamedQueries({
    @NamedQuery(name="User.findAll", query="SELECT u FROM User u"),
    @NamedQuery(name="User.findByPrimaryEmail", query="SELECT u FROM User u WHERE u.primaryEmail = :email")
})
public class User {
    //fields
    private long id;
    private String primaryEmail;
    private String firstName;
    private String lastName;
    private String hashedPassword;
    private String salt;
    //...

    //relationships
    //...

    public User() {
        primaryEmail = null;
        firstName = null;
        lastName = null;
        salt = null;
        hashedPassword = null;
        //...
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(unique=true)
    public String getPrimaryEmail() {
        return primaryEmail;
    }
    public void setPrimaryEmail(String email) {
        this.primaryEmail = email;
        if (this.primaryEmail != null) {
            this.primaryEmail = email.toLowerCase();
        }
    }

    @Column
    public String getHashedPassword() {
        return hashedPassword;
    }
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    @Column
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }

    //(getters and setters for any other columns and relationships)

    @Transient
    public void setPassword(String passwordPlaintext) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (this.getSalt() == null) {
            this.setSalt(StringUtilities.randomStringOfLength(16));
        }

        this.setHashedPassword(this.computeHash(passwordPlaintext, this.getSalt()));
    }

    @Transient
    public boolean checkPasswordForLogin(String passwordPlaintext) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (StringUtilities.isEmpty(passwordPlaintext)) {
            return false;
        }
        return this.getHashedPassword().equals(this.computeHash(passwordPlaintext, this.getSalt()));
    }

    @Transient
    private String computeHash(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 2048, 160);
        SecretKeyFactory fact = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        //I encode to base64 so that I can treat the hash as text in computations and when storing it in the DB
        return Base64.encodeBytes(fact.generateSecret(spec).getEncoded());
    }
}
