@Entity
@Table(name = "users")
public class User implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userId")
    private int userId;

    @Column(name = "userIdCardNo")
    private String useridcardno;

    @Column(name = "userFname")
    private String fname;

    @Column(name = "userMname")
    private String mname;

    @Column(name = "userLname")
    private String lname;

    @Column(name = "userPhone")
    private int phone;

    @Column(name = "userPhone2")
    private String phone2;

    @Column(name = "userAddress")
    private String address;

    @Column(name = "userAddress2")
    private String address2;

    @Column(name = "userName")
    private String username;

    @Column(name = "userPass")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "userId", nullable = false) , inverseJoinColumns = @JoinColumn(name = "roleId", nullable = false) )
    private List<Role> roles;

    @Enumerated(EnumType.STRING)
    @Column(name = "userStatus")
    private UserStatus status;



    //CREATE MD5 from String
    public static String md5(String input) {
        String md5 = null;
        if (null == input)
            return null;
        try {
            // Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());
            // Converts message digest value in base 16 (hex)
            md5 = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    //CONTRUSCTORS
    public User() {
    }


    public User(int userId, String useridcardno, String fname, String mname, String lname, int phone, String phone2,
            String address, String address2, String username, String password, List<Role> roles, UserStatus status) {
        super();
        this.userId = userId;
        this.useridcardno = useridcardno;
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.phone = phone;
        this.phone2 = phone2;
        this.address = address;
        this.address2 = address2;
        this.username = username;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.roles = roles;
        this.status = status;
    }

    //GETTERS and SETTERS
    public int getUserId() {
        return userId;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }


    public String getUseridcardno() {
        return useridcardno;
    }


    public void setUseridcardno(String useridcardno) {
        this.useridcardno = useridcardno;
    }


    public String getFname() {
        return fname;
    }


    public void setFname(String fname) {
        this.fname = fname;
    }


    public String getMname() {
        return mname;
    }


    public void setMname(String mname) {
        this.mname = mname;
    }


    public String getLname() {
        return lname;
    }


    public void setLname(String lname) {
        this.lname = lname;
    }


    public int getPhone() {
        return phone;
    }


    public void setPhone(int phone) {
        this.phone = phone;
    }


    public String getPhone2() {
        return phone2;
    }


    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public String getAddress2() {
        return address2;
    }


    public void setAddress2(String address2) {
        this.address2 = address2;
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
        this.password = password;
    }


    public List<Role> getRoles() {
        return roles;
    }


    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }


    public UserStatus getStatus() {
        return status;
    }


    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
