public String encrypt(String message, SecretKey skey) {
    byte[] raw = skey.getEncoded();
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

    // Instantiate the cipher

    Cipher cipher;
    byte[] encrypted = null;
    try {
        cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        encrypted = cipher.doFinal(message.getBytes());
        System.out.println("raw is " + encrypted);

    } catches
    return asHex(encrypted);
}
