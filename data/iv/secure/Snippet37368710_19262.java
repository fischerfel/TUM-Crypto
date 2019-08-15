public static String encryptAES(String data, String secretKey) {
    try {
        byte[] secretKeys = Hashing.sha1().hashString(secretKey, Charsets.UTF_8)
                .toString().substring(0, 16)
                .getBytes(Charsets.UTF_8);

        final SecretKey secret = new SecretKeySpec(secretKeys, "AES");

        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);

        final AlgorithmParameters params = cipher.getParameters();

        final byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        final byte[] cipherText = cipher.doFinal(data.getBytes(Charsets.UTF_8));

        return DatatypeConverter.printHexBinary(iv) + DatatypeConverter.printHexBinary(cipherText);
    } catch (Exception e) {
        throw Throwables.propagate(e);
    }
}


public static String decryptAES(String data, String secretKey) {
    try {
        byte[] secretKeys = Hashing.sha1().hashString(secretKey, Charsets.UTF_8)
                .toString().substring(0, 16)
                .getBytes(Charsets.UTF_8);

        // grab first 16 bytes - that's the IV
        String hexedIv = data.substring(0, 32);

        // grab everything else - that's the cipher-text (encrypted message)
        String hexedCipherText = data.substring(32);

        byte[] iv = DatatypeConverter.parseHexBinary(hexedIv);
        byte[] cipherText = DatatypeConverter.parseHexBinary(hexedCipherText);

        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKeys, "AES"), new IvParameterSpec(iv));

        return new String(cipher.doFinal(cipherText), Charsets.UTF_8);
    } catch (BadPaddingException e) {
        throw new IllegalArgumentException("Secret key is invalid");
    }catch (Exception e) {
        throw Throwables.propagate(e);
    }
}
