import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AES {
    public static void main(String[] args) throws Exception {
    String test = "HIHI";
    String tmp = null;
    SecretKey tempKey1 = generateKey();
    saveKey(tempKey1);
    SecretKey tempKey2 = loadKey();
    tmp = encrypt(test, tempKey1);
    String printString = decrypt(tmp, tempKey2);
    System.out.println(printString);

}

public static SecretKey generateKey() throws NoSuchAlgorithmException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(128);
    SecretKey key = keyGenerator.generateKey();
    return key;
}

public static void saveKey(SecretKey key) throws IOException {
    String stringKey = Base64.encodeBase64String(key.getEncoded());
    System.out.println("stringKey: " + stringKey);
    try {
        final PreparedStatement pstmt;

        // Register the JDBC driver
        // for MySQL.
        Class.forName("com.mysql.jdbc.Driver");

        // Define URL of database
        // server for
        // database named JunkDB on
        // the localhost
        // with the default port
        // number 3306.
        String url = "jdbc:mysql://127.0.0.1:3306/fyp";

        // Get a connection to the
        // database for a
        // user named auser with the
        // password
        // drowssap, which is
        // password spelled
        // backwards.
        Connection con = DriverManager.getConnection(url, "user", "user");

        pstmt = con.prepareStatement("insert into compfyp values (?)");
        pstmt.setString(1, stringKey);

        pstmt.executeUpdate();

        pstmt.close();

        con.close();
    } catch (Exception e) {
        e.printStackTrace();
    }// end catch

}

public static SecretKey loadKey() throws IOException {
    String stringKey = null;
    try {
        Statement stmt;
        ResultSet rs;

        // Register the JDBC driver for MySQL
        Class.forName("com.mysql.jdbc.Driver");

        // Define URL of database server for database named comp2220
        String url = "jdbc:mysql://127.0.0.1:3306/fyp";

        // Get a connection to the database for a user named user with the
        // password userpassword, which is password spelled backwards
        Connection con = DriverManager.getConnection(url, "user", "user");

        // Get a Statement object
        stmt = con.createStatement();

        // Get another statement object initialized as shown
        stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        // Query the database, storing the result in an object of type
        // ResultSet
        rs = stmt.executeQuery("SELECT * from compfyp");

        // Use the methods of class ResultSet in a loop to display all of
        // the data in the database.
        while (rs.next()) {
            stringKey = rs.getString("encryptionkey");

        }// end while loop
        con.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    byte[] encodedKey = Base64.decodeBase64(stringKey);
    SecretKey trueKey = new SecretKeySpec(encodedKey, 0, encodedKey.length,
            "AES/ECB/PKCS5Padding");
    System.out.println("after encode & decode secret_key:"
            + Base64.encodeBase64String(trueKey.getEncoded()));

    return trueKey;
}

public static String encrypt(String plaintext, SecretKey encryptionKey)
        throws Exception {
    IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, iv);
    byte[] encryptData = cipher.doFinal(plaintext.getBytes());

    return encryptData.toString();

}

public static String decrypt(String ciphertext, SecretKey encryptionKey)
        throws Exception {
    IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
    SecretKeySpec spec = new SecretKeySpec(encryptionKey.getEncoded(),
            "AES/CBC/PKCS5Padding");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, spec, iv);
    byte[] encryptData = ciphertext.getBytes();
    ;

    byte[] original = cipher.doFinal(encryptData);
    return new String(original);

}
