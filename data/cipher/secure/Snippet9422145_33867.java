private static void testCode() {
    try {
        String stringDec = "Hi there";
        SecretKey aesKey = new SecretKeySpec(new byte[16], "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);

        // no encoding given, don't use getBytes() without a Charset.forName("UTF-8")
        byte[] data = cipher.doFinal(stringDec.getBytes());
        byte[] iv = cipher.getIV();

        // doesn't do anything
        AlgorithmParameters.getInstance("AES");

        cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(iv));
        byte[] decrypted = cipher.doFinal(data);
        System.out.println(new String(decrypted));
    } catch (GeneralSecurityException e) {
        throw new IllegalStateException(e);
    }
}
