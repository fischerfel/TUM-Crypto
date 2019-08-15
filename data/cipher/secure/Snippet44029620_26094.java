    public static KeyPair generateKeyPairRSA()  {
    try {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024, random);
        KeyPair keyPair = keyGen.generateKeyPair();
        return keyPair;
    } catch (Exception e) {
        Log.d(TAG,e.getLocalizedMessage());
    }
    return null;
}


public byte[] RSAEncrypt(final String plain, PublicKey publicKey) throws Exception {
    Cipher cipher = Cipher.getInstance(ALGO_RSA);
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] encryptedBytes = cipher.doFinal(plain.getBytes());
    return encryptedBytes;
}

public static PublicKey loadPublicKey1(String stored) throws Exception{
    byte[] data = Base64.decode(stored.getBytes());
    X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
    KeyFactory fact = KeyFactory.getInstance(ALGO_RSA);
    return fact.generatePublic(spec);
}
