     Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

    byte[] input = acVote;
    byte[] cipherText = null;

    Cipher cipher = null;

    try {
    cipher = Cipher.getInstance("RSA/ECB/OAEPPadding", "BC");

    SecureRandom random = new SecureRandom();

    //do encryption

    cipher.init(Cipher.ENCRYPT_MODE, pubKey, random);

    cipherText = cipher.doFinal(input);
    } catch (Exception ex) {
        log.error("Exeption Message : " + ex);
    }
