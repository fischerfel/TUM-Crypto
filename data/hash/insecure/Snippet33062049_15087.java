package edu.tmc.uth.i2b2shibboleth;

import java.security.*;
import java.sql.*;
import java.util.*;
import javax.faces.bean.*;
import javax.faces.context.*;
import javax.servlet.ServletContext;
import javax.servlet.http.*;

/**Manages the retrieval of Shibboleth attributes and the connection to the
 * i2b2 hive database.
 *
 * @author JRussell
 */
@ManagedBean(name = "connectDatabase")
@SessionScoped
public class ConnectDatabase implements HttpSessionBindingListener {

    private User user;
    private Connection connection;
    private List<String> test = new ArrayList<String>();
    String propertyPath = null;
    private ResourceBundle properties;

    public User getUser() {
        return user;
    }

    /*Main function for retrieving Shibboleth information and updating 
     * database records.  This is called from the Facelets page (index.xhtml).
     */
    public List<String> getUserInfo() {
        try {
            connection = connectToDatabase();
            setShibbolethAttributes();
            updateI2b2UserDatabase();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return test;
    }

    /*Returns a connection to the i2b2 hive database.
     * Uses the properties in the database.properties file.
     */
    public Connection connectToDatabase() {
        try {            
            properties = ResourceBundle.getBundle("edu/tmc/uth/i2b2shibboleth/database");
            String url = properties.getString("I2B2_PM.connectionURL");
            String userName = properties.getString("I2B2_PM.userName");
            String password = properties.getString("I2B2_PM.password");

            Class.forName(properties.getString("I2B2_PM.databaseClass"));
            connection = DriverManager.getConnection(url, userName, password);
            if (connection != null) {
                System.out.println("connected to DB");
                test.add("Connected to i2b2 hive database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /*Populates the Shibboleth attributes into the User object.
     * The request header values are based on the attribute-map.xml configuration
     * file on the Shibboleth Service Provider.  The attribute identifiers and 
     * the attributes released are configured on an institutional basis.
     */
    public void setShibbolethAttributes() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();        
        String firstName = request.getHeader("Shib-InetOrgPerson-givenName");
        String lastName = request.getHeader("Shib-Person-surname");
        String email = request.getHeader("Shib-InetOrgPerson-mail");
        String identifier = request.getHeader("Shib-iamPerson-subjectUniqueId");
        String session = request.getHeader("Shib-Session-ID"); 
        user = new User(firstName, lastName, email, identifier, session);
        test.add("Basic User attributes");
        test.add(user.toString());
        test.add("All Shibboleth headers");
        //Outputs all of the headers from the Shibboleth request
        Enumeration enumer = request.getHeaderNames();
        while(enumer.hasMoreElements()){
            String headerName = enumer.nextElement().toString();
            if(headerName.startsWith("Shib")){
            test.add(headerName+" - "+request.getHeader(headerName));
            }
        }
    }

    /*Add a new user to the i2b2 hive database if they don't have an existing record. 
     * Update the password and enable the user if the i2b2 user already exists. 
     */
    private void updateI2b2UserDatabase() throws SQLException {
        //Try to find current user in database
        String stmt = "SELECT user_id FROM pm_user_data WHERE user_id=?";
        PreparedStatement pst = connection.prepareStatement(stmt);
        pst.setString(1, user.identifier);
        System.out.println(stmt+" "+user.identifier);
        ResultSet result = pst.executeQuery();
        //user record found in database so update password
        if (result.next()) {
            System.out.println("user record found");
            test.add("Subject identifier already in database -  " + result.getString("user_id"));
            stmt = "UPDATE pm_user_data SET password=?, status_cd=? WHERE user_id=?";
            pst = connection.prepareStatement(stmt);
            pst.setString(1, encryptMD5(user.session));
            pst.setString(2, "A");
            pst.setString(3, user.identifier);
            pst.executeUpdate();
            connection.commit();
        } 
        else { //new user so add record to database
            test.add("New user - " + user.identifier + " " + user.first + " " + user.last);
            stmt = "INSERT INTO pm_user_data (user_id, full_name, password, email, status_cd) \n"
                    + "VALUES (?,?,?,?,?)";
            pst = connection.prepareStatement(stmt);
            pst.setString(1, user.identifier);
            pst.setString(2, user.first + " " + user.last);
            pst.setString(3, encryptMD5(user.session));
            pst.setString(4, user.email);
            pst.setString(5, "A");
            pst.executeUpdate();

            //assign user roles to i2b2 project
            String project = properties.getString("I2B2_PM.projectName");
            pst = connection.prepareStatement("INSERT INTO pm_project_user_roles (project_id, user_id, user_role_cd, status_cd) \n"
                    + "VALUES (?,?,?,?)");
            pst.setString(1, project);
            pst.setString(2, user.identifier);
            pst.setString(3, "DATA_OBFSC");
            pst.setString(4, "A");
            pst.executeUpdate();
            pst.setString(3, "DATA_AGG");
            pst.executeUpdate();
            pst.setString(3, "USER");
            pst.executeUpdate();
            connection.commit();
        }
    }

    /* The i2b2 applications expect passwords to be encrypted.
    *This function encrypts passwords with MD5 before inserting them
    *into the database.
     */
    private String encryptMD5(String text) {
        String encrypted = "";

        byte[] defaultBytes = text.getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            encrypted = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encrypted;
    }

    /*Function needed to implement HttpSessionBindingListener.
     * Don't need to modify the function itself. 
     */
    public void valueBound(HttpSessionBindingEvent event) {
        //do nothing
    }

    //This function deactivates a user in the i2b2 hive database when 
    //the user session times out.  Session timeout is set in web.xml.
    public void valueUnbound(HttpSessionBindingEvent event) {
        connection = connectToDatabase();
        try {
            //Try to find current user in database
            String stmt = "SELECT user_id FROM pm_user_data WHERE user_id=?";
            PreparedStatement pst = connection.prepareStatement(stmt);
            pst.setString(1, user.identifier);
            ResultSet result = pst.executeQuery();
            //user record found in database so deactivate them since they have logged off
            if (result.next()) {
                System.out.println("in results.next");
                stmt = "UPDATE pm_user_data SET status_cd=? WHERE user_id=?";
                pst = connection.prepareStatement(stmt);
                pst.setString(1, "D");
                pst.setString(2, user.identifier);
                pst.executeUpdate();
                connection.commit();
            }
            connection.close();
            ServletContext context = event.getSession().getServletContext();
            FacesContext.getCurrentInstance().getExternalContext().redirect("logout.xhtml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
