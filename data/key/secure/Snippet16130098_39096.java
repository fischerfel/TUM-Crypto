public class UltimateEncryptor {

  private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
  private static final String RANDOM_GENERATOR_ALGORITHM = "AES";


  // Encrypts string and encodes in Base64
  public String encrypt( String password, String data ) throws Exception 
  {
    byte[] secretKey = password.getBytes();
    byte[] clear = data.getBytes();

    SecretKeySpec secretKeySpec = new SecretKeySpec( secretKey, CIPHER_ALGORITHM );
    Cipher cipher = Cipher.getInstance( CIPHER_ALGORITHM );
    cipher.init( Cipher.ENCRYPT_MODE, secretKeySpec );

    byte[] encrypted = cipher.doFinal( clear );
    String encryptedString = Base64.encodeToString( encrypted, Base64.DEFAULT );

    return encryptedString;
  }

  // Decrypts string encoded in Base64
  public String decrypt( String password, String encryptedData ) throws Exception 
  {
    byte[] secretKey = password.getBytes();
    SecretKeySpec secretKeySpec = new SecretKeySpec( secretKey, CIPHER_ALGORITHM );
    Cipher cipher = Cipher.getInstance( CIPHER_ALGORITHM );
    cipher.init( Cipher.DECRYPT_MODE, secretKeySpec );

    byte[] encrypted = Base64.decode( encryptedData, Base64.DEFAULT );
    byte[] decrypted = cipher.doFinal( encrypted );

    return new String( decrypted );
  }
}
