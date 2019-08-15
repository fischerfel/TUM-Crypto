private String rsaEncrypt (String plain) {

    byte [] encryptedBytes;
    Cipher cipher = Cipher.getInstance("RSA");
    PublicKey publicKey = getPublicKey();
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    encryptedBytes = cipher.doFinal(plain.getBytes());
    String encrypted = new String(encryptedBytes);
    return encrypted;
