import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.math.*;

public class Encryptor3 {

  public static byte[] hexStringToByteArray(String hexInputString){
    byte[] bts = new byte[hexInputString.length() / 2];

    for (int i = 0; i < bts.length; i++) {
      bts[i] = (byte) Integer.parseInt(hexInputString.substring(2*i, 2*i+2), 16);
    }

    return bts;
  }

  public static String byteArrayToString(byte[] byteArray) {
    StringBuilder str = new StringBuilder();

    for (int i = 0; i < byteArray.length; i++) {
      str.append((char) byteArray[i]);
    }

    return str.toString();
  }

  public static String byteArrayToHexString(byte[] arg) {
    int l = arg.length * 2;
    return String.format("%0"+l+"x", new BigInteger(1, arg));
  }

  public static byte[] encrypt(byte[] key1, byte[] key2, byte[] value) {
    try {
      IvParameterSpec iv = new IvParameterSpec(key2);
      SecretKeySpec skeySpec = new SecretKeySpec(key1, "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
      cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

      byte[] encrypted = cipher.doFinal(value);

      return encrypted;

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return null;
  }

  public static byte[] decrypt(byte[] key1, byte[] key2, byte[] encrypted) {
    try {
      IvParameterSpec iv = new IvParameterSpec(key2);
      SecretKeySpec skeySpec = new SecretKeySpec(key1, "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
      cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

      byte[] original = cipher.doFinal(encrypted);

      return original;

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return null;
  }

  public static String toHex(String arg) {
    int l = arg.length() * 2;

    return String.format("%0"+l+"x", new BigInteger(1, arg.getBytes()));
  }

  public static String HexStringToString (String arg) {
    StringBuilder output = new StringBuilder();
    for (int i = 0; i < arg.length(); i+=2) {
      String str = arg.substring(i, i+2);
      output.append((char)Integer.parseInt(str, 16));
    }

    return output.toString();
  }


  public static void main(String[] args) {
    // source: http://www.inconteam.com/software-development/41-encryption/55-aes-test-vectors#aes-cbc-128
    String message = "6bc1bee22e409f96e93d7e117393172a"; // 16 byte = 128 bit key
    //String message = toHex("Hello00000000000");
    String key1 =    "2b7e151628aed2a6abf7158809cf4f3c";
    String iv =      "000102030405060708090A0B0C0D0E0F";
    String match =   "7649abac8119b246cee98e9b12e9197d";

    System.out.print("message (hex):         "); System.out.println(message);
    System.out.print("key (hex):             "); System.out.println(key1);
    System.out.print("iv (hex):              "); System.out.println(iv);
    System.out.print("match (hex):           "); System.out.println(match);
    System.out.println();

    byte[] enc_message_ba = encrypt(hexStringToByteArray(key1), hexStringToByteArray(iv), hexStringToByteArray(message));
    System.out.print("Encrypted (hex):       "); System.out.println(byteArrayToHexString(enc_message_ba));
    System.out.println();

    byte[] dec_message_ba = decrypt(hexStringToByteArray(key1), hexStringToByteArray(iv), enc_message_ba);
    System.out.print("Decrypted (hex):       "); System.out.println(byteArrayToHexString(dec_message_ba));
  }
}
