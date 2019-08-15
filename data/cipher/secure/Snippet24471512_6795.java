public static String encryptAES(String key, String source) {
String encrypted = "";
try {  
    SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
    IvParameterSpec iv = new IvParameterSpec(new byte[16]);
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv); 
    byte[] bXml = source.getBytes("UTF8");
    byte[] enc = cipher.doFinal(bXml);
    encrypted = new sun.misc.BASE64Encoder().encode(enc); 
} catch (Exception e) {  /** removed **/ }
return encrypted;
}
public static String decryptAES(String key, String source) {
String decrypted = "";
try {  
    SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");     
    IvParameterSpec iv = new IvParameterSpec(new byte[16]);
    cipher.init(Cipher.DECRYPT_MODE, keySpec, iv); 
    byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(source); 
    byte[] bDecrypted = cipher.doFinal(dec);  /** Error occurs here **/
    decrypted = new String(bDecrypted, "UTF8");     
} catch (Exception e) {  /** removed **/ }  
return decrypted;
}
