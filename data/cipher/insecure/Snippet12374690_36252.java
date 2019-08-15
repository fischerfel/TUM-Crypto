import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.xml.bind.DatatypeConverter;

public class MACTest {

  public static void main(String[] args) throws Exception {

    final byte[] keyBytes = new byte[24];
    final byte[] paddedPlaintext = 
        hexStringToByteArray("11223344556677889900112200000000");
    final byte[] iv = new byte[8];
    final byte[] sessionKeyBytes = hexStringToByteArray("2923be84b1495461");

    final byte[] derivedKeyBytes = new byte[24];
    for (int i = 0; i < sessionKeyBytes.length; i++) {
      derivedKeyBytes[i] = (byte) (keyBytes[i] ^ sessionKeyBytes[i]);
    }

    System.out.println(toHexString(derivedKeyBytes));
    SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
    SecretKey derivedKey = factory.generateSecret(new DESedeKeySpec(
        derivedKeyBytes));

    Cipher c = Cipher.getInstance("DESede/CBC/NoPadding");
    c.init(Cipher.ENCRYPT_MODE, derivedKey, new IvParameterSpec(iv));
    byte[] result = c.doFinal(paddedPlaintext);
    System.out.println(toHexString(result));
  }

  public static String toHexString(byte[] array) {
    return DatatypeConverter.printHexBinary(array);
  }

  public static byte[] hexStringToByteArray(String s) {
    return DatatypeConverter.parseHexBinary(s);
  }
}
