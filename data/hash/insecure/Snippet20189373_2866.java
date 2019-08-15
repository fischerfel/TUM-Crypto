package my.example.model;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    private String login;
    private String password;
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    User() {
        // for persistence provider only
    }

    public User(String login, String password) {
        this.login = login;
        this.password = hashPassword(password);
        this.registrationDate = new Date();
    }

    public String getLogin() {
        return login;
    }

    public String setPassword(String password) {
        this.password = hashPassword(password);
    }

    public boolean matchPassword(String password) {
        return this.password.equals(hashPassword(password));
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("sha-1");
            StringBuilder sb = new StringBuilder();
            byte[] bytes = digest.digest(password.getBytes(charset));
            for (byte b : bytes) {
                sb.append(Character.forDigit((b >>> 4) & 0xF, 16)).append(Character.forDigit(b & 0xF, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }
}

package my.example.model;

public interface UserRepository {
    User findByLogin(String login);

    User findBySurrogateId(int id);

    Integer getSurrogateId(User user);

    boolean contains(User user);

    void add(User user);

    void delete(User user);
}

package my.example.infrastructure;

@Component
public class PersistentUserRepository implements UserRepository {
    @PersistenceContext
    private EntityManager em;

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override public User findByLogin(String login) {
        // I'd use QueryDSL here
        QUser qusr = new QUser("usr");
        return new JPAQuery(em)
                .from(qusr)
                .where(qusr.login.eq(login))
                .singleResult(qusr);
    }

    @Override public User findBySurrogateId(int id) {
        return em.find(User.class, id);
    }

    @Override public Integer getSurrogateId(User user) {
        return (Integer)em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentity(user);
    }

    @Override public boolean contains(User user) {
        return em.contains(user);
    }

    @Override public void add(User user) {
        em.persist(user);
    }

    @Override public void delete(User user) {
        em.remove(user);
    }
}

package my.example.facade;

public interface UserRemoteFacade {
    UserDTO getUser(String login);

    UserDTO getUser(int id);

    void changePassword(int userId, String newPassword);

    void registerUser(String login, String password) throws LoginOccupiedException;

    boolean authenticate(String login, String password);
}

package my.example.facade;

public class UserDTO implements Serializable {
    private int id;
    private String login;
    private Date registrationDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}

package my.example.server;

@Transactional @Component
public class UserRemoteFacadeImpl imlements UserRemoteFacade {
    private UserRepository repository;
    private Security security;

    @Autowired
    public UserRemoteFacadeImpl(UserRepository repository, Security security) {
        this.repository = repository;
        this.security = security;
    }

    @Override public UserDTO getUser(String login) {
        return mapUser(repository.findByLogin(login));
    }

    @Override public UserDTO getUser(int id) {
        return mapUser(repository.findBySurrogateId(id));
    }

    private UserDTO mapUser(User user) {
        if (user != security.getCurrentUser()) {
            security.checkPermission("viewUser");
        }
        UserDTO dto = new UserDTO();
        dto.setId(repository.getSurrogateId(user));
        dto.setLogin(user.getLogin());
        dto.setRegistrationDate(user.getRegistrationDate());
        return dto;
    }

    @Override public void changePassword(int userId, String newPassword) {
        User user = repository.findByLogin(login);
        if (user != security.getCurrentUser()) {
            security.checkPermission("changePassword");
        }
        user.setPassword(newPassword);
    }

    @Override public void registerUser(String login, String password) throws LoginOccupiedException {
        if (repository.findByLogin(login) != null) {
            throw new LoginOccupiedException(login);
        }
        User user = new User(login, password);
        repository.add(user);
    }

    @Override public boolean authenticate(String login, String password) throws LoginOccupiedException {
        User user = repository.findByLogin(login);
        return user != null && user.matchPassword(password);
    }
}
