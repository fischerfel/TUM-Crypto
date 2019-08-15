public class AES {
    public static String encrypt(String message, String key){
        try {
            SecretKeySpec KS = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, KS);
            byte[] encrypted = cipher.doFinal(message.getBytes());      
            return Base64.encodeToString(encrypted, Base64.NO_PADDING);
        } catch (Exception e) {
             return "ERROR:"+e.getMessage();
        }
    }

    public static String decrypt(String chiperText, String key){
        try {

            SecretKeySpec KS = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, KS);
            byte[] decrypted = cipher.doFinal(Base64.decode(chiperText, Base64.NO_PADDING));
            return new String(decrypted);
        } catch (Exception e) {
             return "ERROR";
        }
    }
