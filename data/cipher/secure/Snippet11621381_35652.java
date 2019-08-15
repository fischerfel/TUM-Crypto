public static synchronized byte[] encryptPrivateKey(
        ElGamalPrivateKey privateKey, byte[] hashedAnsBytes)
        throws GeneralSecurityException, IOException {
    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    MessageDigest digest = MessageDigest.getInstance("SHA256");
    final byte[] reallyHashed = digest.digest(hashedAnsBytes);
    System.out.println(reallyHashed.length);
    SecretKeySpec key = new SecretKeySpec(reallyHashed, "AES");
    SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
    final byte[] iv = new byte[c.getBlockSize()];
    rnd.nextBytes(iv);
    IvParameterSpec spec = new IvParameterSpec(iv);
    c.init(Cipher.ENCRYPT_MODE, key, spec);

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutput out = new ObjectOutputStream(bos);
    out.write(iv);
    out.write(c.doFinal(privateKey.getEncoded()));
    out.close();
    bos.close();
    byte[] pvtKeyBytes = c.doFinal(bos.toByteArray());
    return pvtKeyBytes;
}
