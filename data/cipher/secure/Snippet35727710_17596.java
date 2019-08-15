    public static byte[] encryptSecretKey(SecretKey secretKey, PublicKey publicKey)
{
    byte[] encryptedSecret = null;
    try
    {

        Cipher cipher = Cipher.getInstance("RSA/ECB/NOPADDING");

        System.out.println("provider: " + cipher.getProvider().getName());

        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        System.out.println("original secret key: " + Base64.getEncoder().encodeToString(secretKey.getEncoded()) + " \n secretkey encoded length: " + secretKey.getEncoded().length);

        encryptedSecret = cipher.doFinal(secretKey.getEncoded());


        System.out.println("encrypted secret: " + Base64.getEncoder().encodeToString(encryptedSecret)); 

    }catch(Exception e)
    {
        e.printStackTrace();
    }
    return encryptedSecret;
}



public static SecretKey decryptSecretKey(byte[] encryptedKey, PrivateKey privateKey)
{
    SecretKey returnKey = null;
    try
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/NOPADDING");

        System.out.println("provider: " + cipher.getProvider().getName());

        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        System.out.println("encryptedkey length: " + encryptedKey.length);

        byte [] encodedSecret = cipher.doFinal(encryptedKey);

        System.out.println("encoded Secret after decrypt: " + Base64.getEncoder().encodeToString(encodedSecret));

        returnKey = new SecretKeySpec(encodedSecret, 0, encodedSecret.length, "PBEWithMD5AndDES");

        System.out.println("secret key: " + Base64.getEncoder().encodeToString(returnKey.getEncoded()));

        System.out.println("secret key length post decrypt: " + returnKey.getEncoded().length);
    }catch(Exception e)
    {
        e.printStackTrace();
    }
    return returnKey;
}
