public class Crypto {
  Cipher decipher;

  byte[] salt = {
      (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04,
      (byte) 0x0A, (byte) 0x0B, (byte) 0x0C, (byte) 0x0D
  };
  int iterationCount = 10;

  public Crypto(String pass) {
    try {
      KeySpec keySpec = new PBEKeySpec(pass.toCharArray(), salt, iterationCount);

      SecretKey key = SecretKeyFactory.getInstance(
          "PBEWithMD5AndTripleDES").generateSecret(keySpec);

      ecipher = Cipher.getInstance("PBEWithMD5AndTripleDES/CBC/PKCS5Padding");

      AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

      decipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

    } catch (Exception ex) {
    }
  }
}
