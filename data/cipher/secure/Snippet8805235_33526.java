public class Encryptor {

private SecretKey secretKey;
private Cipher cipher;

public Encryptor(String algorithmName, String paddingName, String key) {
    String keyHexCode = StringUtils.convertUnicodeToHexCode(key.getBytes());
    try {
        byte[] desKeyData = StringUtils.convertHexStringToByteArray(keyHexCode);

        DESKeySpec desKeySpec = null;
        try {
            desKeySpec = new DESKeySpec(desKeyData);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithmName);
        try {
            secretKey = keyFactory.generateSecret(desKeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        try {
            cipher = Cipher.getInstance(paddingName);
        } catch (NoSuchPaddingException e) {
            // TODO: handle exception
        }
    } catch (NoSuchAlgorithmException e) {
        // TODO: handle exception
    }
}

private void initEncryptor(int mode) {
    try {
        cipher.init(mode, secretKey);
    } catch (InvalidKeyException e) {
        // TODO: handle exception
    }
}

public String encrypt(String clearText) {
    initEncryptor(Cipher.ENCRYPT_MODE);
    try {
        // Encrypt the cleartext
        byte[] encryptedBytes = cipher.doFinal(clearText.getBytes());
        return StringUtils.convertUnicodeToHexCode(encryptedBytes).toUpperCase();
    } catch (IllegalBlockSizeException e) {
        // TODO: handle exception
    } catch (BadPaddingException e) {
        // TODO: handle exception
    }
    return "";
}

public String decrypt(String encryptedTextHex) {
    byte[] encryptedText = StringUtils.convertHexCodeSequenceToUnicode(encryptedTextHex);
    initEncryptor(Cipher.DECRYPT_MODE);
    try {
        // Decrypt the encryptedTextHex
        return new String(cipher.doFinal(encryptedText));
    } catch (IllegalBlockSizeException e) {
        // TODO: handle exception
    } catch (BadPaddingException e) {
        // TODO: handle exception
    }
    return "";
}
}
