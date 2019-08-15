public static PublicKey getPublicKeyFromString(String stringKey) throws Exception {
    byte[] keyBytes = stringKey.getBytes();
    byte[] decode = Base64.decode(keyBytes, Base64.DEFAULT);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(decode);
    return (PublicKey) fact.generatePublic(x509KeySpec);
}
public static String RSAEncrypt(final String plain, final PublicKey publicKey)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    byte[] encryptedBytes;
    Cipher cipher;      
    cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    encryptedBytes = cipher.doFinal(plain.getBytes());
    return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
}
//I call these functions in this manner.
private final String pubKeyString = 
//"-----BEGIN PUBLIC KEY-----" +
"MIG..." +
"...";
//"-----END PUBLIC KEY-----"
PublicKey pubKey = RSAFunctions.getPublicKeyFromString(pubKeyString);
String encData = RSAFunctions.RSAEncrypt("prova", pubKey);
