String cipher = Encryption.encryptStrRSA(txt, pubKeyk);

public static String encryptStrRSA(String str, PublicKey pubKey)
    throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
    IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {

    Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);

    byte[] encryptedAesKey = cipher.doFinal(str.getBytes());
    String cipherStr = new String(encryptedAesKey);

    System.out.println(cipherStr);
    return cipherStr;
}

public static PublicKey strToPublicKey(String key64) throws GeneralSecurityException {
    byte[] data = Base64.getDecoder().decode(key64);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    return fact.generatePublic(spec);
}

public static String publicKeyToStr(PublicKey publ) throws GeneralSecurityException {
    KeyFactory fact = KeyFactory.getInstance("RSA");
    X509EncodedKeySpec spec = fact.getKeySpec(publ, X509EncodedKeySpec.class);
    return Base64.getEncoder().encode(spec.getEncoded()).toString();
}
