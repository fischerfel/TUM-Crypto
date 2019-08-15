public class utils {

public static String encrypt(String message,String secretPhrase){
    try{
        MessageDigest mdig=MessageDigest.getInstance("MD5");
        byte[] digestedBytes=mdig.digest(secretPhrase.getBytes("UTF-8"));
        SecretKeySpec keySpec=new SecretKeySpec(digestedBytes,"AES");

        Cipher cipher=Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedBytes=cipher.doFinal(message.getBytes("UTF-8"));

        return new String(encryptedBytes,"UTF-8");
    }catch(Exception exc){
        return null;
    }
}

public static String decrypt(String message,String secretPhrase){
    try{
        MessageDigest mdig=MessageDigest.getInstance("MD5");
        byte[] digestedBytes=mdig.digest(secretPhrase.getBytes("UTF-8"));
        SecretKeySpec keySpec=new SecretKeySpec(digestedBytes,"AES");

        Cipher cipher=Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] encryptedBytes=cipher.doFinal(message.getBytes("UTF-8"));

        return new String(encryptedBytes,"UTF-8");
    }catch(Exception exc){
        return null;
    }
}
