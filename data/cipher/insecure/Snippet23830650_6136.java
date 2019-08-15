public static CipherOutputStream encryptedStream(OutputStream out) {
    try {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return new CipherOutputStream(out, pbeCipher);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(CryptoHandler.class.getName()).log(Level.SEVERE, null, ex);
    }//Much more catches here...
}
public static ObjectInputStream decryptedObjectStream(InputStream in) {
    try {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return new ObjectInputStream(new CipherInputStream(in, pbeCipher));
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(CryptoHandler.class.getName()).log(Level.SEVERE, null, ex);
    } //Much more catches here...
}
