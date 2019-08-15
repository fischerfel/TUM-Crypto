@Component
@Scope("session")
public class HomeBean extends BaseBean {

    private static final String editUserBtn = "tab:form1:editUser";
    private static final String deleteUserBtn = "tab:form1:deleteUser";
    private static final String editCompBtn = "tab:form2:editComp";
    private static final String deleteCompBtn = "tab:form2:deleteComp";
    private static final String editAppBtn = "tab:form3:editApp";
    private static final String deleteAppBtn = "tab:form3:deleteApp";
    @Autowired
    private HibernateDBManager hibernateDBManager;
    private List<User> users;
    private List<Computer> computers;
    private List<Application> applications;
    private User selectedUser, newUser;
    private Computer selectedComputer, newComputer;
    private Application selectedApplication, newApplication;
    private RequestContext rc;

    @Override
    public void init() {
        setUsers(hibernateDBManager.getAllUsers());
        setComputers(hibernateDBManager.getAllComputers());
        setApplications(hibernateDBManager.getAllApplications());
        newUser = new User();
        newComputer = new Computer();
        newApplication = new Application();
        rc = RequestContext.getCurrentInstance();
    }

    public void addUser() throws NoSuchAlgorithmException {
        if (newUser != null && newUser.getPassword() != null) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(newUser.getPassword().getBytes());
            String hash = new BigInteger(1, md.digest()).toString(16);
            newUser.setPassword(hash);
            if (hibernateDBManager.insertUser(newUser)) {
                users.add(newUser);
            }
        }
    }

    public void editUser() {
        if (selectedUser != null) {
            hibernateDBManager.updateUser(selectedUser);
            users.set(users.indexOf(selectedUser), selectedUser);
            selectedUser = null;
            rc.update(deleteUserBtn);
            rc.update(editUserBtn);
        }
    }

    public void deleteUser() throws IOException {
        if (selectedUser != null) {
            if (hibernateDBManager.deleteUserById(selectedUser.getUserId()) > 0) {
                users.remove(selectedUser);
                selectedUser = null;
                rc.update(deleteUserBtn);
                rc.update(editUserBtn);
            }
        }
    }

    public void addComputer() {
        if (newComputer != null && hibernateDBManager.insertComputer(newComputer)) {
            computers.add(newComputer);
        }
    }

    public void deleteComputer() {
        if (selectedComputer != null) {
            if (hibernateDBManager.deleteComputerById(selectedComputer.getComputerId()) > 0) {
                computers.remove(selectedComputer);
                selectedComputer = null;
                rc.update(editCompBtn);
                rc.update(deleteCompBtn);
            }
        }
    }

    public void addApplication() {
        if (newApplication != null && hibernateDBManager.insertApplication(newApplication)) {
            applications.add(newApplication);
        }
    }

    public void deleteApplication() {
        if (selectedApplication != null) {
            if (hibernateDBManager.deleteApplicationById(selectedApplication.getAppId()) > 0) {
                applications.remove(selectedApplication);
                selectedApplication = null;
                rc.update(editAppBtn);
                rc.update(deleteAppBtn);
            }
        }
    }

    public void onUserRowSelect(SelectEvent event) {
        setSelectedUser((User) event.getObject());
    }

    public void onUserRowUnselect(UnselectEvent event) {
        setSelectedUser(null);
    }

    public void onCompRowSelect(SelectEvent event) {
        setSelectedComputer((Computer) event.getObject());
    }

    public void onAppRowSelect(SelectEvent event) {
        setSelectedApplication((Application) event.getObject());
    }

    //Getters etc.
    }
