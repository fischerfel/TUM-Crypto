public void validateUserPassword(String encryptedPassword) {
    String algorithm = "AES";
    SecretKeySpec  keySpec = null;
    byte[] key =  "<==OMGWTFBBQ!==>".getBytes();

    Cipher cipher = null;

    cipher = Cipher.getInstance(algorithm);
    keySpec = new SecretKeySpec(key, algorithm);

    byte[] encryptionBytes = new sun.misc.BASE64Decoder().decodeBuffer(encryptedPassword);      
    cipher.init(Cipher.DECRYPT_MODE, keySpec);
    byte[] recoveredBytes = cipher.doFinal(encryptionBytes);
    String recovered = new String(recoveredBytes);

    log.info("Encrypted password: " + encryptedPassword);
    log.info("Dencrypted password: " + recovered);
}
