  public static String generateKey(String eisId) 
  {
    String uuidKey = null;
    try {

        KeyGenerator gen = KeyGenerator.getInstance("AES");
        gen.init(128); /* 128-bit AES */
        SecretKey secret = gen.generateKey();
        uuidKey = secret.getEncoded().toString();
        System.out.println("uuidKey : "+uuidKey);

        // Store in DB
        // **********************

    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return uuidKey;
}

public static SealedObject encryptData(String eisId, SecurityDomainDTO sDObj) 
{
    try
    {
        String secret = generateKey(eisId);
        SecretKeySpec aesKey = new SecretKeySpec(secret.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        SealedObject so = new SealedObject(sDObj, cipher);

        return so;
    } 
    catch (Exception e) 
    {
        System.out.println("Error while encrypting: " + e.toString());
    }
    return null;
}
