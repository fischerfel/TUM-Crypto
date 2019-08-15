//use to encrypt key
public static byte[] encryptA(byte[] value) throws GeneralSecurityException, IOException
{
    SecretKeySpec sks = getSecretKeySpec(true);
    System.err.println("encrypt():\t" + sks.toString());
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
    byte[] encrypted = cipher.doFinal(value);
    return encrypted;
}

//use to encrypt data
public static byte[] encrypt2(byte[] value) throws GeneralSecurityException, IOException
{
    SecretKeySpec key1 = getSecretKeySpec(true);
    System.err.println("encrypt():\t" + key1.toString());
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, key1, cipher.getParameters());
    byte[] encrypted = cipher.doFinal(value);

    SecretKeySpec key2 = getSecretKeySpec(false);
    System.err.println("encrypt():\t" + key2.toString());
    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key2, new IvParameterSpec(getIV()));
    byte[] encrypted2 = cipher.doFinal(encrypted);

    return encrypted2;//Base64Coder.encode(encrypted2);
}
//use to decrypt data
public static byte[] decrypt2(byte[] message, boolean A) throws GeneralSecurityException, IOException
{
    SecretKeySpec key1 = getSecretKeySpec(false);
    System.err.println("decrypt():\t" + key1.toString());
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, key1, new IvParameterSpec(getIV()));
    byte[] decrypted = cipher.doFinal(message);

    SecretKeySpec key2 = getSecretKeySpec(true);
    System.err.println("decrypt():\t" + key2.toString());
    cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, key2);
    byte[] decrypted2 = cipher.doFinal(decrypted);

    return decrypted2;
}

    //use to decrypt key
public static byte[] decryptKey(String message, byte[] key) throws GeneralSecurityException
{
    SecretKeySpec sks = new SecretKeySpec(key, ALGORITHM);
    System.err.println("decryptKey()");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, sks);
    byte[] decrypted = cipher.doFinal(Base64Coder.decode(message));
    return decrypted;
}

//method for fetching keys
private static SecretKeySpec getSecretKeySpec(boolean fromSO) throws NoSuchAlgorithmException, IOException, GeneralSecurityException
{
    return new SecretKeySpec(fromSO ? getKeyBytesFromSO() : getKeyBytesFromAssets(), "AES");
}
