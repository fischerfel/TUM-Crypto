    public static SecretKey generateSecretKey(String keyPassword)
{
    SecretKey key = null;
    try
    {
        SecretKeyFactory method = SecretKeyFactory.getInstance("PBEWithMD5AndDES");

        //System.out.println("salt length: " + new SaltIVManager().getSalt().length);

        PBEKeySpec spec = new PBEKeySpec(keyPassword.toCharArray(), new SaltIVManager().getSalt(), 10000, 128);

        key = method.generateSecret(spec);

        System.out.println("generate secret key length: " + key.getEncoded().length); 

    }catch(Exception e)
    {
        e.printStackTrace();
    }
    return key;
}
