encryptionAlgorithm = "RSA/ECB/PKCS1Padding";
algorithm = "RSA";

try {
    SecureRandom random = SecRandom.getDefault();

    // Since you are working with asymmetric crypto, you need a keypair:

    KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm);
    kpg.initialize(2048, random);
    KeyPair kp = kpg.generateKeyPair();

    // encrypting something with asymmetric crypto needs a public key:

    Cipher cipher1 = Cipher.getInstance(encryptionAlgorithm);
    cipher1.init(Cipher.ENCRYPT_MODE, kp.getPublic());

    byte[] text = "This is a test".getBytes("ASCII");

    System.out.println("text = " +(new String(text)));

    byte[] ciphertext = cipher1.doFinal(text);

    // here you could store & load your sipertext

    System.out.println("ciphertext = " + ciphertext);

    // decrypting something with asymmetric crypto needs a private key:

    Cipher cipher2 = Cipher.getInstance(encryptionAlgorithm);
    cipher2.init(Cipher.DECRYPT_MODE, kp.getPrivate());

    byte[] cleartext = cipher2.doFinal(ciphertext);

    System.out.println("cleartext = " +(new String(cleartext)));

} catch (Exception e) {
    e.printStackTrace();
}
