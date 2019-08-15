private static byte[] testCipher() {

    try {

        final byte[] encrypted = Base64.decode("R3JhbmRlIFZpY3RvaXJlICE=", Base64.NO_WRAP);

        byte[] keyBytes = Base64.decode("L/91ZYrliXvmhYt9FKEkkDDni+PzcnOuV9cikm188+4=", Base64.NO_WRAP);
        byte[] ivBytes = Base64.decode("gqjFHI+YQiP7XYEfcIEJHw==".getBytes(), Base64.NO_WRAP);

        final IvParameterSpec ivSpecForData = new IvParameterSpec(ivBytes);

        SecretKeySpec decodedKeySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, decodedKeySpec, ivSpecForData);

        byte[] decodedBytes = cipher.doFinal(encrypted); // I have the error here //

        return decodedBytes;

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
