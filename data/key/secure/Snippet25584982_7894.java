public static void main(String[] args) throws Exception {
    Security.addProvider(new BouncyCastleProvider());

    KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECIES");
    ECGenParameterSpec brainpoolP256R1 = new ECGenParameterSpec(
            "brainpoolP256R1");
    kpg.initialize(brainpoolP256R1);
    KeyPair kp = kpg.generateKeyPair();

    Cipher c = Cipher.getInstance("ECIES");
    c.init(Cipher.ENCRYPT_MODE, kp.getPublic());

    final byte[] aesKeyData = new byte[16];
    SecureRandom rng = new SecureRandom();
    rng.nextBytes(aesKeyData);

    byte[] wrappedKey = c.doFinal(aesKeyData);
    SecretKey aesKey = new SecretKeySpec(aesKeyData, "AES");
    Arrays.fill(aesKeyData, (byte) 0);
}
