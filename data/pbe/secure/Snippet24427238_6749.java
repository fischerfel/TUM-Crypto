public static String AESencryptString(String clearStr) throws Exception {
    String cipherStr = null;

    //génération de la clé de cryptage AES
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec spec = new PBEKeySpec(KEY.toCharArray());
    Log.d("test", ""+ spec);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey key = new SecretKeySpec(tmp.getEncoded(), "AES");

    //cryptage du mot de passe
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] cipherByteArray = cipher.doFinal(clearStr.getBytes("UTF-8"));

    //convertion du mot de passe en String pour l'enregistrement en base
    cipherStr = new String(Base64.encode(cipherByteArray, 0));

    return cipherStr;
}

public static String AESdecryptString(String cipherStr) throws Exception {
    String clearStr = null;

    //génération de la clé de cryptage AES
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(KEY.toCharArray());
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey key = new SecretKeySpec(tmp.getEncoded(), "AES");

    //décryptage du mot de passe
    Cipher decipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    decipher.init(Cipher.DECRYPT_MODE, key);
    byte[] clearByteArray = decipher.doFinal(cipherStr.getBytes());

    //convertion du mot de passe en String pour l'enregistrement en base
    clearStr = new String(Base64.encode(clearByteArray, 0));

    return clearStr;
}
