@Column(name = "PASSWORD")
@XmlElement(name = "Password")
public String getPasswordHash() {
    return passwordHash;
}

public void setPasswordHash(String passwordHash) {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        this.passwordHash = (new HexBinaryAdapter()).marshal(md.digest(passwordHash.getBytes(Charset.forName("UTF-8"))));
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
    }
}
