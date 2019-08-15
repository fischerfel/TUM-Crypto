import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public void encrypt(){
    String password = new String(jPasswordField1.getPassword());

    MessageDigest algorithm = null;
    try {
        algorithm = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(pe.class.getName()).log(Level.SEVERE, null, ex);
    }
    algorithm.reset();
    algorithm.update(password.getBytes());
    byte bytes [] = algorithm.digest();
    StringBuffer sb = new StringBuffer();
    for (byte b : bytes){
         String hex = Integer.toHexString(0xff & b);
         if (hex.length() == 1)
             sb.append('0');
         sb.append(hex);
    }
    String result = sb.toString();
    jTextArea1.setText(result);

}
