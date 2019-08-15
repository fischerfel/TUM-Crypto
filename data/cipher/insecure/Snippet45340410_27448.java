public static String encryptData(byte[] clearData, String key, int keySize, boolean paddingEnable)
        throws Exception {
    byte[] keyBytes = getEncryptionKey(key, keySize);
    SecretKey secretKey = new SecretKeySpec(keyBytes, "DESede");
    String algo = "DESede/ECB/pkcs5padding";
    Cipher cipher = Cipher.getInstance(algo);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] cipherText = cipher.doFinal(clearData);
         System.out.println(clearData.toString());
    return String.valueOf(Hex.encodeHex(cipherText, false));
}

public static String decryptData(String clearData, String key, int keySize, boolean paddingEnable)
        throws Exception {
   byte[] keyBytes = getEncryptionKey(key, keySize);
    SecretKey secretKey = new SecretKeySpec(keyBytes, "DESede");
    String algo = "DESede/ECB/pkcs5padding";
    Cipher cipher = Cipher.getInstance(algo);
    byte[] stringDecode = Hex.decodeHex(clearData.toCharArray());
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    byte[] cipherText = cipher.doFinal(stringDecode);
    return cipherText.toString();
}


private static byte[] getEncryptionKey(String keyString, int keySize)
        throws Exception {
    int keyLength = keyString.length();
    switch (keySize) {
        case 56:
            if (keyLength != 16) {
                throw new InvalidKeyException("Hex Key length should be 16 for a 56 Bit Encryption, found [" + keyLength + "]");
            }
            break;
        case 112:
            if (keyLength != 32) {
                throw new InvalidKeyException("Hex Key length should be 32 for a 112 Bit Encryption, found[" + keyLength + "]");
            }
            break;
        case 168:
            if ((keyLength != 32) && (keyLength != 48)) {
                throw new InvalidKeyException("Hex Key length should be 32 or 48 for a 168 Bit Encryption, found[" + keyLength + "]");
            }
            if (keyLength == 32) {
                keyString = keyString + keyString.substring(0, 16);
            }
            break;
        default:
            throw new InvalidKeyException("Invalid Key Size, expected one of [56, 112, 168], found[" + keySize + "]");
    }
    return Hex.decodeHex(keyString.toCharArray());
}
