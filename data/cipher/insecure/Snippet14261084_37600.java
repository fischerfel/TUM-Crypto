...
    //*********************************************
    byte[] bArray = {23, 42, 55};
    String stringRepresentation = bArray[0] + "," + bArray[1] + "," + bArray[2];
    System.out.println("Plain Byte Array: " + Arrays.toString(bArray));
    String eba = EncryptByteArray(bArray);
    System.out.println("Encrypted Byte Array: "+eba);
    byte[] deba = decryptByteArray(eba);
    System.out.println("Decrypted Byte Array: "+Arrays.toString(deba));
    //*********************************************
}

public static String EncryptByteArray(byte[] array) throws Exception
{
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    String encryptedString = Base64.encodeBase64String(cipher.doFinal(array));
    return encryptedString;
}

public static byte[] decryptByteArray(String strToDecrypt) throws Exception
{
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
    SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    return cipher.doFinal(Base64.decodeBase64(strToDecrypt));
}
