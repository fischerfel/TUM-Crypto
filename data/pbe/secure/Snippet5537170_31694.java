private byte[] encrypt (Object obj, String pass) throws Exception 
{
    PBEKeySpec keySpec = new PBEKeySpec(pass.toCharArray());
    SecretKey secretKey =
        SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
    Cipher c = Cipher.getInstance(secretKey.getAlgorithm());
    c.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] encrypted = c.doFinal(obj);
    return encrypted;
}
