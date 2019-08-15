private static String decrypt_data(String encData)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    String key = "bad8deadcafef00d";
    SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES");

    cipher.init(Cipher.DECRYPT_MODE, skeySpec);

    System.out.println("Base64 decoded: "
            + Base64.decode(encData.getBytes()).length);
    byte[] original = cipher
            .doFinal(Base64.decode(encData.getBytes()));
    return new String(original).trim();
}

private static String encrypt_data(String data)
        throws Exception {
    String key = "bad8deadcafef00d";
    SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES");

    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

    System.out.println("Base64 encoded: "
            + Base64.encode(data.getBytes()).length);

    byte[] original = Base64.encode(cipher.doFinal(data.getBytes()));
    return new String(original);
}
