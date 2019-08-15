private static void stackOverflow15554296()
    throws
        NoSuchAlgorithmException, NoSuchPaddingException,        
        InvalidKeyException, IllegalBlockSizeException,
        BadPaddingException
{

    // prepare key
    KeyGenerator keygen = KeyGenerator.getInstance("AES");
    SecretKey aesKey = keygen.generateKey();
    String aesKeyForFutureUse = Base64.getEncoder().encodeToString(
            aesKey.getEncoded()
    );

    // cipher engine
    Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

    // cipher input
    aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
    byte[] clearTextBuff = "Text to encode".getBytes();
    byte[] cipherTextBuff = aesCipher.doFinal(clearTextBuff);

    // recreate key
    byte[] aesKeyBuff = Base64.getDecoder().decode(aesKeyForFutureUse);
    SecretKey aesDecryptKey = new SecretKeySpec(aesKeyBuff, "AES");

    // decipher input
    aesCipher.init(Cipher.DECRYPT_MODE, aesDecryptKey);
    byte[] decipheredBuff = aesCipher.doFinal(cipherTextBuff);
    System.out.println(new String(decipheredBuff));
}
