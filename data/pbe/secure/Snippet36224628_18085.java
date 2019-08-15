import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public PBKDF2() {
    initComponents();
}

private void initComponents() {//"Generated Code"
...... 
}

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
    password= jPasswordField1.getPassword();
    jTextArea1.setText(null);
    try {
        jTextArea1.append(hashPassword(password.toString(), "salt"));
    } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
        Logger.getLogger(PBKDF2.class.getName()).log(Level.SEVERE, null, ex);
    }
}

public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PBKDF2().setVisible(true);
            }
        });
    }

 public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException{
    char[] pw = password.toCharArray();
    byte[] slt = salt.getBytes(StandardCharsets.UTF_8);
    PBEKeySpec spec = new PBEKeySpec(pw,slt,2000,160);
    SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    byte[] hashedPassword = key.generateSecret(spec).getEncoded();
    return String.format("%X", new BigInteger(hashedPassword));
  }

    public static char[] password;
    public javax.swing.JButton jButton1;
    public javax.swing.JPasswordField jPasswordField1;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextArea jTextArea1;
}
