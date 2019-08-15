public class AESencrp {

  private static final String ALGO = "AES";
  private static final byte[] keyValue = 
      new byte[] { 'A', 'b', 'c', 'd', 'e', 'f', 'g',
      'h', 'i', 'j', 'k','l', 'm', 'n', 'o', 'p'};

  public static String encrypt(String Data) throws Exception {
    Key key = generateKey();
    Cipher c = Cipher.getInstance(ALGO);
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encVal = c.doFinal(Data.getBytes());
    String encryptedValue = new BASE64Encoder().encode(encVal);
    return encryptedValue;
  }


  private static Key generateKey() throws Exception {
    Key key = new SecretKeySpec(keyValue, ALGO);
    return key;
  }
}
