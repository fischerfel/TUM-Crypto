import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import org.apache.commons.net.util.Base64;

   private static SecretKey key;

         try {
            byte[] secretBytes = "secret key".getBytes("UTF8");
            DESKeySpec keySpec = new DESKeySpec(secretBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            key = keyFactory.generateSecret(keySpec);
         } catch (Exception e) {
            Log.e(Flashum.LOG_TAG, "DatabaseHelper " + e.toString());
         }

   public byte[] encryptPassword(String userpw) {
      try {
         byte[] cleartext = userpw.getBytes("UTF8");      

         Cipher cipher = Cipher.getInstance("DES");
         cipher.init(Cipher.ENCRYPT_MODE, key);
         byte[] clearBytes = cipher.doFinal(cleartext);
         byte[] encryptedPwd = Base64.encodeBase64(clearBytes);
         return encryptedPwd;
      } catch (Exception e) {
         Log.e(Flashum.LOG_TAG, "DatabaseHelper " + e.toString());
      }
      return null;
   }

   public String decryptPassword(byte[] userpw) {
      String pw = "";
      try {
         byte[] encrypedPwdBytes = Base64.decodeBase64(userpw);

         Cipher cipher = Cipher.getInstance("DES");
         cipher.init(Cipher.DECRYPT_MODE, key);
         byte[] plainTextPwdBytes = cipher.doFinal(encrypedPwdBytes);
         pw = new String(plainTextPwdBytes, "UTF8");
      } catch (Exception e) {
         Log.e(Flashum.LOG_TAG, "DatabaseHelper " + e.toString());
      }
      return pw;
   }
