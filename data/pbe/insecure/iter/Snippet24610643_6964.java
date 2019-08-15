public class enc_dec {
public static Cipher dcipher, ecipher;

 public  enc_dec() {

          byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
                       (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03 };

          int iterationCount = 19;
try {
                 // Generate a temporary key. In practice, you would save this key
                 // Encrypting with DES Using a Pass Phrase
                 KeySpec keySpec = new PBEKeySpec("".toCharArray(), salt,iterationCount);
                 SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
                              .generateSecret(keySpec);

                 ecipher = Cipher.getInstance(key.getAlgorithm());
                 dcipher = Cipher.getInstance(key.getAlgorithm());

                 // Prepare the parameters to the cipthers
                 AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
                              iterationCount);

                 ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
                 dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

          } catch (Exception e) { }
    }

   // Encrpt Password
   @SuppressWarnings("unused")
   public String encrypt(String str) {
          try {
                 // Encode the string into bytes using utf-8
                 byte[] utf8 = str.getBytes("UTF8");
                 // Encrypt
                 byte[] enc = ecipher.doFinal(utf8);
                 // Encode bytes to base64 to get a string
                 return new sun.misc.BASE64Encoder().encode(enc);

          } catch (Exception e) {
          }
          return null;
   }

   // Decrpt password
   // To decrypt the encryted password
   public String decrypt(String str) {
          Cipher dcipher = null;
          try {
                 byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
                              (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03 };
                 int iterationCount = 19;
                 try {
                       String passPhrase = "";
                       KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(),
                                     salt, iterationCount);
                       SecretKey key = SecretKeyFactory
                                     .getInstance("PBEWithMD5AndDES")
                                     .generateSecret(keySpec);
                       dcipher = Cipher.getInstance(key.getAlgorithm());
                       // Prepare the parameters to the cipthers
                       AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
                                     iterationCount);
                       dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
                 } catch (Exception e) {}


                 // Decode base64 to get bytes
                 byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
                 // Decrypt
                 byte[] utf8 = dcipher.doFinal(dec);
                 // Decode using utf-8
                 return new String(utf8, "UTF8");
          } catch (Exception e) {
          }
          return null;
    }  
