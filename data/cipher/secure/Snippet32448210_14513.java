import sun.misc.BASE64Decoder;
 import sun.misc.BASE64Encoder;

 @Service
 public class SecureModule {
     private Logger log = LoggerFactory.getLogger(getClass());
     public static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
     public static final String Decrypt(String text, String key) throws Exception
     {
         byte[] textBytes = Base64.decodeBase64(text);
         //byte[] textBytes = str.getBytes("UTF-8");
         AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
         SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
         cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
         return new String(cipher.doFinal(textBytes), "UTF-8");
     }
     public static final String DecryptAndBase64(String text, String key) throws Exception
     {
         byte[] textBytes = Base64.decodeBase64(text);
         //byte[] textBytes = str.getBytes("UTF-8");
         AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
         SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
         cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
         String Result="";
         // tried but doens't work
         //    Result = new String(cipher.doFinal(new Base64().decode(text.getBytes())), "UTF-8");

         Result = new String(Base64.decodeBase64(new String(cipher.doFinal(textBytes), "UTF-8").getBytes()));
         return Result;
=    }
     public static final String Encrypt(String text, String key) throws Exception
     {
         byte[] textBytes = text.getBytes("UTF-8");
         AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
              SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
              Cipher cipher = null;
         cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
 //      cipher = Cipher.getInstance("AES/CBC");
         cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
         byte[] sResult = cipher.doFinal(textBytes);
         String ssResult = Base64.encodeBase64String(sResult);
         return ssResult;
     }
     public static final String EncryptAndBase64(String text, String key) throws Exception
     {
        String plaintext = "";
         plaintext = new String(Base64.encodeBase64(text.getBytes()));
         byte[] textBytes = plaintext.getBytes("UTF-8");
         AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
              SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
              Cipher cipher = null;
         cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
         cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
         return Base64.encodeBase64String(cipher.doFinal(textBytes));
    }
}
