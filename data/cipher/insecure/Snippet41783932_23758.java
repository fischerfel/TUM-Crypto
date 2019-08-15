import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class CryptoTest
{

  private static final String ADMIN_PASSWORD = "admin";
  private static final String ADMIN_ENCRYPTED_PASSWORD = "532C05C5B5";                             // RC4 encrypted password using KEY
  private static final String ADMIN_AUTH_KEY = "1391a8a860b7d6e2e86df513700e490c16dae47cdae227ca"; // PBKDF2(username,password,salt)
  private static final String CRYPTO_ALGORITHM = "RC4";

  protected static String encryptPassword(String passwordDataToEncrypt, String userskey) throws Exception 
  {
    SecureRandom sr = new SecureRandom(userskey.getBytes());
    KeyGenerator kg = KeyGenerator.getInstance(CRYPTO_ALGORITHM);
    kg.init(sr);
    SecretKey sk = kg.generateKey();
    Cipher cipher = Cipher.getInstance(CRYPTO_ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, sk);
    return bytesToHex(cipher.doFinal(passwordDataToEncrypt.getBytes()));
  }

  private static String bytesToHex(byte[] in) 
  {
    return DatatypeConverter.printHexBinary(in);
  }

  private static byte[] hexStringToByteArray(String s) 
  {
    return DatatypeConverter.parseHexBinary(s);
  }

  protected static String decryptPassword(byte[] toDecryptPassword, String key) throws Exception 
  {
    SecureRandom sr = new SecureRandom(key.getBytes());
    KeyGenerator kg = KeyGenerator.getInstance(CRYPTO_ALGORITHM);
    kg.init(sr);
    SecretKey sk = kg.generateKey();
    Cipher cipher = Cipher.getInstance(CRYPTO_ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, sk);
    return new String(cipher.doFinal(toDecryptPassword));
  }

  public static void assertEquals(String arg1, String arg2)
  {
    if (! arg1.equals(arg2))
    {
      System.out.println(String.format("%s does not equal %s", arg1, arg2));
    }
  }

  public static void testGetDecryptedPassword() throws Exception
  {
    String decryptedPassword = decryptPassword(hexStringToByteArray(ADMIN_ENCRYPTED_PASSWORD), ADMIN_AUTH_KEY);
    assertEquals(ADMIN_PASSWORD, decryptedPassword);
  }

  public static void testGetEncryptedPassword() throws Exception
  {
    String encryptedPassword = encryptPassword(ADMIN_PASSWORD, ADMIN_AUTH_KEY);
    assertEquals(ADMIN_ENCRYPTED_PASSWORD, encryptedPassword);
  }

  public static void testEncryptAndDecryptPasswords() throws Exception
  {
    String originalPassword = "password";
    String encryptedPassword = encryptPassword(originalPassword, ADMIN_AUTH_KEY);
    String decryptedPassword = decryptPassword(hexStringToByteArray(encryptedPassword), ADMIN_AUTH_KEY);
    assertEquals(originalPassword, decryptedPassword);

    originalPassword = "This is a STRONG password 4 me!!!@#$^";
    encryptedPassword = encryptPassword(originalPassword, ADMIN_AUTH_KEY);
    decryptedPassword = decryptPassword(hexStringToByteArray(encryptedPassword), ADMIN_AUTH_KEY);
    assertEquals(originalPassword, decryptedPassword);
  }

  public static void main(final String[] args)
  {
    try
    {
      int strength =  Cipher.getMaxAllowedKeyLength("AES");
      if ( strength > 128 ){
        System.out.printf("isUnlimitedSupported=TRUE,strength: %d%n",strength);
      } else {
        System.out.printf("isUnlimitedSupported=FALSE,strength: %d%n",strength);
      }

      testGetDecryptedPassword();
      testGetEncryptedPassword();
      testEncryptAndDecryptPasswords();
    }
    catch (Exception e)
    {
      System.out.printf("Caught exception: %s\n", e.getMessage());
      e.printStackTrace(System.out);
    }
  }
}
