 public static String encrypt(String originalPassword) throws Exception {
    String methodName = "encrypt -->";
    _logger.debug(methodName + Constants.CALLED);
    String encryptedString = null;
    try {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        encryptedString = base64Encode(pbeCipher.doFinal(originalPassword.getBytes("UTF-8")));
        _logger.debug(methodName + "encrypted string " + encryptedString);
    }  catch (Exception e) {
        _logger.error(methodName + "Encryption failed due to: " + e.getMessage());
        throw new Exception("Failed to Encrypt String");
    }
    _logger.debug(methodName + Constants.END);
    return encryptedString;
}

public static String decrypt(String encryptedPassword) throws Exception {
        String methodName = "decrypt -->";
        _logger.debug(methodName + Constants.CALLED);
        String decryptedString = null;
        try {
            _logger.debug(methodName + " string to decrypt " + encryptedPassword);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            decryptedString = new String(pbeCipher.doFinal(base64Decode(encryptedPassword)), "UTF-8");
        } catch (Exception e) {
            _logger.error(methodName + "Decryption failed due to: " + e.getMessage());
            throw new Exception("Failed to Decrypt String");
        }
        _logger.debug(methodName + Constants.END);
        return decryptedString;
    }
