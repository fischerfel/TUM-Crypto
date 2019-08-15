String PRIVATE_RSA_KEY_PKCS8 = 
    "-----BEGIN PRIVATE KEY-----\n" +
    "MDSTofml23d....\n" +
    [...] +
    "-----END PRIVATE KEY-----\n";
String key = PRIVATE_RSA_KEY_PKCS8
    .replace("-----BEGIN PRIVATE KEY-----\n", "")
    .replace("\n-----END PRIVATE KEY-----\n", "");
PKCS8EncodedKeySpec keySpec =
    new PKCS8EncodedKeySpec(DatatypeConverter.parseBase64Binary(key));
try {
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
    Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] bytes = parseBase64Binary(encryptedNodeIdentifier);
    byte[] decryptedData = cipher.doFinal(bytes);
    return new String(decryptedData);
} catch (GeneralSecurityException e) {
    return "";
}
