import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class FXMLDocumentController implements Initializable {

    @FXML private TextField tfUsername;
    @FXML private PasswordField pfPassword;
    @FXML private Button btnSignIn;

    @FXML
    void handlebtnSignIn(ActionEvent event){

        int userID = 0;
        String username = "", accountType = "";
        byte[] encryptedPassword = null, salt = null;
        if(tfUsername.getText().length() > 0 && pfPassword.getText().length() > 0)
        {
           try(Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db"); 
                PreparedStatement  stmt = createPreparedStatement(conn, tfUsername.getText());
                ResultSet rs = stmt.executeQuery())
            {            
                while(rs.next())
                {
                    userID = rs.getInt("user_id");
                    username = rs.getString("username");
                    encryptedPassword = rs.getBytes("password");
                    salt = rs.getBytes("salt");
                    accountType = rs.getString("account_type");              
                }

                if(tfUsername.getText().equals(username) && authenticate(pfPassword.getText(), encryptedPassword, salt))
                {
                    Stage stage = new Stage();

                    stage.setWidth(1080);
                    stage.setHeight(720);
                    Parent fxmlLoader = null;

                    switch(accountType)
                    {
                        case "administrator":  
                            stage.setTitle("Tech Services Console");
                            System.out.println("I just signed in as an administrator");
                            fxmlLoader = FXMLLoader.load(getClass().getResource("AdminView.fxml"));
                            break;
                        case "collection":
                            stage.setTitle("Collection Console");
                            System.out.println("I just signed in as a collection member");
                            fxmlLoader = FXMLLoader.load(getClass().getResource("CollectionDevView.fxml"));
                            break;
                        case "selector":
                            stage.setTitle("Selector Console");
                            System.out.println("I just signed in as a selector member");
                            fxmlLoader = FXMLLoader.load(getClass().getResource("Selector.fxml"));
                            break;
                    }


                    stage.setScene(new Scene(fxmlLoader));

                    stage.show();
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                    tfUsername.setText("");
                    pfPassword.setText("");
                }  
                else
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect username or password!");
                    alert.showAndWait();
                }
            } 
            catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Incorrect username or password!");
                alert.showAndWait();
                System.out.println(ex.toString());
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }        
    }

    @FXML
    void handleEnterButtonPress(KeyEvent event)
    {
        if(event.getCode().equals(KeyCode.ENTER))
        {
            btnSignIn.fire();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   

    private PreparedStatement createPreparedStatement(Connection con, String username) throws SQLException {
        String sql = "SELECT * FROM User WHERE username = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, username);
        return ps;
    }

    boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        byte[] encryptedAttmeptedPassword = getEncryptedPassword(attemptedPassword, salt);
        return Arrays.equals(encryptedPassword, encryptedAttmeptedPassword);
    }

    public byte[] getEncryptedPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160;
        int iterations = 2000;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);
        SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

        return f.generateSecret(spec).getEncoded();
    }

    public byte[] generateSalt() throws NoSuchAlgorithmException 
    {    
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[8];
        random.nextBytes(salt);

        return salt;
    }
}
