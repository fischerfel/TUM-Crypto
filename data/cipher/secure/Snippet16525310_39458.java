public static KeyPair generateDSAKey() {
    KeyPair pair = null;
    try {
        KeyPairGenerator keyGen = KeyPairGenerator
                .getInstance("DSA", "SUN");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);
        pair = keyGen.generateKeyPair();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return pair;
}
public static KeyPair generateRSAKey() {
    KeyPairGenerator kpg;
    KeyPair kp = null;
    try {
        kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        kp = kpg.genKeyPair();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return kp;
}

public static byte[] encryptRSA(byte[] msg, PublicKey pubKey) {
    byte[] cipherData = null;
    try {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        cipherData = cipher.doFinal(msg);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return cipherData;
}
