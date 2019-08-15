@Entity
@Table(name = "Users")
public class User implements Serializable {

    private String id;

    private String username;

    private String password;

    private String email;



    private Boolean enabled;


    private Event checkedInEvent;

    @Id
    @GeneratedValue(generator="uuid", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", unique = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @PrePersist
    public void encryptPassword() throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte byteData[] = md.digest();
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        password = sb.toString();
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Column(name = "enabled")
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @PostLoad
    public void postLoad()
    {
        password = null;
    }


    @ManyToOne(optional=true,cascade=CascadeType.ALL)
    @JoinTable
    (
        name="CheckedInUsers",
        joinColumns={ @JoinColumn(name="checkedInUsersId", referencedColumnName="id") },
        inverseJoinColumns={ @JoinColumn(name="checkedInEventsId", referencedColumnName="id") }
    )
    public Event getCheckedInEvent() {
        return checkedInEvent;
    }

    public void setCheckedInEvent(Event checkedInEvent) {
        this.checkedInEvent = checkedInEvent;
    }
}
