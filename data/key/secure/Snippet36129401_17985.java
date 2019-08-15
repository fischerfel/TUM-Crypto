import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



public class Test {


  private static final String ALGO = "AES";

  public static String encrypt(String data,String secretKey) throws Exception {
    Key key = generateKey(secretKey);
    byte[] ivAES = {(byte)0x22,(byte)0x22,(byte)0x22,(byte)0x22,(byte)0x22,(byte)0x22,(byte)0x22,(byte)0x22,(byte)0x22,(byte)0x22,(byte)0x22,(byte)0x22,(byte)0x22,(byte)0x22,(byte)0x22,(byte)0x22};
    IvParameterSpec ivspec = new IvParameterSpec(ivAES);
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
    byte[] encVal = cipher.doFinal(data.getBytes());
    byte[] finalByte=copyBytes(encVal, ivAES);
    String encryptedValue = new BASE64Encoder().encode(finalByte);
    return encryptedValue;
  }

  public static String decrypt(String encryptedData,String secretKey) throws Exception {
    Key key = generateKey(secretKey);
    byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
    ArrayList<byte[]> al = retreiveIv(decordedValue);
    byte[] data = al.get(0);
    byte[] iv = al.get(1);
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

    byte[] decValue = cipher.doFinal(data);
    String decryptedValue = new String(decValue);
    return decryptedValue;
  }

  private static Key generateKey(String secretKey) throws Exception {
    Key key = new SecretKeySpec(secretKey.getBytes(), ALGO);
    return key;
  }
  private static byte[] copyBytes(byte[] encVal, byte[] iv) throws Exception {
       ByteArrayOutputStream os = new ByteArrayOutputStream();
       os.write(iv);
       os.write(encVal);
       return os.toByteArray();
    }
  private static ArrayList<byte[]> retreiveIv(byte[] combineByte) {
       ByteArrayOutputStream iv = new ByteArrayOutputStream(16);
       ByteArrayOutputStream data = new ByteArrayOutputStream();

       data.write(combineByte, combineByte.length - 16, 16);
       iv.write(combineByte, 0, combineByte.length - 16);

       ArrayList<byte[]> al = new ArrayList<byte[]>();
       al.add(data.toByteArray());
       al.add(iv.toByteArray());

       return al;
    }

  public static void main(String[] args) throws Exception{


      System.out.println(AESUtil.decrypt(AESUtil.encrypt("Hello", "AVH4TU8AC99dhL2l"), "AVH4TU8AC99dhL2l"));

}


}
