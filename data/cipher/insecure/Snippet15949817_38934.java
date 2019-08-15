import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Main {

  public static void main(String[] args) throws Exception {

    KeyGenerator keygenerator = KeyGenerator.getInstance("Blowfish");
    SecretKey secretkey = keygenerator.generateKey();

    String plaintextString = "StackOverflow";
    System.out.println(plaintextString + " " + bytesToHex(plaintextString.getBytes()) + " " + Arrays.toString(plaintextString.getBytes()));

    SecretKeySpec key = new SecretKeySpec(secretkey.getEncoded(), "Blowfish");
    Cipher cipher = Cipher.getInstance("Blowfish");

    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] encrypted = cipher.doFinal(plaintextString.getBytes());
    String encryptedString = bytesToHex(encrypted);
    System.out.println(new String(encrypted) + " " + encryptedString + " " + Arrays.toString(encrypted));

    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] decrypted = cipher.doFinal(hexToBytes(encryptedString));
    String decryptedString = bytesToHex(decrypted);
    System.out.println(new String(decrypted) + " " + decryptedString + " " + Arrays.toString(decrypted));

  }

  public static byte[] hexToBytes(String str) {
    if (str == null) {
      return null;
    } else if (str.length() < 2) {
      return null;
    } else {
      int len = str.length() / 2;
      byte[] buffer = new byte[len];
      for (int i = 0; i < len; i++) {
        buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
      }
      return buffer;
    }

  }

  public static String bytesToHex(byte[] data) {
    if (data == null) {
      return null;
    } else {
      int len = data.length;
      String str = "";
      for (int i = 0; i < len; i++) {
        if ((data[i] & 0xFF) < 16)
          str = str + "0" + java.lang.Integer.toHexString(data[i] & 0xFF);
        else
          str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
      }
      return str.toUpperCase();
    }
  }
}
