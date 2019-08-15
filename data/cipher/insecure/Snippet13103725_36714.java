import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class EncryptionExample {

  public static void main(String[] args) throws Exception {
    final String keyHex = "00000000000000000000000000123456";
    final String plaintextHex = "00000000000000000000000000000000";

    SecretKey key = new SecretKeySpec(DatatypeConverter
        .parseHexBinary(keyHex), "AES");

    Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, key);

    byte[] result = cipher.doFinal(DatatypeConverter
        .parseHexBinary(plaintextHex));

    System.out.println(DatatypeConverter.printHexBinary(result));
  }
}
