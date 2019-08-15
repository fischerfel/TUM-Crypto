public class crypto {

    static String IV = "AAAAAAAAAAAAAAAA";
    static String plaintext = "my non padded text";
    static String encryptionKey = "0123456789abcdef";

    public static void main(String[] args) 
    {
        byte[] cipher = encrypt(plaintext);
        String decrypted = decrypt(cipher);
    }

    protected static String covertto16Byte(String plainText)
    {
        while(plainText.length()%16 != 0)
            plainText += "\0";          
        return plainText;
    }

    protected static String removePadding(String plainText)
    {
        return plainText.replace("\0","");
    }

    protected static byte[] encrypt(String plainText) 
    {
        try 
        {
            String _plaintText_16 = covertto16Byte(plainText);
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
            SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
            return cipher.doFinal(_plaintText_16.getBytes("UTF-8"));
        } catch (Exception ex) 
        {
            //catch mechanism
            return null;
        }
    }

    protected static String decrypt(byte[] cipherText) 
    {
        try 
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
            SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
            return removePadding(new String(cipher.doFinal(cipherText), "UTF-8"));
        } catch (Exception ex) 
        {   
            //catch mechanism
            return null;
        }
    }    
}
