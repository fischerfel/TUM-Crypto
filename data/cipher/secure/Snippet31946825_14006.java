@Test
public void testPadding() throws Exception {
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(1024, random);
    KeyPair keyPair = keyGen.generateKeyPair();

    /* constant 117 is a public key size - 11 */
    byte[] plaintext = new byte[117];
    random.nextBytes(plaintext);

    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
    byte[] ciphertext = cipher.doFinal(plaintext);
    System.out.println(plaintext.length + " becomes " + ciphertext.length);
}
