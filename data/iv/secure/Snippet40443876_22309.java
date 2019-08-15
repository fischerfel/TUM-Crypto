KeyGenerator keyGen = KeyGenerator.getInstance("AES");
keyGen.init(128);
SecretKey mSecretKey = keyGen.generateKey();

public byte[] encrypt(byte[] data) {
    try {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        SecretKeySpec k = new SecretKeySpec(mSecretKey.getEncoded(), "AES");
        c.init(Cipher.ENCRYPT_MODE, k);
        byte[] encryptedData = c.doFinal(data);
        return Bytes.concat(c.getIV(), encryptedData);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

public byte[] decrypt(byte[] encryptedData) {
    try {
        byte[] iv = Arrays.copyOfRange(encryptedData, 0, 16);
        SecretKeySpec k = new SecretKeySpec(mSecretKey.getEncoded(), "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        c.init(Cipher.DECRYPT_MODE, k, new IvParameterSpec(iv));
        byte[] decrypted = c.doFinal(Arrays.copyOfRange(encryptedData, 16, encryptedData.length));
        return decrypted;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
