public void rsaSignatureIntegrityTest() {
    KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
    gen.initialize(2048, new SecureRandom());
    KeyPair pair = gen.generateKeyPair();

    byte[] digest = MessageDigest.getInstance("SHA-256").digest(MESSAGE);
    Signature signer = Signature.getInstance("NONEwithRSA");
    signer.initSign(pair.getPrivate());
    signer.update(digest);
    byte[] signed = signer.sign();

    Signature verifier = Signature.getInstance("SHA256withRSA");
    verifier.initVerify(pair.getPublic());
    verifier.update(MESSAGE);
    verifier.verify(signed);
}
