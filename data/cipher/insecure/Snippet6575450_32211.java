Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
byte[] keyBytes = DES_KEY.getBytes(); //<== The same as above

SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

try {
    // Return the raw bytes 
    byte []data = Base64.decode(encryptedContent);

    // Gets the Cipher...
    final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
    cipher.init(Cipher.DECRYPT_MODE, keySpec);

    String resultString = new String(cipher.doFinal(data));
} catch (Exception ex) {
    ...
}
