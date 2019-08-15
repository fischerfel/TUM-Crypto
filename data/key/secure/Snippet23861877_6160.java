String dataEncrypted = new String();
try {
    Cipher aesCipher = Cipher.getInstance("AES");
    byte[] raw = hexToBytes(key);
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    aesCipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] byteDataToEncrypt = data.getBytes();
    byte[] byteCipherText = aesCipher.doFinal(byteDataToEncrypt);
    dataEncrypted = new BASE64Encoder().encode(byteCipherText);
    return dataEncrypted;
} catch (Exception ex) {
    //log.d(ex.getMessage());
}
