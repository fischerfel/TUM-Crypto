public String encrypt(String key, String data) {
    if (key == null || data == null)
        return null;
    try {
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(charsetName));
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
        SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
        byte[] dataBytes = data.getBytes(charsetName);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.encodeToString(cipher.doFinal(dataBytes), base64Mode);
    } catch (Exception e) {
        return null;
    }
}

public String decrypt(String key, String data) {
    if (key == null || data == null)
        return null;
    try {
        byte[] dataBytes = Base64.decode(data, base64Mode);
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(charsetName));
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
        SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] dataBytesDecrypted = (cipher.doFinal(dataBytes));
        return new String(dataBytesDecrypted);
    } catch (Exception e) {
        return null;
    }
}
