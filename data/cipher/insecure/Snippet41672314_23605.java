protected static byte[] encrypt(String text) 
    {
        try
        {
            String key = "6589745268754125";
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            return encrypted;
        }
        catch(Exception ex)
        {
            WriteLog("Encryption Failed");
            WriteLog(ex.getMessage());
            return null;
        }
    }

protected static String decrypt(byte[] pass)
{
    try
    {
        String key = "6589745268754125";
        // Create key and cipher
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        // decrypt the text
        cipher.init(Cipher.DECRYPT_MODE, aesKey);           
        String decrypted = new String(cipher.doFinal(pass));        
        return decrypted;
    }
    catch(Exception ex)
    {
        WriteLog("Encryption Failed");      
        WriteLog(ex.getMessage());
        return null;
    }
}
