import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class DesHelper { 
    private Cipher _dcipher;
    public DesHelper() {
       try {

           byte[] tdesKey = new byte[24];
           System.arraycopy("2557133392096270".getBytes(StandardCharsets.US_ASCII), 0, tdesKey, 0, 16);
           System.arraycopy("2557133392096270".getBytes(StandardCharsets.US_ASCII), 0, tdesKey, 16, 8);

           final SecretKey key = new SecretKeySpec(tdesKey, "DESede");

           _dcipher = Cipher.getInstance("DESede/CBC/NoPadding");
           final IvParameterSpec iv = new IvParameterSpec(new byte[8]);

           _dcipher.init(Cipher.DECRYPT_MODE, key,iv);

      } catch (final Exception e) {
          throw new RuntimeException(e);
      }
  }


  public String decrypt(final String str) {
      try {

          final byte[] dec1 = hexToBytes(str);
          final byte[] decryptedBytes = _dcipher.doFinal(dec1);   
          return new String(decryptedBytes, StandardCharacters.US_ASCII);
      } catch (final Exception e) {
          System.out.println("decrypting string failed: " + str + " (" + e.getMessage() + ")");
          return null;
      }
  }

 private static byte[] hexToBytes(final String hex) {
      final byte[] bytes = new byte[hex.length() / 2];
      for (int i = 0; i < bytes.length; i++) {
          bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
      }
      return bytes;
 }
