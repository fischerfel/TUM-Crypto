public static String SIMMETRICAL_ALGORITHM = "AES";

//Generate cipher key with user provided password
private static String getPassphraseSize16(String key) {
    if (TextUtils.isEmpty(key)) {
        return null;
    }
    char controlChar = '\u0014';
    String key16 = key + controlChar;
    if (key16.length() < 16) {
        while (key16.length() < 16) {
            key16 += key + controlChar;
        }
    }
    if (key16.length() > 16) {
        key16 = key16.substring(key16.length() - 16, key16.length());
    }
    return key16;
}

//AES cipher with passphrase
public static byte[] encrypt(byte[] message, String passphrase)
        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    String passphrase16 = getPassphraseSize16(passphrase);

    SecretKeySpec secretKey = new SecretKeySpec(passphrase16.getBytes(), SIMMETRICAL_ALGORITHM);
    Cipher cipher = Cipher.getInstance(SIMMETRICAL_ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] encoded = cipher.doFinal(message);

    return encoded;
}

//AES decipher with passphrase
public static byte[] decrypt(byte[] encodedMessage, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    String passphrase16 = getPassphraseSize16(key);

    SecretKeySpec secretKey = new SecretKeySpec(passphrase16.getBytes(), SIMMETRICAL_ALGORITHM);
    Cipher cipher = Cipher.getInstance(SIMMETRICAL_ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    byte decoded[] = cipher.doFinal(encodedMessage);

    return decoded;
}
