import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.security.AlgorithmParameters;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class FileEncryptionTest {
  public static void main(String[] args) throws Exception {

    char[] password = "password".toCharArray();
    byte[] salt = new byte[8];

    /* Derive the key, given password and salt. */
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

    /* Encrypt the message. */
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    AlgorithmParameters params = cipher.getParameters();
    byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

    String file = "someprivatekey.p12";
    String keyFileText = readFile(file);

    byte[] ciphertext = cipher.doFinal(keyFileText.getBytes("UTF-8"));

    Cipher otherCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    otherCipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));

    String plaintext = new String(otherCipher.doFinal(ciphertext), "UTF-8");

    if (plaintext.equals(keyFileText))
      System.out.println("decrypted plaintext same as plaintext");
    System.out.println("plaintext length: " + plaintext.length() + " keyFileText length: " + keyFileText.length());

    writeFile(plaintext, "new-" + file);
  }

  private static void writeFile(String contents, String filePath) {
    PrintWriter out = null;
    try {
      out = new PrintWriter(new FileOutputStream(filePath));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    out.write(contents);
    out.close();
  }

  private static String readFile(String filePath) {
    FileInputStream fis = null;
    int buf;
    StringBuilder contents = null;
    try {
      fis = new FileInputStream(filePath);
      contents = new StringBuilder();
      while ((buf = fis.read()) != -1) {
        contents.append((char) buf);
      }
      fis.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return contents.toString();
  }
}
