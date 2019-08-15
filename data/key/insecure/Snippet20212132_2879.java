public class CipherUtils
{
  static Log log = LogFactory.getLog(CipherUtils.class);
  private static byte[] key = "xxxxxx".getBytes();
  private static byte[] iv = "xxxxxx".getBytes();

  public static String[] decrypt(String[] strToDecrypt){
     try{
       String[] strDecrypted;
       //Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
       Cipher cipher = Cipher.getInstance("aes/cbc/nopadding");
       final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
       IvParameterSpec ips = new IvParameterSpec(iv);
       //cipher.init(Cipher.DECRYPT_MODE, secretKey);
       cipher.init(Cipher.DECRYPT_MODE, secretKey,ips);
       for(int i =0;i<strToDecrypt.length ; i++){
          final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt [i])));
          strDecrypted[i] = decryptedString ;
       }
       return strDecrypted;    
     }catch (Exception e){
         log.error("Error while decrypting : " + strToDecrypt , e);
      }
     return null;
 }

public static void main(String args[]) throws Exception
{
     String array[] =      {"yXTVA6oG4kWOlvfKN/qXwa3VgEyiBu4kkgKh9WHt0s8=","yX7JI7IaExK3eBC6BU5RdCvkCrAAcyV3YTmHqYH5nG0=","yj56tfZEh3405yEwladp+ml/nk/h8Cx56XnP5Ycdeio="};

     String a = CipherUtils.decrypt(array);
     System.out.println(">>>"+a.trim());
  }
}
