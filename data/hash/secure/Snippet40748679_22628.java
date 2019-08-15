public String EncryptText(String input) throws NoSuchAlgorithmException
{
    byte[] bytesToBeEncrypted = input.getBytes();
    byte[] passwordBytes = Config.ServerKey.getBytes();
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    passwordBytes = md.digest(passwordBytes);
    byte[] bytesEncrypted = null;
try {
    bytesEncrypted = AES_Encrypt(bytesToBeEncrypted, passwordBytes);
} catch (NoSuchPaddingException ex) {
    Logger.getLogger(CryptoClass.class.getName()).log(Level.SEVERE, null, ex);
} catch (InvalidAlgorithmParameterException ex) {
    Logger.getLogger(CryptoClass.class.getName()).log(Level.SEVERE, null, ex);
} catch (InvalidKeyException ex) {
    Logger.getLogger(CryptoClass.class.getName()).log(Level.SEVERE, null, ex);
} catch (IllegalBlockSizeException ex) {
    Logger.getLogger(CryptoClass.class.getName()).log(Level.SEVERE, null, ex);
} catch (BadPaddingException ex) {
    Logger.getLogger(CryptoClass.class.getName()).log(Level.SEVERE, null, ex);
}
    return Base64.getEncoder().encodeToString(bytesEncrypted);
}
