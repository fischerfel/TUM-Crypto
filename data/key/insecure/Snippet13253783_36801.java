public String encrypt(String password){
    try
    {
        String key = "mysecretpassword";
        SecretKeySpec keySpec = null;
        keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return new String(cipher.doFinal(password.getBytes()));
    }
    catch (Exception e)
    {
        return null;
    }
}

public String decrypt(String password){
    try
    {
        String key = "mysecretpassword";
        SecretKeySpec keySpec = null;
        keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE,keySpec);
        return new String(cipher.doFinal(password.getBytes()));
    }
    catch (Exception e)
    {
        System.out.println(e);
        return null;
    }
}
